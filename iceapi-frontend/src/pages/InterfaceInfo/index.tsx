import { PageContainer } from '@ant-design/pro-components';
import {Button, Card, Descriptions, Divider, Empty, Form, Input, notification, Spin} from 'antd';
import React, { useEffect, useState } from 'react';
import {
  getInterfaceInfoByIdUsingGET,
  invokeInterfaceInfoUsingPOST
} from '@/services/iceapi-backend/interfaceInfoController';
import { useParams } from '@@/exports';

/**
 * 接口详情
 * @constructor
 */
const Index: React.FC = () => {
  const [loading, setLoading] = useState(false);
  const [invokeLoading, setInvokeLoading] = useState(false);
  const [data, setData] = useState<API.InterfaceInfo>();
  const [invokeRes,setInvokeRes] = useState<any>();
  const params = useParams();

  const loadData = async () => {
    if (!params.id) {
      notification.error({
        message: '参数不存在',
      });
      return;
    }
    setLoading(true);
    try {
      const res = await getInterfaceInfoByIdUsingGET({
        id: Number(params.id),
      });
      setData(res.data);
    } catch (error: any) {
      notification.error({
        message: '加载失败. 请刷新!',
      });
    }
    setLoading(false);
  };

  const onFinish = async (values: any) => {
    if (!params.id) {
      notification.error({
        message: '接口不存在',
      });
      return;
    }
    setInvokeLoading(true);
    try {
      const res = await invokeInterfaceInfoUsingPOST({
        id: params.id,
        ...values
      });
      setInvokeRes(res.data);
      notification.success({
        message: '请求成功',
      });
    } catch (error: any) {
      notification.error({
        message: '请求失败. 请重试!',
      });
    }
    setInvokeLoading(false);
  };

  useEffect(() => {
    loadData();
  }, []);

  return (
    <PageContainer title="在线接口文档">
      <Card loading={loading}>
        {data ? (
          <Descriptions title={data?.name} column={2}>
            <Descriptions.Item label="接口状态">
              {data.status === 0 ? '未发布' : '发布'}
            </Descriptions.Item>
            <Descriptions.Item label="描述">{data.description}</Descriptions.Item>
            <Descriptions.Item label="请求地址">{data.url}</Descriptions.Item>
            <Descriptions.Item label="请求类型">{data.method}</Descriptions.Item>
            <Descriptions.Item label="请求参数">{data.requestParams}</Descriptions.Item>
            <Descriptions.Item label="请求头">{data.requestHeader}</Descriptions.Item>
            <Descriptions.Item label="响应头">{data.responseHeader}</Descriptions.Item>
            <Descriptions.Item label="创建时间">{data.createTime}</Descriptions.Item>
            <Descriptions.Item label="更新时间">{data.updateTime}</Descriptions.Item>
          </Descriptions>
        ) : (
          <Empty />
        )}
      </Card>
      <Divider />
      <Card title="在线调试">
        <Form name="invoke" onFinish={onFinish} layout="vertical">
          <Form.Item label="请求参数" name="userRequestParams">
            <Input.TextArea />
          </Form.Item>
          <Form.Item wrapperCol={{ span: 16 }}>
            <Button type="primary" htmlType="submit">
              调用
            </Button>
          </Form.Item>
        </Form>
      </Card>
      <Divider />
      <Card title="返回结果" loading={invokeLoading}>
        {
          invokeRes
        }
      </Card>
    </PageContainer>
  );
};

export default Index;
