<template>
  <q-dialog 
    v-model="showModal" 
    persistent
    maximized
    transition-show="slide-up"
    transition-hide="slide-down"
  >
    <q-card class="column">
      <q-bar class="bg-primary text-white">
        <q-space />
        <div class="text-weight-bold">Detalle del Pago</div>
        <q-space />
        <q-btn dense flat icon="close" @click="closeModal">
          <q-tooltip>Cerrar</q-tooltip>
        </q-btn>
      </q-bar>

      <q-card-section class="col q-pa-md">
        <div v-if="loading" class="row justify-center q-pa-xl">
          <q-spinner-dots size="3em" color="primary" />
        </div>

        <div v-else-if="paymentDetail" class="row q-gutter-lg">
          <!-- Payment Information -->
          <div class="col-md-6 col-xs-12">
            <q-card flat bordered>
              <q-card-section>
                <div class="text-h6 q-mb-md">
                  <q-icon name="payment" class="q-mr-sm" />
                  Información del Pago
                </div>

                <q-list>
                  <q-item>
                    <q-item-section>
                      <q-item-label caption>ID Pago</q-item-label>
                      <q-item-label class="text-weight-medium">
                        {{ paymentDetail.paymentId }}
                      </q-item-label>
                    </q-item-section>
                  </q-item>

                  <q-item>
                    <q-item-section>
                      <q-item-label caption>ID Orden</q-item-label>
                      <q-item-label class="text-weight-medium">
                        {{ paymentDetail.orderId }}
                      </q-item-label>
                    </q-item-section>
                  </q-item>

                  <q-item>
                    <q-item-section>
                      <q-item-label caption>Monto</q-item-label>
                      <q-item-label class="text-weight-bold text-h6">
                        {{ formatCurrency(paymentDetail.amount) }}
                      </q-item-label>
                    </q-item-section>
                  </q-item>

                  <q-item>
                    <q-item-section>
                      <q-item-label caption>Método</q-item-label>
                      <q-item-label>
                        <q-chip
                          :color="paymentDetail.method === 'set_amount' ? 'blue' : 'orange'"
                          text-color="white"
                          :label="paymentDetail.method === 'set_amount' ? 'Monto Fijo' : 'Monto Abierto'"
                        />
                      </q-item-label>
                    </q-item-section>
                  </q-item>

                  <q-item>
                    <q-item-section>
                      <q-item-label caption>Estado</q-item-label>
                      <q-item-label>
                        <q-chip
                          :color="getStatusColor(paymentDetail.status)"
                          text-color="white"
                          :label="getStatusLabel(paymentDetail.status)"
                        />
                      </q-item-label>
                    </q-item-section>
                  </q-item>

                  <q-item v-if="paymentDetail.settlementEta">
                    <q-item-section>
                      <q-item-label caption>ETA de Liquidación</q-item-label>
                      <q-item-label class="text-weight-medium">
                        {{ formatDate(paymentDetail.settlementEta) }}
                      </q-item-label>
                    </q-item-section>
                  </q-item>
                </q-list>
              </q-card-section>
            </q-card>
          </div>

          <!-- Business Information -->
          <div class="col-md-6 col-xs-12">
            <q-card flat bordered>
              <q-card-section>
                <div class="text-h6 q-mb-md">
                  <q-icon name="business" class="q-mr-sm" />
                  Información del Comercio
                </div>

                <q-list>
                  <q-item v-if="paymentDetail.customerName">
                    <q-item-section>
                      <q-item-label caption>Cliente</q-item-label>
                      <q-item-label class="text-weight-medium">
                        {{ paymentDetail.customerName }}
                      </q-item-label>
                    </q-item-section>
                  </q-item>

                  <q-item v-if="paymentDetail.branchName">
                    <q-item-section>
                      <q-item-label caption>Sucursal</q-item-label>
                      <q-item-label class="text-weight-medium">
                        {{ paymentDetail.branchName }}
                      </q-item-label>
                    </q-item-section>
                  </q-item>

                  <q-item v-if="paymentDetail.cashierName">
                    <q-item-section>
                      <q-item-label caption>Caja</q-item-label>
                      <q-item-label class="text-weight-medium">
                        {{ paymentDetail.cashierName }}
                      </q-item-label>
                    </q-item-section>
                  </q-item>

                  <q-item>
                    <q-item-section>
                      <q-item-label caption>Fecha Creación</q-item-label>
                      <q-item-label class="text-weight-medium">
                        {{ formatDate(paymentDetail.createdAt) }}
                      </q-item-label>
                    </q-item-section>
                  </q-item>

                  <q-item v-if="paymentDetail.updatedAt !== paymentDetail.createdAt">
                    <q-item-section>
                      <q-item-label caption>Última Actualización</q-item-label>
                      <q-item-label class="text-weight-medium">
                        {{ formatDate(paymentDetail.updatedAt) }}
                      </q-item-label>
                    </q-item-section>
                  </q-item>
                </q-list>
              </q-card-section>
            </q-card>
          </div>

          <!-- Timeline -->
          <div class="col-12" v-if="timeline.length > 0">
            <q-card flat bordered>
              <q-card-section>
                <div class="text-h6 q-mb-md">
                  <q-icon name="timeline" class="q-mr-sm" />
                  Timeline de Estados
                </div>

                <q-timeline :layout="$q.screen.xs ? 'dense' : 'comfortable'">
                  <q-timeline-entry
                    v-for="(event, index) in timeline"
                    :key="index"
                    :title="event.title"
                    :subtitle="formatDate(event.timestamp)"
                    :color="event.color"
                    :icon="event.icon"
                  />
                </q-timeline>
              </q-card-section>
            </q-card>
          </div>
        </div>
      </q-card-section>

      <q-card-actions align="right" class="bg-grey-1">
        <q-btn
          flat
          label="Cerrar"
          color="grey-7"
          @click="closeModal"
        />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import { usePaymentsStore } from '../stores/payments'

