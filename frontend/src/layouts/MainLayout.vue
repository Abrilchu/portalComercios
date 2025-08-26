<template>
  <q-layout view="lHh Lpr lFf">
    <q-header elevated>
      <q-toolbar>
        <q-btn
          flat
          dense
          round
          icon="menu"
          aria-label="Menu"
          @click="toggleLeftDrawer"
        />

        <q-toolbar-title>
          UX Dual - Portal de Comercios
        </q-toolbar-title>

        <q-chip 
          :color="roleColor" 
          text-color="white" 
          :label="roleLabel"
          class="q-mr-md"
        />

        <q-btn
          flat
          dense
          round
          icon="logout"
          @click="logout"
        >
          <q-tooltip>Cerrar Sesión</q-tooltip>
        </q-btn>
      </q-toolbar>
    </q-header>

    <q-drawer
      v-model="leftDrawerOpen"
      show-if-above
      bordered
    >
      <q-list>
        <q-item-label header>
          Navegación
        </q-item-label>

        <q-item
          v-for="link in menuLinks"
          :key="link.title"
          :to="link.to"
          clickable
          v-ripple
          exact-active-class="bg-primary text-white"
        >
          <q-item-section avatar>
            <q-icon :name="link.icon" />
          </q-item-section>

          <q-item-section>
            <q-item-label>{{ link.title }}</q-item-label>
          </q-item-section>
        </q-item>
      </q-list>
    </q-drawer>

    <q-page-container>
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

export default {
  name: 'MainLayout',
  setup() {
    const router = useRouter()
    const authStore = useAuthStore()
    const leftDrawerOpen = ref(false)

    const menuLinks = [
      {
        title: 'Dashboard',
        icon: 'dashboard',
        to: '/dashboard'
      },
      {
        title: 'Pagos',
        icon: 'payment',
        to: '/payments'
      },
      {
        title: 'Clientes',
        icon: 'people',
        to: '/customers'
      }
    ]

    const roleColor = computed(() => {
      switch (authStore.userRole) {
        case 'owner': return 'purple'
        case 'manager': return 'blue'
        case 'cashier': return 'green'
        default: return 'grey'
      }
    })

    const roleLabel = computed(() => {
      switch (authStore.userRole) {
        case 'owner': return 'Propietario'
        case 'manager': return 'Gerente'
        case 'cashier': return 'Cajero'
        default: return 'Usuario'
      }
    })

    const toggleLeftDrawer = () => {
      leftDrawerOpen.value = !leftDrawerOpen.value
    }

    const logout = () => {
      authStore.logout()
      router.push('/login')
    }

    onMounted(() => {
      authStore.initializeAuth()
    })

    return {
      leftDrawerOpen,
      menuLinks,
      roleColor,
      roleLabel,
      toggleLeftDrawer,
      logout
    }
  }
}
</script>
