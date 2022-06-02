// @ts-ignore
/* eslint-disable */
import { request } from 'umi';

/**
 * 获取全部预约的数据
 */
export async function getArrangeInfo(options?: { [key: string]: any }) {
    return request<API.BackendResult>('http://localhost:3000/api/arrange', {
        method: 'GET' ,
        ...( options || {} )
    })
}
/**
 * 获取全部医生的数据
 */
export async function getDoctorInfo(options?: { [key: string]: any }) {
    return request<API.BackendResult>('http://localhost:3000/api/doctor/all', {
        method: 'GET' ,
        ...(options || {}),
    })
}

/** 获取当前的用户 GET /api/currentUser */
export async function currentUser(body: { [key: string]: any }, options?: { [key: string]: any }) {
    return request<API.BackendResult>('http://localhost:3000/api/oauth/verify', {
        method: 'GET',
        params: body,
        ...(options || {}),
    });
}

/** 退出登录接口 POST /api/login/outLogin */
export async function logout(options?: { [key: string]: any }) {
    localStorage.clear();
    return request<Record<string, any>>('http://localhost:3000/api/oauth/logout', {
        method: 'GET',
        ...(options || {}),
    });
}

export async function getCaptcha(options?: { [key: string]: any }) {
    return request<API.BackendResult>('http://localhost:3000/api/oauth/captcha', {
        method: 'GET',
        ...(options || {}),
    });
}

/** 登录接口 POST /api/login/account */
export async function login(body: { [key: string]: any }, options?: { [key: string]: any }) {
    return request<API.BackendResult>('http://localhost:3000/api/oauth/login', {
        method: 'POST',
        params: body,
        ...(options || {}),
    });
}

export async function patientRegister(
    body: { [key: string]: any },
    options?: { [key: string]: any },
) {
    return request<API.BackendResult>('http://localhost:3000/api/oauth/register', {
        method: 'POST',
        params: body,
        ...(options || {}),
    });
}

/** 此处后端没有提供注释 GET /api/notices */
export async function getNotices(options?: { [key: string]: any }) {
    return request<API.NoticeIconList>('/api/notices', {
        method: 'GET',
        ...(options || {}),
    });
}

/** 获取规则列表 GET /api/rule */
export async function rule(
    params: {
        // query
        /** 当前的页码 */
        current?: number;
        /** 页面的容量 */
        pageSize?: number;
    },
    options?: { [key: string]: any },
) {
    return request<API.RuleList>('/api/rule', {
        method: 'GET',
        params: {
            ...params,
        },
        ...(options || {}),
    });
}

/** 新建规则 PUT /api/rule */
export async function updateRule(options?: { [key: string]: any }) {
    return request<API.RuleListItem>('/api/rule', {
        method: 'PUT',
        ...(options || {}),
    });
}

/** 新建规则 POST /api/rule */
export async function addRule(options?: { [key: string]: any }) {
    return request<API.RuleListItem>('/api/rule', {
        method: 'POST',
        ...(options || {}),
    });
}

/** 删除规则 DELETE /api/rule */
export async function removeRule(options?: { [key: string]: any }) {
    return request<Record<string, any>>('/api/rule', {
        method: 'DELETE',
        ...(options || {}),
    });
}
