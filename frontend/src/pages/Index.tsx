import React from "react";
import { Layout } from 'antd';
import Footer from '../components/Footer';

import styles from './Index.less';

const Index : React.FC = () => {
    const isLogin =
        localStorage.getItem('userName') != null && localStorage.getItem('userName') != undefined;

    return (
        <div className={styles.container}>
            <Layout>
                <Layout.Header>Header</Layout.Header>
                <Layout.Content>
                    <div style={{paddingLeft:20, display:"flex"}}>
                        {isLogin && <div>欢迎您， {localStorage.getItem('userName')}</div>}
                        {!isLogin && <><a href={'/login'}>请登录</a><br/></>}
                        <div>
                            挂号系统
                            <br/>
                            药房系统
                            <br/>
                            医生系统
                            <br/>
                            医生查询
                            <br/>
                            <a href={'/dashboard'}>后台管理</a>
                        </div>
                    </div>
                </Layout.Content>
                <Layout.Footer>
                    <Footer />
                </Layout.Footer>
            </Layout>
        </div>
    )
}

export default Index;