<template>
  <q-card>
    <q-card-section class="q-pa-none">
      <q-table
        :rows="paymentsStore.payments"
        :columns="columns"
        :loading="paymentsStore.loading"
        :pagination="pagination"
        @request="onRequest"
        row-key="id"
        class="payment-table"
        virtual-scroll
        :virtual-scroll-item-size="48"
        :virtual-scroll-sticky-size-start="48"
        :rows-per-page-options="[20, 50, 100]"
      >
        <template v-slot:body-cell-status="props">
          <q-td :props="props">
            <q-chip
              :color="paymentsStore.getStatusColor(props.value)"
              text-color="white"
              :label="paymentsStore.getStatusLabel(props.value)"
              size="sm"
              class="status-chip"
            />
          </q-td>
        </template>

        <template v-slot:body-cell-amount="props">
          <q-td :props="props">
            <span class="text-weight-medium">
              {{ formatCurrency(props.value) }}
            </span>
          </q-td>
        </template>

        <template v-slot:body-cell-method="props">
          <q-td :props="props">
            <q-chip
              :color="props.value === 'set_amount' ? 'blue' : 'orange'"
              text-color="white"
              :label="props.value === 'set_amount' ? 'Monto Fijo' : 'Monto Abierto'"
              size="sm"
              outline
            />
          </q-td>
        </template>

        <template v-slot:body-cell-createdAt="props">
          <q-td :props="props">
            {{ formatDate(props.value) }}
          </q-td>
        </template>

        <template v-slot:body-cell-actions="props">
          <q-td :props="props">
            <q-btn
              flat
              round
              dense
              icon="visibility"
              @click="showPaymentDetail(props.row)"
            >
              <q-tooltip>Ver Detalle</q-tooltip>
            </q-btn>
          </q-td>
        </template>

        <template v-slot:no-data>
          <div class="full-width row flex-center text-accent q-gutter-sm">
            <q-icon size="2em" name="payment" />
            <span>No se encontraron pagos</span>
          </div>
        </template>

        <template v-slot:loading>
          <q-inner-loading showing color="primary" />
        </template>

        <template v-slot:top-right>
          <div class="row q-gutter-sm items-center">
            <q-chip
              v-if="paymentsStore.payments.length > 0"
              color="info"
              text-color="white"
              :label="`${paymentsStore.payments.length} pagos`"
            />
            <q-btn
              flat
              round
              dense
              icon="refresh"
              @click="refreshData"
              :loading="paymentsStore.loading"
            >
              <q-tooltip>Actualizar</q-tooltip>
            </q-btn>
          </div>
        </template>
      </q-table>
    </q-card-section>
  </q-card>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { usePaymentsStore } from '../stores/payments'

export default {
  name: 'PaymentTable',
  setup() {
    const paymentsStore = usePaymentsStore()
    const selectedPayment = ref(null)

    const columns = [
      {
        name: 'paymentId',
        required: true,
        label: 'ID Pago',
        align: 'left',
        field: 'paymentId',
        sortable: true,
        style: 'width: 120px'
      },
      {
        name: 'orderId',
        label: 'ID Orden',
        align: 'left',
        field: 'orderId',
        sortable: true,
        style: 'width: 120px'
      },
      {
        name: 'customerName',
        label: 'Cliente',
        align: 'left',
        field: 'customerName',
        sortable: true
      },
      {
        name: 'branchName',
        label: 'Sucursal',
        align: 'left',
        field: 'branchName',
        sortable: true
      },
      {
        name: 'cashierName',
        label: 'Caja',
        align: 'left',
        field: 'cashierName',
        sortable: true
      },
      {
        name: 'amount',
        label: 'Monto',
        align: 'right',
        field: 'amount',
        sortable: true,
        style: 'width: 120px'
      },
      {
        name: 'method',
        label: 'Método',
        align: 'center',
        field: 'method',
        style: 'width: 140px'
      },
      {
        name: 'status',
        label: 'Estado',
        align: 'center',
        field: 'status',
        sortable: true,
        style: 'width: 140px'
      },
      {
        name: 'createdAt',
        label: 'Fecha',
        align: 'left',
        field: 'createdAt',
        sortable: true,
        style: 'width: 140px'
      },
      {
        name: 'actions',
        label: 'Acciones',
        align: 'center',
        style: 'width: 80px'
      }
    ]

    const pagination = computed(() => ({
      sortBy: 'createdAt',
      descending: true,
      page: paymentsStore.pagination.page + 1,
      rowsPerPage: paymentsStore.pagination.size,
      rowsNumber: paymentsStore.pagination.totalElements
    }))

    const onRequest = (props) => {
      const { page, rowsPerPage } = props.pagination
      paymentsStore.pagination.page = page - 1
      paymentsStore.pagination.size = rowsPerPage
      paymentsStore.fetchPayments(page - 1, true)
    }

    const showPaymentDetail = (payment) => {
      selectedPayment.value = payment
      // Emit event to show modal - the modal will listen for this
      document.dispatchEvent(new CustomEvent('show-payment-detail', {
        detail: payment
      }))
    }

    const refreshData = () => {
      paymentsStore.fetchPayments(paymentsStore.pagination.page, true)
    }

    const formatCurrency = (amount) => {
      if (!amount) return '$0'
      return new Intl.NumberFormat('es-AR', {
        style: 'currency',
        currency: 'ARS'
      }).format(amount)
    }

    const formatDate = (dateString) => {
      if (!dateString) return ''
      return new Date(dateString).toLocaleDateString('es-AR', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      })
    }

    return {
      paymentsStore,
      columns,
      pagination,
      onRequest,
      showPaymentDetail,
      refreshData,
      formatCurrency,
      formatDate
    }
  }
}
</script>

<style scoped>
.payment-table {
  height: 70vh;
}

.status-chip {
  min-width: 100px;
}

.q-table__top {
  padding: 12px 16px;
  border-bottom: 1px solid #e0e0e0;
}
</style>
