import { defineStore } from 'pinia'
import { ref } from 'vue'
import { api } from '../services/api'
import { Notify } from 'quasar'

export const useDashboardStore = defineStore('dashboard', () => {
  const stats = ref({
    salesToday: 0,
    approvedToday: 0,
    pendingSettlement: 0,
    settlementMonth: 0
  })
  
  const loading = ref(false)

  const fetchDashboardStats = async () => {
    try {
      loading.value = true
      const response = await api.get('/dashboard/stats')
      stats.value = response.data
    } catch (error) {
      console.error('Error fetching dashboard stats:', error)
      Notify.create({
        type: 'negative',
        message: 'Error al cargar las estadísticas del dashboard'
      })
    } finally {
      loading.value = false
    }
  }

  const formatCurrency = (amount) => {
    if (!amount) return '$0'
    return new Intl.NumberFormat('es-AR', {
      style: 'currency',
      currency: 'ARS'
    }).format(amount)
  }

  const formatNumber = (number) => {
    if (!number) return '0'
    return new Intl.NumberFormat('es-AR').format(number)
  }

  return {
    stats,
    loading,
    fetchDashboardStats,
    formatCurrency,
    formatNumber
  }
})
