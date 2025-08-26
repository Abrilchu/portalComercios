<template>
  <q-card class="filter-section q-mb-md">
    <q-card-section>
      <div class="text-h6 q-mb-md">Filtros</div>
      
      <div class="row q-gutter-md">
        <div class="col-md-2 col-sm-4 col-xs-6">
          <q-input
            v-model="filters.fromDate"
            label="Fecha Desde"
            type="datetime-local"
            outlined
            dense
          />
        </div>

        <div class="col-md-2 col-sm-4 col-xs-6">
          <q-input
            v-model="filters.toDate"
            label="Fecha Hasta"
            type="datetime-local"
            outlined
            dense
          />
        </div>

        <div class="col-md-2 col-sm-4 col-xs-6">
          <q-select
            v-model="filters.status"
            :options="statusOptions"
            option-value="value"
            option-label="label"
            label="Estado"
            outlined
            dense
            clearable
            emit-value
            map-options
          />
        </div>

        <div class="col-md-2 col-sm-4 col-xs-6" v-if="!authStore.isCashier">
          <q-select
            v-model="filters.branchIds"
            :options="branchOptions"
            option-value="id"
            option-label="name"
            label="Sucursales"
            outlined
            dense
            clearable
            multiple
            emit-value
            map-options
            use-chips
          />
        </div>

        <div class="col-md-2 col-sm-4 col-xs-6" v-if="showCashierFilter">
          <q-select
            v-model="filters.cashierIds"
            :options="cashierOptions"
            option-value="id"
            option-label="name"
            label="Cajas"
            outlined
            dense
            clearable
            multiple
            emit-value
            map-options
            use-chips
          />
        </div>

        <div class="col-md-2 col-sm-4 col-xs-6">
          <q-input
            v-model="filters.minAmount"
            label="Monto Mínimo"
            type="number"
            outlined
            dense
            step="0.01"
            min="0"
          />
        </div>

        <div class="col-md-2 col-sm-4 col-xs-6">
          <q-input
            v-model="filters.maxAmount"
            label="Monto Máximo"
            type="number"
            outlined
            dense
            step="0.01"
            min="0"
          />
        </div>

        <div class="col-md-2 col-sm-4 col-xs-6">
          <q-input
            v-model="filters.paymentId"
            label="ID Pago"
            outlined
            dense
            clearable
          />
        </div>

        <div class="col-md-2 col-sm-4 col-xs-6">
          <q-input
            v-model="filters.orderId"
            label="ID Orden"
            outlined
            dense
            clearable
          />
        </div>
      </div>

      <div class="row q-mt-md q-gutter-sm">
        <q-btn
          color="primary"
          label="Aplicar Filtros"
          icon="search"
          @click="applyFilters"
        />
        <q-btn
          color="grey-7"
          label="Limpiar"
          icon="clear"
          outline
          @click="clearFilters"
        />
        <q-space />
        <q-btn
          color="secondary"
          :label="autoRefresh ? 'Pausar Auto-refresco' : 'Activar Auto-refresco'"
          :icon="autoRefresh ? 'pause' : 'play_arrow'"
          @click="toggleAutoRefresh"
        />
      </div>
    </q-card-section>
  </q-card>
</template>

<script>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import { useAuthStore } from '../stores/auth'
import { usePaymentsStore } from '../stores/payments'
import { api } from '../services/api'

export default {
  name: 'PaymentFilters',
  emits: ['apply-filters', 'clear-filters'],
  setup(props, { emit }) {
    const authStore = useAuthStore()
    const paymentsStore = usePaymentsStore()
    
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

    const branchOptions = ref([])
    const cashierOptions = ref([])
    const autoRefresh = ref(true)

    const statusOptions = computed(() => paymentsStore.statusOptions)

    const showCashierFilter = computed(() => {
      return !authStore.isCashier && (authStore.isManager || authStore.isOwner)
    })

    const loadBranches = async () => {
      if (authStore.isCashier) return
      
      try {
        // In a real implementation, you would have a branches endpoint
        // For now, we'll use the user's available branch IDs
        const userBranchIds = authStore.getUserBranchIds
        
        // Mock branch data based on user's accessible branches
        branchOptions.value = userBranchIds.map(id => ({
          id,
          name: `Sucursal ${id}`
        }))
      } catch (error) {
        console.error('Error loading branches:', error)
      }
    }

    const loadCashiers = async () => {
      if (authStore.isCashier) return
      
      try {
        // In a real implementation, you would have a cashiers endpoint
        const userCashierIds = authStore.getUserCashierIds
        
        // Mock cashier data based on user's accessible cashiers
        cashierOptions.value = userCashierIds.map(id => ({
          id,
          name: `Caja ${id}`
        }))
      } catch (error) {
        console.error('Error loading cashiers:', error)
      }
    }

    const applyFilters = () => {
      const cleanFilters = { ...filters.value }
      
      // Convert date strings to ISO format
      if (cleanFilters.fromDate) {
        cleanFilters.fromDate = new Date(cleanFilters.fromDate).toISOString()
      }
      if (cleanFilters.toDate) {
        cleanFilters.toDate = new Date(cleanFilters.toDate).toISOString()
      }

      // Convert string numbers to numbers
      if (cleanFilters.minAmount) {
        cleanFilters.minAmount = parseFloat(cleanFilters.minAmount)
      }
      if (cleanFilters.maxAmount) {
        cleanFilters.maxAmount = parseFloat(cleanFilters.maxAmount)
      }

      emit('apply-filters', cleanFilters)
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
      emit('clear-filters')
    }

    const toggleAutoRefresh = () => {
      autoRefresh.value = !autoRefresh.value
      if (autoRefresh.value) {
        paymentsStore.startPolling()
      } else {
        paymentsStore.stopPolling()
      }
    }

    onMounted(() => {
      loadBranches()
      loadCashiers()
    })

    return {
      authStore,
      filters,
      statusOptions,
      branchOptions,
      cashierOptions,
      showCashierFilter,
      autoRefresh,
      applyFilters,
      clearFilters,
      toggleAutoRefresh
    }
  }
}
</script>
