import React, {useEffect, useState} from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import {Card, Alert, Typography, message} from 'antd';
import { useIntl, FormattedMessage } from 'umi';
import styles from './Welcome.less';
import ProCard from "@ant-design/pro-card";
import { getDoctorNumbers, getPatientNumbers } from '@/services/ant-design-pro/api';

const CodePreview: React.FC = ({ children }) => (
  <pre className={styles.pre}>
    <code>
      <Typography.Text copyable>{children}</Typography.Text>
    </code>
  </pre>
);

const Welcome: React.FC = () => {
    const intl = useIntl();
    const user = localStorage.getItem('userName') ;

    // const [type, setType] = useState<string>('welcome') ;
    const [doctorsNumber, setDoctorsNumber] = useState<number>(0) ;
    const [patientNumber, setPatientNumber] = useState<number>(0) ;

    const initDoctorNumber = async () => {
        const result = await getDoctorNumbers();
        setDoctorsNumber(result);
    }

    const initPatientNumber = async () => {
        const result = await getPatientNumbers();
        setPatientNumber(result);
    }

    useEffect(()=>{
        initDoctorNumber();
        initPatientNumber();
    },[]) ;

  return (
      <div>

        <PageContainer
            content="欢迎使用管理员系统"
        >
          <ProCard split="vertical" style={{maxHeight:1000}}>

            <ProCard title="管理员 " colSpan="30%" >
              <div>
                {
                    user !== null && (
                        <div>
                          <p>
                            你好， {user} ！
                          </p>
                        </div>
                    )
                }
                {
                    user === null && (
                        <div>
                          <p>
                            找不到用户！请登录。
                          </p>
                        </div>
                    )
                }
              </div>
            </ProCard>

            <ProCard title="医院信息">
              <Card title="注册医生">
                <p>
                  共：{doctorsNumber}位注册医生。
                </p>
              </Card>

              <Card title="预约信息">
                <p>
                    共：{patientNumber}位注册病人。
                </p>
              </Card>

            </ProCard>

          </ProCard>
    </PageContainer>
</div>
  );
};

export default Welcome;
