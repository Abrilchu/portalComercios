import axios from 'axios'
import { Notify } from 'quasar'

// Create axios instance with base configuration
const api = axios.create({
  baseURL: process.env.NODE_ENV === 'production' ? '/api' : 'http://localhost:8000/api',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// Request interceptor to add auth token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('auth_token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Response interceptor for error handling
api.interceptors.response.use(
  (response) => {
    return response
  },
  (error) => {
    const { response, request } = error

    if (response) {
      // Server responded with error status
      const { status, data } = response

      switch (status) {
        case 401:
          // Unauthorized - redirect to login
          localStorage.removeItem('auth_token')
          localStorage.removeItem('user_data')
          
          if (window.location.pathname !== '/login') {
            window.location.href = '/login'
          }
          
          Notify.create({
            type: 'negative',
            message: 'Sesión expirada. Por favor, inicie sesión nuevamente.'
          })
          break

        case 403:
          Notify.create({
            type: 'negative',
            message: 'No tiene permisos para realizar esta acción.'
          })
          break

        case 404:
          Notify.create({
            type: 'negative',
            message: 'Recurso no encontrado.'
          })
          break

        case 422:
          // Validation errors
          if (data && data.errors) {
            const errorMessages = Object.values(data.errors).flat()
            errorMessages.forEach(message => {
              Notify.create({
                type: 'negative',
                message
              })
            })
          } else {
            Notify.create({
              type: 'negative',
              message: data.message || 'Error de validación.'
            })
          }
          break

        case 500:
          Notify.create({
            type: 'negative',
            message: 'Error interno del servidor. Por favor, contacte al administrador.'
          })
          break

        default:
          Notify.create({
            type: 'negative',
            message: data.message || `Error HTTP ${status}`
          })
      }
    } else if (request) {
      // Request made but no response received
      if (error.code === 'ECONNABORTED') {
        Notify.create({
          type: 'negative',
          message: 'La solicitud tardó demasiado tiempo. Verifique su conexión.'
        })
      } else {
        Notify.create({
          type: 'negative',
          message: 'Error de conexión. Verifique su conexión a internet.'
        })
      }
    } else {
      // Something else happened
      Notify.create({
        type: 'negative',
        message: 'Error inesperado. Por favor, intente nuevamente.'
      })
    }

    return Promise.reject(error)
  }
)

// Helper functions for common API patterns
const apiHelpers = {
  // GET request with error handling
  async get(url, config = {}) {
    try {
      const response = await api.get(url, config)
      return response
    } catch (error) {
      throw error
    }
  },

  // POST request with error handling
  async post(url, data, config = {}) {
    try {
      const response = await api.post(url, data, config)
      return response
    } catch (error) {
      throw error
    }
  },

  // PUT request with error handling
  async put(url, data, config = {}) {
    try {
      const response = await api.put(url, data, config)
      return response
    } catch (error) {
      throw error
    }
  },

  // DELETE request with error handling
  async delete(url, config = {}) {
    try {
      const response = await api.delete(url, config)
      return response
    } catch (error) {
      throw error
    }
  },

  // File download helper
  async downloadFile(url, filename, params = {}) {
    try {
      const response = await api.get(url, {
        params,
        responseType: 'blob'
      })

      // Create blob link to download
      const href = URL.createObjectURL(response.data)
      
      // Create "a" HTML element with href to file & click
      const link = document.createElement('a')
      link.href = href
      link.setAttribute('download', filename)
      document.body.appendChild(link)
      link.click()

      // Clean up "a" element & remove ObjectURL
      document.body.removeChild(link)
      URL.revokeObjectURL(href)

      return response
    } catch (error) {
      throw error
    }
  }
}

// Export both the axios instance and helper functions
export { api, apiHelpers }
export default api
