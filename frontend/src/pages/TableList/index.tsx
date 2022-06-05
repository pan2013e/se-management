import {HomeOutlined, HeartOutlined, LockOutlined, PlusOutlined, UserOutlined} from '@ant-design/icons';
import {Button, message, FormInstance, TimePicker, Select} from 'antd';
import React, {useState, useRef, useEffect} from 'react';
import { FormattedMessage } from 'umi';
import { PageContainer, FooterToolbar } from '@ant-design/pro-layout';
import type { ProColumns, ActionType } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import { ModalForm, ProFormText, ProFormTextArea } from '@ant-design/pro-form';
import type { ProDescriptionsItemProps } from '@ant-design/pro-descriptions';
import ProDescriptions from '@ant-design/pro-descriptions';
import type { FormValueType } from './components/UpdateForm';
import UpdateForm from './components/UpdateForm';
import {addDoctor, removeRule, getDoctors, addArrange, getDoctorArrange} from '@/services/ant-design-pro/api';
import styles from "@/pages/user/Login/index.less";
import {Moment} from "moment";


const handleAdd = async (fields: API.DoctorInfoItem) => {
    const res = await addDoctor(fields);
    if(res.code === 0){
        message.success('添加成功');
        return true;
    } else {
        message.error(res.message);
        return false;
    }
};
const handleCheck = async (id : number )=>{
    const res = await getDoctorArrange(id) ;
    if ( res.code === 0 ) {
        message.success('查询成功');
        return true;
    }else {
        message.error(res.message);
        return false;
    }
};
const handleArrange = async (fields: API.ArrangeInfoItem) => {
  const hide = message.loading('Configuring');
  try {
    await addArrange({
      id: fields.id,
      startTime: fields.startTime,
      endTime: fields.endTime,
      dayType: fields.dayType
    });
    hide();
    message.success('Configuration is successful');
    return true;
  } catch (error) {
    hide();
    message.error('Configuration failed, please try again!');
    return false;
  }
};

const handleRemove = async (selectedRows: API.DoctorInfoItem[]) => {
  const hide = message.loading('正在删除');
  if (!selectedRows) return true;
  try {
    await removeRule({
      key: selectedRows.map((row) => row.key),
    });
    hide();
    message.success('Deleted successfully and will refresh soon');
    return true;
  } catch (error) {
    hide();
    message.error('Delete failed, please try again');
    return false;
  }
};