export default {
  name: 'PaymentDetailModal',
  setup() {
    const paymentsStore = usePaymentsStore()
    const showModal = ref(false)
    const paymentDetail = ref(null)
    const loading = ref(false)

    const timeline = computed(() => {
      if (!paymentDetail.value?.statusTimeline) return []

      try {
        const timelineData = JSON.parse(paymentDetail.value.statusTimeline)
        const events = []

        if (timelineData.created) {
          events.push({
            title: 'Pago Creado',
            timestamp: timelineData.created,
            color: 'blue',
            icon: 'add_circle'
          })
        }

        if (timelineData.approved) {
          events.push({
            title: 'Pago Aprobado',
            timestamp: timelineData.approved,
            color: 'green',
            icon: 'check_circle'
          })
        }

        if (timelineData.rejected) {
          events.push({
            title: 'Pago Rechazado',
            timestamp: timelineData.rejected,
            color: 'red',
            icon: 'cancel'
          })
        }

        if (timelineData.settled) {
          events.push({
            title: 'Pago Liquidado',
            timestamp: timelineData.settled,
            color: 'purple',
            icon: 'account_balance'
          })
        }

        // Sort by timestamp
        return events.sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp))
      } catch (error) {
        console.error('Error parsing status timeline:', error)
        return []
      }
    })

    const showPaymentDetail = async (event) => {
      const payment = event.detail
      if (!payment) return

      try {
        loading.value = true
        showModal.value = true
        
        // Fetch detailed payment information
        paymentDetail.value = await paymentsStore.getPaymentDetail(payment.id)
      } catch (error) {
        console.error('Error loading payment detail:', error)
        closeModal()
      } finally {
        loading.value = false
      }
    }

    const closeModal = () => {
      showModal.value = false
      paymentDetail.value = null
    }

    const getStatusColor = (status) => {
      return paymentsStore.getStatusColor(status)
    }

    const getStatusLabel = (status) => {
      return paymentsStore.getStatusLabel(status)
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
        minute: '2-digit',
        second: '2-digit'
      })
    }

    onMounted(() => {
      document.addEventListener('show-payment-detail', showPaymentDetail)
    })

    onBeforeUnmount(() => {
      document.removeEventListener('show-payment-detail', showPaymentDetail)
    })

    return {
      showModal,
      paymentDetail,
      loading,
      timeline,
      closeModal,
      getStatusColor,
      getStatusLabel,
      formatCurrency,
      formatDate
    }
  }
}
</script>

<style scoped>
.q-timeline__entry .q-timeline__subtitle {
  opacity: 0.8;
}
</style>
