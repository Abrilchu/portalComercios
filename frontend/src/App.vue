<template>
  <router-view />
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
.q-page {
  padding: 16px;
}

.dashboard-card {
  transition: transform 0.2s ease-in-out;
}

.dashboard-card:hover {
  transform: translateY(-2px);
}

.payment-table {
  max-height: 70vh;
}

.status-chip {
  font-weight: 500;
}

.filter-section {
  background: #f5f5f5;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
}

.virtual-scroll-container {
  height: 60vh;
}
</style>
