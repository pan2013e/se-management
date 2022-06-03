import {HomeOutlined, HeartOutlined, LockOutlined, PlusOutlined, UserOutlined} from '@ant-design/icons';
import { Button, message, Input, Drawer } from 'antd';
import React, { useState, useRef } from 'react';
import { useIntl, FormattedMessage } from 'umi';
import { PageContainer, FooterToolbar } from '@ant-design/pro-layout';
import type { ProColumns, ActionType } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import { ModalForm, ProFormText, ProFormTextArea } from '@ant-design/pro-form';
import type { ProDescriptionsItemProps } from '@ant-design/pro-descriptions';
import ProDescriptions from '@ant-design/pro-descriptions';
import type { FormValueType } from './components/UpdateForm';
import UpdateForm from './components/UpdateForm';
import { rule, addDoctor, updateRule, removeRule, getDoctors } from '@/services/ant-design-pro/api';
import styles from "@/pages/user/Login/index.less";


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

const handleUpdate = async (fields: FormValueType) => {
  const hide = message.loading('Configuring');
  try {
    await updateRule({
      name: fields.name,
      desc: fields.desc,
      key: fields.key,
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

  const actionRef = useRef<ActionType>();
  const [currentRow, setCurrentRow] = useState<API.DoctorInfoItem>();
  const [selectedRowsState, setSelectedRows] = useState<API.DoctorInfoItem[]>([]);

  /**
   * @en-US International configuration
   * @zh-CN 国际化配置
   * */
  const intl = useIntl();

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
            handleUpdateModalVisible(true);
            setCurrentRow(record);
          }}
        >
          查看排班
        </a>,
        <a key="subscribeAlert" href="https://procomponents.ant.design/">
          修改排班
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
          const success = await handleUpdate(value);
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
      >

      </ModalForm>
    </PageContainer>
  );
};

export default TableList;
