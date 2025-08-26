import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { api } from '../services/api'
import { Notify } from 'quasar'

export const usePaymentsStore = defineStore('payments', () => {
  const payments = ref([])
  const loading = ref(false)
  const pagination = ref({
    page: 0,
    size: 20,
    totalElements: 0,
    totalPages: 0
  })
  
  const filters = ref({
    fromDate: null,
    toDate: null,
    status: null,
    branchIds: null,
    cashierIds: null,
    minAmount: null,
    maxAmount: null,
    paymentId: '',
    orderId: ''
  })

  const lastSyncCursor = ref(new Date().toISOString())
  const pollingInterval = ref(null)

  const statusOptions = [
    { label: 'Aprobada', value: 'APROBADA', color: 'positive' },
    { label: 'Rechazada', value: 'RECHAZADA', color: 'negative' },
    { label: 'Pendiente Liquidación', value: 'PEND_LIQ', color: 'warning' },
    { label: 'Liquidada', value: 'LIQUIDADA', color: 'info' }
  ]

  const getStatusColor = (status) => {
    const option = statusOptions.find(opt => opt.value === status)
    return option?.color || 'grey'
  }

  const getStatusLabel = (status) => {
    const option = statusOptions.find(opt => opt.value === status)
    return option?.label || status
  }

  const fetchPayments = async (page = 0, resetData = true) => {
    try {
      loading.value = true
      
      const params = {
        ...filters.value,
        page,
        size: pagination.value.size
      }

      // Clean empty parameters
      Object.keys(params).forEach(key => {
        if (params[key] === null || params[key] === undefined || params[key] === '') {
          delete params[key]
        }
      })

      const response = await api.get('/payments', { params })
      
      if (resetData) {
        payments.value = response.data.content
      } else {
        payments.value.push(...response.data.content)
      }
      
      pagination.value = {
        page: response.data.number,
        size: response.data.size,
        totalElements: response.data.totalElements,
        totalPages: response.data.totalPages
      }

      // Update sync cursor
      lastSyncCursor.value = new Date().toISOString()
      
    } catch (error) {
      console.error('Error fetching payments:', error)
      Notify.create({
        type: 'negative',
        message: 'Error al cargar los pagos'
      })
    } finally {
      loading.value = false
    }
  }

  const fetchUpdatedPayments = async () => {
    try {
      const params = {
        sinceCursor: lastSyncCursor.value
      }

      const response = await api.get('/payments/updates', { params })
      
      if (response.data.length > 0) {
        // Update existing payments or add new ones
        response.data.forEach(updatedPayment => {
          const index = payments.value.findIndex(p => p.id === updatedPayment.id)
          if (index >= 0) {
            payments.value[index] = updatedPayment
          } else {
            payments.value.unshift(updatedPayment)
          }
        })
        
        // Update sync cursor
        lastSyncCursor.value = new Date().toISOString()
        
        Notify.create({
          type: 'info',
          message: `${response.data.length} pago(s) actualizado(s)`
        })
      }
    } catch (error) {
      console.error('Error fetching payment updates:', error)
    }
  }

  const getPaymentDetail = async (paymentId) => {
    try {
      const response = await api.get(`/payments/${paymentId}`)
      return response.data
    } catch (error) {
      console.error('Error fetching payment detail:', error)
      Notify.create({
        type: 'negative',
        message: 'Error al cargar el detalle del pago'
      })
      throw error
    }
  }

  const exportPayments = async (format = 'csv') => {
    try {
      const params = {
        ...filters.value
      }

      // Clean empty parameters
      Object.keys(params).forEach(key => {
        if (params[key] === null || params[key] === undefined || params[key] === '') {
          delete params[key]
        }
      })

      const response = await api.get(`/payments/export/${format}`, { 
        params,
        responseType: 'blob'
      })
      
      // Create download link
      const url = window.URL.createObjectURL(new Blob([response.data]))
      const link = document.createElement('a')
      link.href = url
      link.setAttribute('download', `payments.${format}`)
      document.body.appendChild(link)
      link.click()
      link.remove()
      window.URL.revokeObjectURL(url)
      
      Notify.create({
        type: 'positive',
        message: 'Exportación completada'
      })
    } catch (error) {
      console.error('Error exporting payments:', error)
      Notify.create({
        type: 'negative',
        message: 'Error al exportar los pagos'
      })
    }
  }

  const startPolling = () => {
    stopPolling()
    pollingInterval.value = setInterval(() => {
      fetchUpdatedPayments()
    }, 3000) // Poll every 3 seconds
  }

  const stopPolling = () => {
    if (pollingInterval.value) {
      clearInterval(pollingInterval.value)
      pollingInterval.value = null
    }
  }

  const setFilters = (newFilters) => {
    filters.value = { ...filters.value, ...newFilters }
  }

  const clearFilters = () => {
    filters.value = {
      fromDate: null,
      toDate: null,
      status: null,
      branchIds: null,
      cashierIds: null,
      minAmount: null,
      maxAmount: null,
      paymentId: '',
      orderId: ''
    }
  }

  return {
    payments,
    loading,
    pagination,
    filters,
    statusOptions,
    getStatusColor,
    getStatusLabel,
    fetchPayments,
    fetchUpdatedPayments,
    getPaymentDetail,
    exportPayments,
    startPolling,
    stopPolling,
    setFilters,
    clearFilters
  }
})
