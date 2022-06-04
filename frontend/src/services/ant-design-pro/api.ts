// @ts-ignore
/* eslint-disable */
import { request } from 'umi';
import {api} from '@/config';
import {message} from "antd";

const baseUrl = `${api.protocol}://${api.host}:${api.port}${api.prefix}`;

/** 获取当前的用户 GET /api/oauth/verify */
export async function currentUser(
    body: { [key: string]: any },
    options?: { [key: string]: any }
) {
    return request<API.BackendResult>(`${baseUrl}/oauth/verify`, {
        method: 'GET',
        params: body,
        ...(options || {}),
    });
}

/** 退出登录接口 POST /api/login/outLogin */
export async function logout(
    options?: { [key: string]: any }
) {
    localStorage.clear();
    return request<API.BackendResult>(`${baseUrl}/oauth/logout`, {
        method: 'GET',
        ...(options || {}),
    });
}

export async function getCaptcha(
    options?: { [key: string]: any }
) {
    return request<API.BackendResult>(`${baseUrl}/oauth/captcha`, {
        method: 'GET',
        ...(options || {}),
    });
}

/** 登录接口 POST /api/login/account */
export async function login(
    body: { [key: string]: any },
    options?: { [key: string]: any }
) {
    return request<API.BackendResult>(`${baseUrl}/oauth/login`, {
        method: 'POST',
        params: body,
        ...(options || {}),
    });
}

export async function patientRegister(
    body: { [key: string]: any },
    options?: { [key: string]: any },
) {
    return request<API.BackendResult>(`${baseUrl}/oauth/register`, {
        method: 'POST',
        params: body,
        ...(options || {}),
    });
}

export async function getNotices(userName: string, options?: { [key: string]: any }) {
    return request<API.BackendResult>(`${baseUrl}/ws/mq/${userName}`, {
        method: 'GET',
        ...(options || {}),
    });
}

/** 获取规则列表 GET /api/rule */
export async function getDoctors(
    options?: { [key: string]: any },
) {
    const res = await request<API.BackendResult>(`${baseUrl}/doctor/all`, {
        method: 'GET',
        ...(options || {}),
    });
    if(res.code < 0){
        message.error(res.message);
        return {
            data: [],
            success: false
        };
    } else {
        return {
            data: res.data.doctorInfos,
            success: true
        };
    }
}

export async function getDoctorNumbers(
    options?: { [key: string]: any },
) {
    const res = await request<API.BackendResult>(`${baseUrl}/statistics/doctors`, {
        method: 'GET',
        ...(options || {}),
    });
    if(res.code < 0){
        message.error(res.message);
        return undefined;
    } else {
        return res.data.number;
    }
}

export async function getPatientNumbers(
    options?: { [key: string]: any },
) {
    const res = await request<API.BackendResult>(`${baseUrl}/statistics/patients`, {
        method: 'GET',
        ...(options || {}),
    });
    if(res.code < 0){
        message.error(res.message);
        return undefined;
    } else {
        return res.data.number;
    }
}

/** 新建规则 PUT /api/rule */
export async function updateRule(options?: { [key: string]: any }) {
    return request<API.RuleListItem>('/api/rule', {
        method: 'PUT',
        ...(options || {}),
    });
}

/** 新建规则 POST /api/rule */
export async function addDoctor(
    body: { [key: string]: any },
    options?: { [key: string]: any }
) {
    return request<API.BackendResult>(`${baseUrl}/doctor`, {
        method: 'POST',
        params: body,
        ...(options || {}),
    });
}

/** 删除规则 DELETE /api/rule */
export async function deleteDoctor(
    body: { [key: string]: any },
    options?: { [key: string]: any }
) {
    return request<API.BackendResult>(`${baseUrl}/doctor`, {
        method: 'DELETE',
        params: body,
        ...(options || {}),
    });
}
