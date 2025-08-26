/**
 * Authentication utility functions
 */

export const AUTH_TOKEN_KEY = 'auth_token'
export const USER_DATA_KEY = 'user_data'

/**
 * Get the authentication token from localStorage
 * @returns {string|null} The auth token or null if not found
 */
export const getAuthToken = () => {
  return localStorage.getItem(AUTH_TOKEN_KEY)
}

/**
 * Set the authentication token in localStorage
 * @param {string} token - The JWT token to store
 */
export const setAuthToken = (token) => {
  if (token) {
    localStorage.setItem(AUTH_TOKEN_KEY, token)
  }
}

/**
 * Remove the authentication token from localStorage
 */
export const removeAuthToken = () => {
  localStorage.removeItem(AUTH_TOKEN_KEY)
}

/**
 * Get user data from localStorage
 * @returns {object|null} The user data object or null if not found
 */
export const getUserData = () => {
  const userData = localStorage.getItem(USER_DATA_KEY)
  if (userData) {
    try {
      return JSON.parse(userData)
    } catch (error) {
      console.error('Error parsing user data:', error)
      return null
    }
  }
  return null
}

/**
 * Set user data in localStorage
 * @param {object} userData - The user data object to store
 */
export const setUserData = (userData) => {
  if (userData) {
    localStorage.setItem(USER_DATA_KEY, JSON.stringify(userData))
  }
}

/**
 * Remove user data from localStorage
 */
export const removeUserData = () => {
  localStorage.removeItem(USER_DATA_KEY)
}

/**
 * Check if the user is authenticated
 * @returns {boolean} True if user is authenticated, false otherwise
 */
export const isAuthenticated = () => {
  const token = getAuthToken()
  const userData = getUserData()
  return !!(token && userData)
}

/**
 * Clear all authentication data
 */
export const clearAuth = () => {
  removeAuthToken()
  removeUserData()
}

/**
 * Get user role from stored user data
 * @returns {string|null} The user role or null if not found
 */
export const getUserRole = () => {
  const userData = getUserData()
  return userData?.role || null
}

/**
 * Check if user has a specific role
 * @param {string} role - The role to check
 * @returns {boolean} True if user has the role, false otherwise
 */
export const hasRole = (role) => {
  const userRole = getUserRole()
  return userRole === role
}

/**
 * Check if user is a cashier
 * @returns {boolean} True if user is a cashier, false otherwise
 */
export const isCashier = () => {
  return hasRole('cashier')
}

/**
 * Check if user is a manager
 * @returns {boolean} True if user is a manager, false otherwise
 */
export const isManager = () => {
  return hasRole('manager')
}

/**
 * Check if user is an owner
 * @returns {boolean} True if user is an owner, false otherwise
 */
export const isOwner = () => {
  return hasRole('owner')
}

/**
 * Get user's accessible branch IDs
 * @returns {Array<number>} Array of branch IDs the user can access
 */
export const getUserBranchIds = () => {
  const userData = getUserData()
  return userData?.branchIds || []
}

/**
 * Get user's accessible cashier IDs
 * @returns {Array<number>} Array of cashier IDs the user can access
 */
export const getUserCashierIds = () => {
  const userData = getUserData()
  return userData?.cashierIds || []
}

/**
 * Get user's commerce ID
 * @returns {number|null} The commerce ID or null if not found
 */
export const getCommerceId = () => {
  const userData = getUserData()
  return userData?.commerceId || null
}

/**
 * Get user's username
 * @returns {string|null} The username or null if not found
 */
export const getUsername = () => {
  const userData = getUserData()
  return userData?.username || null
}

/**
 * Check if user can access a specific branch
 * @param {number} branchId - The branch ID to check
 * @returns {boolean} True if user can access the branch, false otherwise
 */
export const canAccessBranch = (branchId) => {
  const accessibleBranches = getUserBranchIds()
  return accessibleBranches.includes(branchId)
}

/**
 * Check if user can access a specific cashier
 * @param {number} cashierId - The cashier ID to check
 * @returns {boolean} True if user can access the cashier, false otherwise
 */
export const canAccessCashier = (cashierId) => {
  const accessibleCashiers = getUserCashierIds()
  return accessibleCashiers.includes(cashierId)
}

/**
 * Decode JWT token payload (without verification)
 * @param {string} token - The JWT token to decode
 * @returns {object|null} The decoded payload or null if invalid
 */
export const decodeJwtPayload = (token) => {
  if (!token) return null
  
  try {
    const parts = token.split('.')
    if (parts.length !== 3) return null
    
    const payload = parts[1]
    const decoded = atob(payload.replace(/-/g, '+').replace(/_/g, '/'))
    return JSON.parse(decoded)
  } catch (error) {
    console.error('Error decoding JWT payload:', error)
    return null
  }
}

/**
 * Check if JWT token is expired
 * @param {string} token - The JWT token to check
 * @returns {boolean} True if token is expired, false otherwise
 */
export const isTokenExpired = (token) => {
  const payload = decodeJwtPayload(token)
  if (!payload || !payload.exp) return true
  
  const currentTime = Math.floor(Date.now() / 1000)
  return payload.exp < currentTime
}

/**
 * Check if current auth token is expired
 * @returns {boolean} True if current token is expired, false otherwise
 */
export const isCurrentTokenExpired = () => {
  const token = getAuthToken()
  return isTokenExpired(token)
}

/**
 * Format user role for display
 * @param {string} role - The role to format
 * @returns {string} The formatted role name
 */
export const formatRole = (role) => {
  const roleMap = {
    'cashier': 'Cajero',
    'manager': 'Gerente',
    'owner': 'Propietario'
  }
  return roleMap[role] || role
}
