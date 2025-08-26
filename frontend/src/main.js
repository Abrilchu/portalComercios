import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { Quasar, Dialog, Notify, Loading } from 'quasar'
import router from './router'
import App from './App.vue'

// Import Quasar css
import 'quasar/dist/quasar.css'

// Import icon libraries
import '@quasar/extras/material-icons/material-icons.css'
import '@quasar/extras/roboto-font/roboto-font.css'

// Import app css
import './css/app.css'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(Quasar, {
  plugins: {
    Dialog,
    Notify,
    Loading
  },
  config: {
    notify: {
      position: 'top',
      timeout: 5000
    }
  }
})

app.mount('#app')
