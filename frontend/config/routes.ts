export default [
    {
        title: '登录',
        path: '/login',
        layout: false,
        component: './user/Login',
    },
    {
        path: '/welcome',
        name: 'welcome',
        icon: 'smile',
        component: './Welcome',
    },
    {
        name: 'doctor-info',
        icon: 'table',
        path: '/doctors',
        component: './TableList',
    },
    {
        path: '/',
        layout: false,
        component: './Index'
    },
    {
        title: '404',
        layout: false,
        component: './404'
    },
];
