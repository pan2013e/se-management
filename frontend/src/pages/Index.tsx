import React from "react";
import {Card, Layout} from 'antd';
import Footer from '../components/Footer';

import styles from './Index.less';
import ProCard from "@ant-design/pro-card";
import Meta from "antd/es/card/Meta";

const Index : React.FC = () => {
    const isLogin =
        localStorage.getItem('userName') != null && localStorage.getItem('userName') != undefined;

    return (
        <div className={styles.container}>
            <Layout>
                <Layout.Header>Header</Layout.Header>
                <Layout.Content>
                    <div style={{paddingLeft:20, display:"flex", backgroundSize:30}}>
                        {isLogin &&
                            <div>
                                <div style={{marginLeft:1220,marginTop:20}}>
                                    用户： {localStorage.getItem('userName')}
                                </div>
                                <div style={{marginLeft:20,marginTop:20,scale:400} }>
                                    欢迎使用医院线上预约平台！
                                </div>
                            </div>
                        }
                        {!isLogin &&
                        <>
                            <a href={'/login'}>请登录</a><br/>
                        </>}
                    </div>
                </Layout.Content>
                        <div>
                            <ProCard gutter={16} style={{ marginTop: 0, marginLeft: 0 }} layout={"center"}>
                                <ProCard bordered>
                                    <Card
                                        cover={<img src={'https://static.thenounproject.com/png/60085-200.png'}/>}
                                    >
                                        <Meta title="挂号系统" description={<a href={''}>www.instagram.com</a> }/>
                                    </Card>
                                </ProCard>
                                <ProCard bordered>
                                    <Card
                                        cover={<img src={'https://www.logosc.cn/oss/icons/2022/03/18/45dd54a444e8ab26f76e36c49c90e443.png'}/>}
                                    >
                                        <Meta title="药房系统" description={<a href={''}>www.instagram.com</a> }/>
                                    </Card>
                                </ProCard>
                                <ProCard bordered>
                                    <Card
                                        cover={<img src={'https://www.logosc.cn/oss/icons/2021/10/21/be754e390d381f33279c01afde5d35fd.png'}/>}
                                    >
                                        <Meta title="医生系统" description={<a href={''}>www.instagram.com</a> }/>
                                    </Card>
                                </ProCard>
                                <ProCard bordered>
                                    <Card
                                        cover={<img src={'https://www.logosc.cn/oss/icons/2021/11/28/4b592fb988813c74734cd9603e33d031.png'}/>}
                                    >
                                        <Meta title="医生查询" description={<a href={''}>www.instagram.com</a> }/>
                                    </Card>
                                </ProCard>
                                <ProCard bordered>
                                    <Card
                                        cover={<img src={'https://www.logosc.cn/oss/icons/2022/04/16/fda47417528b0b161cfd10eff07eba6c.png'}/>}
                                    >
                                        <Meta title={<a href={'/dashboard'}>后台管理</a>} description={<a href={'/dashboard'}>./dashboard</a>}/>
                                    </Card>
                                </ProCard>
                            </ProCard>
                            {/*挂号系统*/}
                            {/*<br/>*/}
                            {/*药房系统*/}
                            {/*<br/>*/}
                            {/*医生系统*/}
                            {/*<br/>*/}
                            {/*医生查询*/}
                            {/*<br/>*/}
                            {/*<a href={'/dashboard'}>后台管理</a>*/}
                        </div>
                <Layout.Footer>
                    <Footer />
                </Layout.Footer>
            </Layout>
        </div>
    )
}

export default Index;