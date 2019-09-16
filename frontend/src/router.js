import Vue from 'vue'
import Router from 'vue-router'
import Organizations from '@/components/Organizations'
// import Service from '@/components/Service'
// import Bootstrap from '@/components/Bootstrap'
// import User from '@/components/User'
// import Login from '@/components/Login'
// import Protected from '@/components/Protected'

// import store from './store'

Vue.use(Router);

const router = new Router({
    mode: 'history', // uris without hashes #, see https://router.vuejs.org/guide/essentials/history-mode.html#html5-history-mode
    routes: [
        { path: '/organization', component: Organizations },
        // { path: '/callservice', component: Service },
        // { path: '/bootstrap', component: Bootstrap },
        // { path: '/user', component: User },
        // { path: '/login', component: Login },
        // {
        //     path: '/protected',
        //     component: Protected,
        //     meta: {
        //         requiresAuth: true
        //     }
        // },

        // otherwise redirect to home
        { path: '*', redirect: '/organization' }
    ]
});

export default router;