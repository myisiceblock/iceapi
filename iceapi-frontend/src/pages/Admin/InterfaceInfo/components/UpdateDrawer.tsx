import {
  DrawerForm,
  ProForm,
  ProFormInstance,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import '@umijs/max';
import { Form, notification, Spin } from 'antd';
import React, { useEffect, useRef, useState } from 'react';
import { updateInterfaceInfoUsingPOST } from '@/services/iceapi-backend/interfaceInfoController';

export type Props = {
  value: API.InterfaceInfo;
};

const UpdateDrawer: React.FC<Props> = (props) => {
  const { value } = props;
  const [form] = Form.useForm<API.InterfaceInfo>();
  const [drawerVisit, setDrawerVisit] = useState(false);
  const formRef = useRef<ProFormInstance>();
  const key = 'message';

  useEffect(() => {
    if (formRef) {
      formRef.current?.setFieldsValue(value);
    }
  }, [value]);

  return (
    <DrawerForm<API.InterfaceInfoUpdateRequest>
      title="修改接口信息"
      onOpenChange={setDrawerVisit}
      form={form}
      open={drawerVisit}
      autoFocusFirstInput
      drawerProps={{
        destroyOnClose: true,
      }}
      trigger={
        <a
          key="updateConfig"
          onClick={() => {
            setDrawerVisit(true);
          }}
        >
          修改
        </a>
      }
      initialValues={value}
      submitTimeout={2000}
      onFinish={async (values) => {
        notification.open({
          key,
          message: '正在修改',
          icon: <Spin />,
          duration: null,
        });
        try {
          await updateInterfaceInfoUsingPOST({
            ...values,
          });
          notification.success({
            key,
            message: '修改成功',
          });
          return true;
        } catch (error: any) {
          notification.error({
            key,
            message: '修改失败',
            description: error.message,
          });
          return false;
        }
      }}
    >
      <ProForm.Group>
        <ProFormText name="id" hidden={true} />
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
          rules={[
            { required: true }
          ]}
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

export default UpdateDrawer;
