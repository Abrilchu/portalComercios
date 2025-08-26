<template>
  <q-page class="flex flex-center bg-primary">
    <q-card class="login-card">
      <q-card-section class="text-center">
        <div class="text-h4 text-primary q-mb-md">UX Dual</div>
        <div class="text-subtitle1 text-grey-7">Portal de Comercios</div>
      </q-card-section>

      <q-card-section>
        <q-form @submit="onSubmit" class="q-gutter-md">
          <q-input
            v-model="form.username"
            label="Usuario"
            outlined
            :rules="[val => !!val || 'Usuario es requerido']"
            autocomplete="username"
          >
            <template v-slot:prepend>
              <q-icon name="person" />
            </template>
          </q-input>

          <q-input
            v-model="form.password"
            label="Contraseña"
            type="password"
            outlined
            :rules="[val => !!val || 'Contraseña es requerida']"
            autocomplete="current-password"
          >
            <template v-slot:prepend>
              <q-icon name="lock" />
            </template>
          </q-input>

          <div class="q-mt-lg">
            <q-btn
              label="Iniciar Sesión"
              type="submit"
              color="primary"
              class="full-width"
              :loading="authStore.loading"
              :disable="authStore.loading"
            />
          </div>
        </q-form>
      </q-card-section>

      <q-card-section class="text-center text-caption text-grey-7">
        <div class="q-mb-sm">Usuarios de prueba:</div>
        <div><strong>owner1</strong> / password123 (Propietario)</div>
        <div><strong>manager1</strong> / password123 (Gerente)</div>
        <div><strong>cashier1</strong> / password123 (Cajero)</div>
      </q-card-section>
    </q-card>
  </q-page>
</template>

<script>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

export default {
  name: 'LoginPage',
  setup() {
    const router = useRouter()
    const authStore = useAuthStore()

    const form = ref({
      username: '',
      password: ''
    })

    const onSubmit = async () => {
      try {
        await authStore.login(form.value)
        router.push('/dashboard')
      } catch (error) {
        // Error handling is done in the store
        console.error('Login error:', error)
      }
    }

    return {
      form,
      authStore,
      onSubmit
    }
  }
}
</script>

<style scoped>
.login-card {
  width: 100%;
  max-width: 400px;
  padding: 20px;
}
</style>
