<template>
  <q-page>
    <div class="row items-center q-mb-lg">
      <div class="text-h4 col">Pagos</div>
      <div class="col-auto">
        <q-btn
          color="primary"
          icon="download"
          label="Exportar CSV"
          @click="exportData"
          :loading="exporting"
        />
      </div>
    </div>

    <PaymentFilters @apply-filters="applyFilters" @clear-filters="clearFilters" />

    <PaymentTable />

    <PaymentDetailModal />
  </q-page>
</template>

<script>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { usePaymentsStore } from '../stores/payments'
import PaymentFilters from '../components/PaymentFilters.vue'
import PaymentTable from '../components/PaymentTable.vue'
import PaymentDetailModal from '../components/PaymentDetailModal.vue'

export default {
  name: 'PaymentsPage',
  components: {
    PaymentFilters,
    PaymentTable,
    PaymentDetailModal
  },
  setup() {
    const paymentsStore = usePaymentsStore()
    const exporting = ref(false)

    const applyFilters = (filters) => {
      paymentsStore.setFilters(filters)
      paymentsStore.fetchPayments(0, true)
    }

    const clearFilters = () => {
      paymentsStore.clearFilters()
      paymentsStore.fetchPayments(0, true)
    }

    const exportData = async () => {
      try {
        exporting.value = true
        await paymentsStore.exportPayments('csv')
      } finally {
        exporting.value = false
      }
    }

    onMounted(() => {
      paymentsStore.fetchPayments()
      paymentsStore.startPolling()
    })

    onBeforeUnmount(() => {
      paymentsStore.stopPolling()
    })

    return {
      paymentsStore,
      exporting,
      applyFilters,
      clearFilters,
      exportData
    }
  }
}
</script>
