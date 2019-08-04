import Login from './views/Login.vue'
import NotFound from './views/404.vue'
import Home from './views/Home.vue'
import Main from './views/Main.vue'
import Table from './views/nav1/Table.vue'
import Lost from './views/nav1/Lost.vue'
import Page4 from './views/nav2/Page4.vue'
import Page5 from './views/nav3/Page5.vue'
import Page6 from './views/nav3/Page6.vue'
import echarts from './views/charts/echarts.vue'

let routes = [
    {
        path: '/login',
        component: Login,
        name: '',
        hidden: true
    },
    {
        path: '/404',
        component: NotFound,
        name: '',
        hidden: true
    },
    //{ path: '/main', component: Main },
    {
        path: '/',
        component: Home,
        name: '用户相关',
        iconCls: 'el-icon-message',//图标样式class
        children: [
            { path: '/main', component: Main, name: '主页', hidden: true },
            { path: '/table', component: Table, name: '所有用户' },
            { path: '/lost', component: Lost, name: '违规用户' }
        ]
    },
    {
        path: '/',
        component: Home,
        name: '音乐相关',
        iconCls: 'fa fa-id-card-o',
        leaf: true,
        children: [
            { path: '/page4', component: Page4, name: '背景音乐' }
        ]
    },
    {
        path: '/',
        component: Home,
        name: '视频相关',
        iconCls: 'fa fa-address-card',
        children: [
            { path: '/page5', component: Page5, name: '视频列表' },
            { path: '/page6', component: Page6, name: '举报处理' }
        ]
    },
    {
        path: '/',
        component: Home,
        name: '实时数据',
        iconCls: 'fa fa-bar-chart',
        leaf: true,
        children: [
            { path: '/echarts', component: echarts, name: '实时数据' }
        ]
    },
    {
        path: '*',
        hidden: true,
        redirect: { path: '/404' }
    }
];

export default routes;