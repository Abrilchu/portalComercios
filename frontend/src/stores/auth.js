import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { api } from '../services/api'
import { Notify } from 'quasar'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(null)
  const token = ref(localStorage.getItem('auth_token'))
  const loading = ref(false)

  const isAuthenticated = computed(() => !!token.value)
  const userRole = computed(() => user.value?.role)
  const isCashier = computed(() => userRole.value === 'cashier')
  const isManager = computed(() => userRole.value === 'manager')
  const isOwner = computed(() => userRole.value === 'owner')

  const login = async (credentials) => {
    try {
      loading.value = true
      const response = await api.post('/auth/login', credentials)
      
      token.value = response.data.token
      user.value = response.data
      
      localStorage.setItem('auth_token', token.value)
      localStorage.setItem('user_data', JSON.stringify(response.data))
      
      // Set axios default header
      api.defaults.headers.common['Authorization'] = `Bearer ${token.value}`
      
      Notify.create({
        type: 'positive',
        message: 'Inicio de sesión exitoso'
      })
      
      return response.data
    } catch (error) {
      const message = error.response?.data?.message || 'Error en el inicio de sesión'
      Notify.create({
        type: 'negative',
        message
      })
      throw error
    } finally {
      loading.value = false
    }
  }

  const logout = () => {
    token.value = null
    user.value = null
    
    localStorage.removeItem('auth_token')
    localStorage.removeItem('user_data')
    
    delete api.defaults.headers.common['Authorization']
    
    Notify.create({
      type: 'info',
      message: 'Sesión cerrada'
    })
  }

  const initializeAuth = () => {
    const savedToken = localStorage.getItem('auth_token')
    const savedUser = localStorage.getItem('user_data')
    
    if (savedToken && savedUser) {
      token.value = savedToken
      user.value = JSON.parse(savedUser)
      api.defaults.headers.common['Authorization'] = `Bearer ${savedToken}`
    }
  }

  const getUserBranchIds = computed(() => {
    if (!user.value) return []
    return user.value.branchIds || []
  })

  const getUserCashierIds = computed(() => {
    if (!user.value) return []
    return user.value.cashierIds || []
  })

  return {
    user,
    token,
    loading,
    isAuthenticated,
    userRole,
    isCashier,
    isManager,
    isOwner,
    getUserBranchIds,
    getUserCashierIds,
    login,
    logout,
    initializeAuth
  }
})