const TableList: React.FC = () => {
  /**
   * @en-US Pop-up window of new window
   * @zh-CN 新建窗口的弹窗
   *  */
  const [createModalVisible, handleModalVisible] = useState<boolean>(false);
  /**
   * @en-US The pop-up window of the distribution update window
   * @zh-CN 分布更新窗口的弹窗
   * */
  const [updateModalVisible, handleUpdateModalVisible] = useState<boolean>(false);
    /**
     * 查看排班表的表单
     */
  const [checkModalVisible, handleCheckModalVisible] = useState<boolean>(false) ;
  const actionRef = useRef<ActionType>();
  const [currentRow, setCurrentRow] = useState<API.DoctorInfoItem>();
  const [selectedRowsState, setSelectedRows] = useState<API.DoctorInfoItem[]>([]);

    /**
     * 添加排班时刷新表单的默认项
     */
  const FormRef = useRef<FormInstance>(null);
  useEffect( ()=>{
      FormRef && FormRef.current && FormRef.current.resetFields() ;
  },[currentRow]) ;

  /**
   * 收集表单中的时间
   */
  type day = 'Monday'|'TuesDay'|'Wednesday'|'Thursday'|'Friday'|'Saturday'|'Sunday' ;
  const [day_Type,setDayType]= useState<day>('Monday') ;
  const [start_Time,setStartTime] = useState<string>('null');
  const [end_Time,setEndTime] = useState<string>('null');
  const timeFormat = 'HH:mm:ss' ;

    const onStartChange = (time: Moment|null, timeString: string) => {
        setStartTime(timeString);
        // console.log(time, timeString);
    };
    const onEndChange = (time: Moment|null, timeString: string) => {
        setEndTime(timeString);
        // console.log(time, timeString);
    };

  const columns: ProColumns<API.DoctorInfoItem>[] = [
    {
      title: '用户ID',
      dataIndex: 'id',
      sorter: (a, b) => parseInt(a.id) - parseInt(b.id),
      renderText: (val: string) =>
          `${val}`,
    },
    {
      title: '用户名',
      dataIndex: 'userName',
      valueType: 'textarea',
      renderText: (val: string) =>
          `${val}`,
    },
    {
      title: '姓名',
      dataIndex: 'realName',
      valueType: 'textarea',
      renderText: (val: string) =>
          `${val}`,
    },
    {
      title: '所在医院',
      dataIndex: 'hospital',
      valueType: 'textarea',
      renderText: (val: string) =>
          `${val}`,
    },
    {
      title: '所属科室',
      dataIndex: 'department',
      valueType: 'textarea',
      renderText: (val: string) =>
          `${val}`,
    },
    {
      title: <FormattedMessage id="pages.searchTable.titleOption" defaultMessage="Operating" />,
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => [
        <a
          key="config"
          onClick={() => {
              setCurrentRow(record);
              handleUpdateModalVisible(true);
          }}
        >
          添加排班
        </a>,
        <a
            key="subscribeAlert"
            onClick={ ()=>{
                setCurrentRow(record);
                handleCheckModalVisible(true);
            }}
        >
          查看排班
        </a>,
      ],
    },
  ];

  return (
    <PageContainer>
      <ProTable<API.DoctorInfoItem>
        headerTitle='医生列表'
        actionRef={actionRef}
        rowKey="id"
        search={false}
        toolBarRender={() => [
          <Button
            type="primary"
            key="primary"
            onClick={() => {
              handleModalVisible(true);
            }}
          >
            <PlusOutlined /> 添加医生
          </Button>,
        ]}
        request={getDoctors}
        columns={columns}
        rowSelection={{
          onChange: (_, selectedRows) => {
            setSelectedRows(selectedRows);
          },
        }}
      />
      {selectedRowsState?.length > 0 && (
        <FooterToolbar
          extra={
            <div>
              已选择{' '}
              <a style={{ fontWeight: 600 }}>{selectedRowsState.length}</a>{' '}
              项
            </div>
          }
        >
          <Button
            onClick={async () => {
              await handleRemove(selectedRowsState);
              setSelectedRows([]);
              actionRef.current?.reloadAndRest?.();
            }}
          >
            批量删除
          </Button>
        </FooterToolbar>
      )}
      <ModalForm
        title='添加医生'
        width="500px"
        visible={createModalVisible}
        onVisibleChange={handleModalVisible}
        onFinish={async (value) => {
          const success = await handleAdd(value as API.DoctorInfoItem);
          if (success) {
            handleModalVisible(false);
            if (actionRef.current) {
              actionRef.current.reload();
            }
          }
        }}
      >
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
        <ProFormText
            name="realName"
            fieldProps={{
              size: 'large',
              prefix: <UserOutlined className={styles.prefixIcon} />,
            }}
            placeholder="姓名"
            rules={[
              {
                required: true,
                message: '请输入姓名',
              },
            ]}
        />
        <ProFormText
            name="hospital"
            fieldProps={{
              size: 'large',
              prefix: <HomeOutlined className={styles.prefixIcon} />,
            }}
            placeholder="医院"
            rules={[
              {
                required: true,
                message: '请输入医院名',
              },
            ]}
        />
        <ProFormText
            name="department"
            fieldProps={{
              size: 'large',
              prefix: <HeartOutlined className={styles.prefixIcon} />,
            }}
            placeholder="科室"
            rules={[
              {
                required: true,
                message: '请输入科室名',
              },
            ]}
        />
        <ProFormText.Password
            name="password"
            fieldProps={{
              size: 'large',
              prefix: <LockOutlined className={styles.prefixIcon} />,
            }}
            placeholder="初始密码"
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
      </ModalForm>

      <ModalForm
        onFinish={async (value) => {
          console.log(value);
          const success = await handleArrange({
              id:(currentRow && currentRow.id ) ,
              startTime:start_Time,
              endTime:end_Time,
              dayType:day_Type
          });
          if (success) {
            handleUpdateModalVisible(false);
            setCurrentRow(undefined);
            if (actionRef.current) {
              actionRef.current.reload();
            }
          }
        }}
        onFinishFailed={() => {
          handleUpdateModalVisible(false);
        }}
        visible={updateModalVisible}
        onVisibleChange={handleUpdateModalVisible}
        formRef={FormRef}
      >
          <ProFormText
              name="userName"
              initialValue = { (currentRow !== undefined && currentRow.id ) || (currentRow===undefined && '')}
              fieldProps={{
                  size: 'large',
                  prefix: <UserOutlined className={styles.prefixIcon} />,
              }}
              placeholder="用户ID"
              rules={[
                  {
                      required: true,
                      message: '请输入用户名',
                  },
              ]}
          />
          <ProFormText
              name="realName"
              initialValue={ (currentRow !== undefined && currentRow.realName) || (currentRow===undefined && '')}
              fieldProps={{
                  size: 'large',
                  prefix: <UserOutlined className={styles.prefixIcon} />,
              }}
              placeholder="姓名"
              rules={[
                  {
                      required: true,
                      message: '请输入姓名',
                  },
              ]}
          />
          <Select value={day_Type} onChange={setDayType}>
              <option value='Monday'>星期一</option>
              <option value='Tuesday'>星期二</option>
              <option value='Wednesday'>星期三</option>
              <option value='Thursday'>星期四</option>
              <option value='Friday'>星期五</option>
              <option value='Saturday'>星期六</option>
              <option value='Sunday'>星期日</option>
          </Select>
          <TimePicker
              placeholder='开始时间'
              format={timeFormat}
              onChange={onStartChange}
          />
          <TimePicker
              placeholder='结束时间'
              format={timeFormat}
              onChange={onEndChange}
          />

      </ModalForm>

        <ModalForm
            onFinish={async (value) => {
                console.log(value);
                const success = await handleCheck(( currentRow !== undefined && currentRow.id ) || 0 );
                if (success) {
                    handleCheckModalVisible(false);
                    setCurrentRow(undefined);
                    if (actionRef.current) {
                        actionRef.current.reload();
                    }
                }
            }}
            onFinishFailed={() => {
                handleCheckModalVisible(false);
            }}
            visible={checkModalVisible}
            onVisibleChange={handleCheckModalVisible}
            >
            <ProTable
                request={getDoctorArrange(currentRow.id)}
            >

            </ProTable>
        </ModalForm>
    </PageContainer>
  );
};

export default TableList;
