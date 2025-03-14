import Vue from 'vue';
import App from './App.vue';
import VueResource from "vue-resource";

new Vue({
    el: '#app',
    render: h => h(App)
});

Vue.use(VueResource);
Vue.http.options.root = '/api';