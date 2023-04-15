import {PageContainer} from '@ant-design/pro-components';
import {List, message} from 'antd';
import React, {useEffect, useState} from 'react';
import {listInterfaceInfoByPageUsingGET} from '@/services/iceapi-backend/interfaceInfoController';

/**
 * 主页
 * @constructor
 */
const Index: React.FC = () => {
  const [loading, setLoading] = useState(false);
  const [list, setList] = useState<API.InterfaceInfo[]>([]);
  const [interfaceTotal, setInterfaceTotal] = useState<number>();

  const loadData = async (current = 1, pageSize = 10) => {
    setLoading(true);
    try {
      const res = await listInterfaceInfoByPageUsingGET({
        current,pageSize
      });
      setList(res?.data?.records ?? []);
      setInterfaceTotal(res?.data?.total ?? 0);
    } catch (error: any) {
      message.error('加载失败. 请重试!');
    }
    setLoading(false);
  };

  useEffect(() => {
    loadData();
  }, []);

  return (
    <PageContainer title="在线接口开放平台">
      <List
        className="myList"
        loading={loading}
        itemLayout="horizontal"
        dataSource={list}
        renderItem={(item) => (
          <List.Item>
            <List.Item.Meta
              title={<a href={`/interface_info/${item.id}`}>{item.name}</a>}
              description={item.description}
            />
          </List.Item>
        )}
        pagination={{
          pageSize: 10,
          onChange(page, pageSize) {
            loadData(page, pageSize);
          },
          showTotal(total: number) {
            return "总条数:" + total;
          },
          total: interfaceTotal
        }}
      />
    </PageContainer>
  );
};

export default Index;
