﻿export default [
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
        path: '/admin',
        name: 'admin',
        icon: 'crown',
        access: 'canAdmin',
        component: './Admin',
        routes: [
            {
                path: '/admin/sub-page',
                name: 'sub-page',
                icon: 'smile',
                component: './Welcome',
            },
            {
                component: './404',
            },
        ],
    },
    {
        name: 'list.table-list',
        icon: 'table',
        path: '/list',
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
