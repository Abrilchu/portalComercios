/**
 * Export utility functions for data export functionality
 */

/**
 * Export data as CSV file
 * @param {Array} data - Array of objects to export
 * @param {Array} columns - Array of column definitions
 * @param {string} filename - Name of the file to download
 */
export const exportToCsv = (data, columns, filename = 'export.csv') => {
  // Create CSV header
  const headers = columns.map(col => col.label || col.name).join(',')
  
  // Create CSV rows
  const rows = data.map(item => {
    return columns.map(col => {
      let value = item[col.field || col.name]
      
      // Handle special formatting
      if (col.format && typeof col.format === 'function') {
        value = col.format(value)
      }
      
      // Escape commas and quotes
      if (typeof value === 'string') {
        value = `"${value.replace(/"/g, '""')}"`
      }
      
      return value || ''
    }).join(',')
  }).join('\n')
  
  // Combine header and rows
  const csvContent = `${headers}\n${rows}`
  
  // Create and download file
  downloadFile(csvContent, filename, 'text/csv')
}

/**
 * Export data as JSON file
 * @param {Array|Object} data - Data to export
 * @param {string} filename - Name of the file to download
 */
export const exportToJson = (data, filename = 'export.json') => {
  const jsonContent = JSON.stringify(data, null, 2)
  downloadFile(jsonContent, filename, 'application/json')
}

/**
 * Create and download a file
 * @param {string} content - File content
 * @param {string} filename - Name of the file
 * @param {string} mimeType - MIME type of the file
 */
const downloadFile = (content, filename, mimeType) => {
  const blob = new Blob([content], { type: mimeType })
  const url = URL.createObjectURL(blob)
  
  const link = document.createElement('a')
  link.href = url
  link.download = filename
  link.style.display = 'none'
  
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  
  // Clean up the URL object
  setTimeout(() => URL.revokeObjectURL(url), 100)
}

/**
 * Format currency for export
 * @param {number} amount - Amount to format
 * @param {string} currency - Currency code (default: ARS)
 * @param {string} locale - Locale for formatting (default: es-AR)
 * @returns {string} Formatted currency string
 */
export const formatCurrencyForExport = (amount, currency = 'ARS', locale = 'es-AR') => {
  if (amount === null || amount === undefined) return ''
  
  return new Intl.NumberFormat(locale, {
    style: 'currency',
    currency: currency
  }).format(amount)
}

/**
 * Format date for export
 * @param {string|Date} date - Date to format
 * @param {object} options - Formatting options
 * @returns {string} Formatted date string
 */
export const formatDateForExport = (date, options = {}) => {
  if (!date) return ''
  
  const defaultOptions = {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  }
  
  const formatOptions = { ...defaultOptions, ...options }
  
  return new Date(date).toLocaleDateString('es-AR', formatOptions)
}

/**
 * Format payment status for export
 * @param {string} status - Payment status
 * @returns {string} Formatted status
 */
export const formatPaymentStatusForExport = (status) => {
  const statusMap = {
    'APROBADA': 'Aprobada',
    'RECHAZADA': 'Rechazada',
    'PEND_LIQ': 'Pendiente Liquidación',
    'LIQUIDADA': 'Liquidada'
  }
  
  return statusMap[status] || status
}

/**
 * Format payment method for export
 * @param {string} method - Payment method
 * @returns {string} Formatted method
 */
export const formatPaymentMethodForExport = (method) => {
  const methodMap = {
    'set_amount': 'Monto Fijo',
    'open_amount': 'Monto Abierto'
  }
  
  return methodMap[method] || method
}

/**
 * Create payment export columns configuration
 * @returns {Array} Column definitions for payment export
 */
export const getPaymentExportColumns = () => {
  return [
    {
      name: 'paymentId',
      label: 'ID Pago',
      field: 'paymentId'
    },
    {
      name: 'orderId',
      label: 'ID Orden',
      field: 'orderId'
    },
    {
      name: 'customerName',
      label: 'Cliente',
      field: 'customerName'
    },
    {
      name: 'branchName',
      label: 'Sucursal',
      field: 'branchName'
    },
    {
      name: 'cashierName',
      label: 'Caja',
      field: 'cashierName'
    },
    {
      name: 'amount',
      label: 'Monto',
      field: 'amount',
      format: formatCurrencyForExport
    },
    {
      name: 'method',
      label: 'Método',
      field: 'method',
      format: formatPaymentMethodForExport
    },
    {
      name: 'status',
      label: 'Estado',
      field: 'status',
      format: formatPaymentStatusForExport
    },
    {
      name: 'settlementEta',
      label: 'ETA Liquidación',
      field: 'settlementEta',
      format: formatDateForExport
    },
    {
      name: 'createdAt',
      label: 'Fecha Creación',
      field: 'createdAt',
      format: formatDateForExport
    },
    {
      name: 'updatedAt',
      label: 'Última Actualización',
      field: 'updatedAt',
      format: formatDateForExport
    }
  ]
}

/**
 * Create customer export columns configuration
 * @returns {Array} Column definitions for customer export
 */
export const getCustomerExportColumns = () => {
  return [
    {
      name: 'customerId',
      label: 'ID Cliente',
      field: 'customerId'
    },
    {
      name: 'name',
      label: 'Nombre',
      field: 'name'
    },
    {
      name: 'phone',
      label: 'Teléfono',
      field: 'phone'
    },
    {
      name: 'email',
      label: 'Email',
      field: 'email'
    },
    {
      name: 'createdAt',
      label: 'Fecha Registro',
      field: 'createdAt',
      format: formatDateForExport
    }
  ]
}

/**
 * Export payments data with proper formatting
 * @param {Array} payments - Payment data to export
 * @param {string} filename - Optional filename
 */
export const exportPayments = (payments, filename) => {
  const columns = getPaymentExportColumns()
  const defaultFilename = `payments_${new Date().toISOString().split('T')[0]}.csv`
  
  exportToCsv(payments, columns, filename || defaultFilename)
}

/**
 * Export customers data with proper formatting
 * @param {Array} customers - Customer data to export
 * @param {string} filename - Optional filename
 */
export const exportCustomers = (customers, filename) => {
  const columns = getCustomerExportColumns()
  const defaultFilename = `customers_${new Date().toISOString().split('T')[0]}.csv`
  
  exportToCsv(customers, columns, filename || defaultFilename)
}
