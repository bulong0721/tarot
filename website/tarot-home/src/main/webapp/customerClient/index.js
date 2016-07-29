import Vue from 'vue';
import Mint from 'mint-ui';
import 'mint-ui/lib/style.css';
import VueRouter from 'vue-router';
import VueResource from 'vue-resource'
import { configRouter } from './app/route-config'
import App from './app/view/app.vue'

// install router & VueResource
Vue.use(Mint);
Vue.use(VueRouter)
Vue.use(VueResource)

// create router
const router = new VueRouter({
  history: false,
  saveScrollPosition: true
})

// configure router
configRouter(router)

// boostrap the app

router.start(Vue.extend({
  components: {
    app: App
  }
}), 'body');
// just for debugging
//window.router = router
