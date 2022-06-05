﻿export default [
    {
        path: '/',
        layout: false,
        component: './Index'
    },
    {
        path: '/login',
        layout: false,
        component: './user/Login',
    },
    {
        path: '/dashboard',
        name: 'welcome',
        icon: 'Dashboard',
        component: './Welcome',
    },
    {
        name: 'doctor-info',
        icon: 'table',
        path: '/doctors',
        component: './TableList',
    },
    {
        title: '404',
        layout: false,
        component: './404'
    },
];
