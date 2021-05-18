// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'

// 引入路由配置
import VueRouter from 'vue-router'
import routes from './router'

// 引入ElementUI
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

// 引入echarts
import echarts from 'echarts'
Vue.prototype.$echarts = echarts



Vue.use(VueRouter)
Vue.use(ElementUI)

const router = new VueRouter({
  routes
})


router.beforeEach(function (to, from, next) {
  if (to.path == '/') {
    next("/total")
  } else {
    next()
  }
})

/* eslint-disable no-new */
new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
