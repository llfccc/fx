import Vue from 'vue';
import Router from 'vue-router';
// import Register from '../components/page/register.vue'
Vue.use(Router);

export default new Router({
    routes: [{
        path: '/',
        redirect: '/login'
    },
    // {
    //     path: '/register',
    //     name: 'Register',
    //     component: Register
    // },
    {
        path: '/readme',
        component: resolve => require(['../components/common/Home.vue'], resolve),
        children: [{
            path: '/',
            component: resolve => require(['../components/page/Readme.vue'], resolve)
        },

        {
            path: '/insertDevWork',
            component: resolve => require(['../components/page/insertData/insertDevWork.vue'], resolve)
        },
        {
            path: '/insertSaleEvent',
            component: resolve => require(['../components/page/insertData/insertSaleEvent.vue'], resolve)
        },
        {
            path: '/insertWeekSummary',
            component: resolve => require(['../components/page/insertData/insertWeekSummary.vue'], resolve) // vue-datasource组件
        },
        {
            path: '/viewWeekly',
            component: resolve => require(['../components/page/insertData/viewWeekly.vue'], resolve)
        },

        {
            path: '/viewSaleEvent',
            component: resolve => require(['../components/page/insertData/viewSaleEvent.vue'], resolve)
        },
       
        {
            path: '/analysisTrend',
            component: resolve => require(['../components/page/analysisData/analysisTrend.vue'], resolve) // vue-datasource组件
        },
        {
            path: '/analysisDevDepart',
            component: resolve => require(['../components/page/analysisData/analysisDevDepart.vue'], resolve) // vue-datasource组件
        },
        {
            path: '/analysisDevProject',
            component: resolve => require(['../components/page/analysisData/analysisDevProject.vue'], resolve) // vue-datasource组件
        },

        {
            path: '/analysisWeekly',
            component: resolve => require(['../components/page/analysisData/analysisWeekly.vue'], resolve) // vue-datasource组件
        },
        {
            path: '/projectManage',
            component: resolve => require(['../components/page/manageData/projectManage.vue'], resolve) // vue-datasource组件
        },
        {
            path: '/analysisSaleEvent',
            component: resolve => require(['../components/page/analysisData/analysisSaleEvent.vue'], resolve) // vue-datasource组件
        },
        {
            path: '/analysisSalePerformance',
            component: resolve => require(['../components/page/analysisData/analysisSalePerformance.vue'], resolve) // vue-datasource组件
        },
        {
            path: '/analysisSalePerformance',
            component: resolve => require(['../components/page/analysisData/analysisSalePerformance.vue'], resolve) // vue-datasource组件
        },
        
        {
            path: '/project_crud',
            component: resolve => require(['../components/page/manageData/project_crud.vue'], resolve) // vue-datasource组件
        },
        {
            path: '/saleCustmoer',
            component: resolve => require(['../components/page/manageData/saleCustmoer.vue'], resolve) // vue-datasource组件
        },     
        ]
    },
    {
        path: '/login',
        component: resolve => require(['../components/page/Login.vue'], resolve)
    },
    ]
})