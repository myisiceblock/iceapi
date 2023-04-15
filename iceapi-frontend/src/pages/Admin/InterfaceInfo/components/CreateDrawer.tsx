import {
  DrawerForm,
  ProForm,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import '@umijs/max';
import { Button, Form, notification, Spin } from 'antd';
import { addInterfaceInfoUsingPOST } from '@/services/iceapi-backend/interfaceInfoController';
import React, { useState } from 'react';
import { PlusOutlined } from '@ant-design/icons';

const CreateDrawer: React.FC = () => {
  const [form] = Form.useForm<API.InterfaceInfo>();
  const [drawerVisit, setDrawerVisit] = useState(false);
  const key = 'message';

  return (
    <DrawerForm<API.InterfaceInfoAddRequest>
      title="添加接口信息"
      onOpenChange={setDrawerVisit}
      form={form}
      open={drawerVisit}
      autoFocusFirstInput
      drawerProps={{
        destroyOnClose: true,
      }}
      trigger={
        <Button
          type="primary"
          key="primary"
          onClick={() => {
            setDrawerVisit(true);
          }}
        >
          <PlusOutlined /> 新建
        </Button>
      }
      submitTimeout={2000}
      onFinish={async (values) => {
        notification.open({
          key,
          message: '正在添加',
          icon: <Spin />,
          duration: null,
        });
        try {
          await addInterfaceInfoUsingPOST({
            ...values,
          });
          notification.success({
            key,
            message: '创建成功',
          });
          return true;
        } catch (error: any) {
          notification.error({
            key,
            message: '创建失败',
            description: error.message,
          });
          return false;
        }
      }}
    >
      <ProForm.Group>
        <ProFormText
          name="name"
          width="md"
          label="接口名称"
          tooltip="最长为 24 位"
          placeholder="请输入名称"
          rules={[{ required: true }]}
        />
        <ProFormSelect
          name="method"
          label="请求类型"
          valueEnum={{
            POST: 'POST',
            GET: 'GET',
            DELETE: 'DELETE',
            PUT: 'PUT',
            PATCH: 'PATCH',
          }}
          placeholder="请选择请求类型"
          rules={[{ required: true }]}
        />
      </ProForm.Group>
      <ProFormText
        name="url"
        width="md"
        label="接口地址"
        placeholder="请输入请求地址"
        rules={[{ required: true }, { type: 'url', warningOnly: true }, { type: 'string', min: 6 }]}
      />
      <ProFormTextArea colProps={{ span: 24 }} name="description" label="接口描述" />
      <ProFormTextArea colProps={{ span: 24 }} name="requestParams" label="请求参数" />
      <ProFormTextArea colProps={{ span: 24 }} name="requestHeader" label="请求头" />
      <ProFormTextArea colProps={{ span: 24 }} name="responseHeader" label="响应头" />
    </DrawerForm>
  );
};

export default CreateDrawer;
