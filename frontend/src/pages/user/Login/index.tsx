import { LockOutlined, UserOutlined } from '@ant-design/icons';
import { message, Tabs, Result } from 'antd';
import React, { useState } from 'react';
import { ProFormText, LoginForm } from '@ant-design/pro-form';
import { history } from 'umi';
import Footer from '@/components/Footer';
import { login, patientRegister } from '@/services/ant-design-pro/api';

import styles from './index.less';

const Login: React.FC = () => {
    const isLogin =
        localStorage.getItem('userName') != null && localStorage.getItem('userName') != undefined;
    const [type, setType] = useState<string>('login');

    const handleSubmit = async (values: Record<string, any>) => {
        if (isLogin && type === 'login') {
            localStorage.clear();
            history.push('/');
            return;
        }
        try {
            if (type === 'register') {
                const msg = await patientRegister({
                    userName: values.regUserName,
                    password: values.regPassword,
                    authorization: localStorage.getItem('token'),
                });
                if (msg.code === 0) {
                    message.success('注册成功');
                    history.push('/');
                } else {
                    message.error(msg.message);
                }
            } else {
                const msg = await login({
                    userName: values.userName,
                    password: values.password,
                });
                if (msg.code === 0) {
                    message.success('登录成功');
                    localStorage.setItem('userName', msg.data.userName);
                    localStorage.setItem('token', msg.data.token);
                    if (!history) return;
                    const { query } = history.location;
                    const { redirect } = query as { redirect: string };
                    console.log(redirect);
                    if (redirect) {
                        history.push('/welcome');
                    } else {
                        window.location.href = decodeURIComponent(redirect);
                    }
                } else {
                    message.error(msg.message);
                }
            }
        } catch (error) {
            message.error('网络错误');
        }
    };

    return (
        <div className={styles.container}>
            <div className={styles.content} style={{ marginBottom: 24 }}>
                <LoginForm
                    logo={<img alt="logo" src="/logo.svg" />}
                    title="统一认证平台"
                    subTitle=" "
                    onFinish={async (values) => {
                        await handleSubmit(values);
                    }}
                    submitter={{
                        searchConfig: {
                            submitText:
                                type === 'login'
                                    ? isLogin === false
                                        ? '登录'
                                        : '退出登录'
                                    : '注册',
                            resetText: '重置',
                        },
                    }}
                >
                    <Tabs activeKey={type} onChange={setType}>
                        <Tabs.TabPane key="login" tab="登录" />
                        <Tabs.TabPane key="register" tab="注册" />
                    </Tabs>

                    {type === 'login' && isLogin === false && (
                        <>
                            <ProFormText
                                name="userName"
                                fieldProps={{
                                    size: 'large',
                                    prefix: <UserOutlined className={styles.prefixIcon} />,
                                }}
                                placeholder="用户名"
                                rules={[
                                    {
                                        required: true,
                                        message: '请输入用户名',
                                    },
                                ]}
                            />
                            <ProFormText.Password
                                name="password"
                                fieldProps={{
                                    size: 'large',
                                    prefix: <LockOutlined className={styles.prefixIcon} />,
                                }}
                                placeholder="密码"
                                rules={[
                                    {
                                        required: true,
                                        message: '请输入密码',
                                    },
                                ]}
                            />
                        </>
                    )}

                    {type === 'login' && isLogin === true && (
                        <>
                            <Result title="您已登录" />
                        </>
                    )}

                    {type === 'register' && (
                        <>
                            <p>目前仅支持病人用户的自助注册</p>
                            <ProFormText
                                name="regUserName"
                                fieldProps={{
                                    size: 'large',
                                    prefix: <UserOutlined className={styles.prefixIcon} />,
                                }}
                                placeholder="用户名"
                                rules={[
                                    {
                                        required: true,
                                        message: '请输入用户名',
                                    },
                                ]}
                            />
                            <ProFormText.Password
                                name="regPassword"
                                fieldProps={{
                                    size: 'large',
                                    prefix: <LockOutlined className={styles.prefixIcon} />,
                                }}
                                placeholder="密码"
                                rules={[
                                    {
                                        required: true,
                                        message: '请输入密码',
                                    },
                                    {
                                        min: 8,
                                        message: '密码长度不能小于8',
                                    },
                                ]}
                            />
                            <ProFormText.Password
                                name="regConfirmPassword"
                                fieldProps={{
                                    size: 'large',
                                    prefix: <LockOutlined className={styles.prefixIcon} />,
                                }}
                                placeholder="确认密码"
                                rules={[
                                    {
                                        required: true,
                                        message: '请确认密码',
                                    },
                                    ({ getFieldValue }) => ({
                                        validator(_, value) {
                                            if (!value || getFieldValue('password') == value) {
                                                return Promise.resolve();
                                            }
                                            return Promise.reject('两次密码输入不一致');
                                        },
                                    }),
                                ]}
                            />
                        </>
                    )}
                </LoginForm>
            </div>
            <Footer />
        </div>
    );
};

export default Login;
