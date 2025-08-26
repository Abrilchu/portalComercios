<template>
  <div class="row q-gutter-md">
    <div class="col-md col-sm-6 col-xs-12">
      <q-card class="dashboard-card bg-blue-1">
        <q-card-section>
          <div class="row items-center">
            <div class="col">
              <div class="text-h4 text-primary">{{ dashboardStore.formatNumber(dashboardStore.stats.salesToday) }}</div>
              <div class="text-subtitle1 text-grey-7">Ventas Hoy</div>
            </div>
            <div class="col-auto">
              <q-icon name="shopping_cart" size="3em" color="primary" />
            </div>
          </div>
        </q-card-section>
      </q-card>
    </div>

    <div class="col-md col-sm-6 col-xs-12">
      <q-card class="dashboard-card bg-green-1">
        <q-card-section>
          <div class="row items-center">
            <div class="col">
              <div class="text-h4 text-positive">{{ dashboardStore.formatNumber(dashboardStore.stats.approvedToday) }}</div>
              <div class="text-subtitle1 text-grey-7">Aprobadas Hoy</div>
            </div>
            <div class="col-auto">
              <q-icon name="check_circle" size="3em" color="positive" />
            </div>
          </div>
        </q-card-section>
      </q-card>
    </div>

    <div class="col-md col-sm-6 col-xs-12">
      <q-card class="dashboard-card bg-orange-1">
        <q-card-section>
          <div class="row items-center">
            <div class="col">
              <div class="text-h4 text-warning">{{ dashboardStore.formatNumber(dashboardStore.stats.pendingSettlement) }}</div>
              <div class="text-subtitle1 text-grey-7">Pendientes de Liquidar</div>
            </div>
            <div class="col-auto">
              <q-icon name="schedule" size="3em" color="warning" />
            </div>
          </div>
        </q-card-section>
      </q-card>
    </div>

    <div class="col-md col-sm-6 col-xs-12">
      <q-card class="dashboard-card bg-purple-1">
        <q-card-section>
          <div class="row items-center">
            <div class="col">
              <div class="text-h4 text-purple">{{ dashboardStore.formatCurrency(dashboardStore.stats.settlementMonth) }}</div>
              <div class="text-subtitle1 text-grey-7">Liquidado Este Mes</div>
            </div>
            <div class="col-auto">
              <q-icon name="account_balance" size="3em" color="purple" />
            </div>
          </div>
        </q-card-section>
      </q-card>
    </div>
  </div>
</template>

<script>
import { useDashboardStore } from '../stores/dashboard'

export default {
  name: 'DashboardCards',
  setup() {
    const dashboardStore = useDashboardStore()

    return {
      dashboardStore
    }
  }
}
</script>
