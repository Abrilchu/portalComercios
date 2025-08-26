<template>
  <q-page>
    <div class="text-h4 q-mb-lg">Dashboard</div>

    <DashboardCards />

    <div class="q-mt-xl">
      <div class="text-h6 q-mb-md">Ventas por Hora (Hoy)</div>
      <q-card class="chart-container">
        <q-card-section>
          <canvas ref="chartCanvas" width="400" height="200"></canvas>
        </q-card-section>
      </q-card>
    </div>
  </q-page>
</template>

<script>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { useDashboardStore } from '../stores/dashboard'
import DashboardCards from '../components/DashboardCards.vue'

export default {
  name: 'DashboardPage',
  components: {
    DashboardCards
  },
  setup() {
    const dashboardStore = useDashboardStore()
    const chartCanvas = ref(null)
    let chart = null
    let refreshInterval = null

    const createChart = () => {
      if (!chartCanvas.value) return

      const ctx = chartCanvas.value.getContext('2d')
      
      // Sample hourly data (in a real app, this would come from the API)
      const hours = Array.from({ length: 24 }, (_, i) => `${i.toString().padStart(2, '0')}:00`)
      const salesData = Array.from({ length: 24 }, () => Math.floor(Math.random() * 10))

      chart = new Chart(ctx, {
        type: 'line',
        data: {
          labels: hours,
          datasets: [{
            label: 'Ventas',
            data: salesData,
            borderColor: 'rgb(75, 192, 192)',
            backgroundColor: 'rgba(75, 192, 192, 0.1)',
            tension: 0.4
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              display: false
            }
          },
          scales: {
            y: {
              beginAtZero: true,
              ticks: {
                stepSize: 1
              }
            }
          }
        }
      })
    }

    const refreshData = () => {
      dashboardStore.fetchDashboardStats()
    }

    onMounted(async () => {
      await dashboardStore.fetchDashboardStats()
      
      // Wait for next tick to ensure canvas is rendered
      setTimeout(createChart, 100)
      
      // Set up auto-refresh every 30 seconds
      refreshInterval = setInterval(refreshData, 30000)
    })

    onBeforeUnmount(() => {
      if (chart) {
        chart.destroy()
      }
      if (refreshInterval) {
        clearInterval(refreshInterval)
      }
    })

    return {
      dashboardStore,
      chartCanvas
    }
  }
}
</script>

<style scoped>
.chart-container {
  height: 300px;
}

.chart-container .q-card-section {
  height: 100%;
  position: relative;
}

canvas {
  max-height: 100%;
}
</style>
