<template>
  <q-page>
    <div class="text-h4 q-mb-lg">Clientes</div>

    <div class="row q-mb-md">
      <div class="col-md-4 col-sm-6 col-xs-12">
        <q-input
          v-model="searchTerm"
          label="Buscar por nombre, teléfono o ID"
          outlined
          clearable
          @keyup.enter="searchCustomers"
        >
          <template v-slot:append>
            <q-btn
              flat
              round
              dense
              icon="search"
              @click="searchCustomers"
            />
          </template>
        </q-input>
      </div>
    </div>

    <q-table
      :rows="customers"
      :columns="columns"
      :loading="loading"
      :pagination="pagination"
      @request="onRequest"
      row-key="id"
      class="customer-table"
    >
      <template v-slot:body-cell-createdAt="props">
        <q-td :props="props">
          {{ formatDate(props.value) }}
        </q-td>
      </template>

      <template v-slot:no-data>
        <div class="full-width row flex-center text-accent q-gutter-sm">
          <q-icon size="2em" name="sentiment_dissatisfied" />
          <span>No se encontraron clientes</span>
        </div>
      </template>
    </q-table>
  </q-page>
</template>

<script>
import { ref, onMounted } from 'vue'
import { api } from '../services/api'
import { Notify } from 'quasar'

export default {
  name: 'CustomersPage',
  setup() {
    const customers = ref([])
    const loading = ref(false)
    const searchTerm = ref('')
    
    const pagination = ref({
      sortBy: 'id',
      descending: false,
      page: 1,
      rowsPerPage: 20,
      rowsNumber: 0
    })

    const columns = [
      {
        name: 'customerId',
        required: true,
        label: 'ID Cliente',
        align: 'left',
        field: 'customerId',
        sortable: true
      },
      {
        name: 'name',
        required: true,
        label: 'Nombre',
        align: 'left',
        field: 'name',
        sortable: true
      },
      {
        name: 'phone',
        label: 'Teléfono',
        align: 'left',
        field: 'phone'
      },
      {
        name: 'email',
        label: 'Email',
        align: 'left',
        field: 'email'
      },
      {
        name: 'createdAt',
        label: 'Fecha Registro',
        align: 'left',
        field: 'createdAt',
        sortable: true
      }
    ]

    const fetchCustomers = async (page = 0, search = null) => {
      try {
        loading.value = true
        
        const params = {
          page,
          size: pagination.value.rowsPerPage
        }

        if (search && search.trim()) {
          params.search = search.trim()
        }

        const response = await api.get('/customers', { params })
        
        customers.value = response.data.content
        pagination.value.rowsNumber = response.data.totalElements
        
      } catch (error) {
        console.error('Error fetching customers:', error)
        Notify.create({
          type: 'negative',
          message: 'Error al cargar los clientes'
        })
      } finally {
        loading.value = false
      }
    }

    const onRequest = (props) => {
      const { page, rowsPerPage } = props.pagination
      pagination.value.page = page
      pagination.value.rowsPerPage = rowsPerPage
      
      fetchCustomers(page - 1, searchTerm.value)
    }

    const searchCustomers = () => {
      pagination.value.page = 1
      fetchCustomers(0, searchTerm.value)
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

    onMounted(() => {
      fetchCustomers()
    })

    return {
      customers,
      loading,
      searchTerm,
      pagination,
      columns,
      onRequest,
      searchCustomers,
      formatDate
    }
  }
}
</script>

<style scoped>
.customer-table {
  max-height: 70vh;
}
</style>
