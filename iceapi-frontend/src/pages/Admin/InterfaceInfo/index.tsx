import type {ActionType, ProColumns, ProDescriptionsItemProps} from '@ant-design/pro-components';
import {FooterToolbar, PageContainer, ProDescriptions, ProTable,} from '@ant-design/pro-components';
import '@umijs/max';
import {Button, Drawer, notification, Spin} from 'antd';
import React, {useRef, useState} from 'react';
import {SortOrder} from 'antd/es/table/interface';
import {
  deleteInterfaceInfoUsingPOST,
  listInterfaceInfoByPageUsingGET,
  offlineInterfaceInfoUsingPOST,
  onlineInterfaceInfoUsingPOST,
} from '@/services/iceapi-backend/interfaceInfoController';
import CreateDrawer from '@/pages/Admin/InterfaceInfo/components/CreateDrawer';
import UpdateDrawer from '@/pages/Admin/InterfaceInfo/components/UpdateDrawer';

const InterfaceInfo: React.FC = () => {
  const [showDetail, setShowDetail] = useState<boolean>(false);
  const actionRef = useRef<ActionType>();
  const [currentRow, setCurrentRow] = useState<API.InterfaceInfo>();
  const [selectedRowsState, setSelectedRows] = useState<API.InterfaceInfo[]>([]);
  const key = 'message';

  /**
   *  Delete node
   * @zh-CN 删除节点
   *
   * @param selectedRows
   */
  const handleRemove = async (values: number[]) => {
    notification.open({
      key,
      message: '正在删除',
      icon: <Spin/>,
      duration: null,
    });
    if (!values) return true;
    try {
      await deleteInterfaceInfoUsingPOST({
        ids: values,
      });
      notification.success({
        key,
        message: '删除成功',
      });
      actionRef.current?.reload();
      return true;
    } catch (error: any) {
      notification.error({
        key,
        message: '删除失败',
        description: error.message,
      });
      return false;
    }
  };

  /**
   *  Delete node
   * @zh-CN 发布接口
   *
   * @param selectedRows
   */
  const handleOnline = async (value: API.IdRequest) => {
    notification.open({
      key,
      message: '发布中',
      icon: <Spin/>,
    });
    if (!value) return true;
    try {
      await onlineInterfaceInfoUsingPOST({
        id: value.id,
      });
      notification.success({
        key,
        message: '发布成功',
      });
      actionRef.current?.reload();
      return true;
    } catch (error: any) {
      notification.error({
        key,
        message: '发布失败',
        description: error.message,
      });
      return false;
    }
  };

  /**
   *  Delete node
   * @zh-CN 下线接口
   *
   * @param selectedRows
   */
  const handleOffline = async (value: API.IdRequest) => {
    notification.open({
      key,
      message: '下线中',
      icon: <Spin/>,
      duration: null,
    });
    if (!value) return true;
    try {
      await offlineInterfaceInfoUsingPOST({
        id: value.id,
      });
      notification.success({
        key,
        message: '下线成功',
      });
      actionRef.current?.reload();
      return true;
    } catch (error: any) {
      notification.error({
        key,
        message: '下线失败',
        description: error.message,
      });
      return false;
    }
  };

  /**
   *  Array node
   * @zh-CN 遍历节点
   *
   * @param selectedRows
   */
  const handleTurnNum = (values: any) => {
    return values.map(Number);
  };

  // @ts-ignore
  /**
   * @en-US International configuration
   * @zh-CN 国际化配置
   * */

  const columns: ProColumns<API.InterfaceInfo>[] = [
    {
      title: 'id',
      dataIndex: 'id',
      valueType: 'index',
      hideInTable: true,
    },
    {
      title: '接口名称',
      dataIndex: 'name',
      valueType: 'text',
      ellipsis: true
    },
    {
      title: '描述',
      dataIndex: 'description',
      valueType: 'textarea',
      ellipsis: true
    },
    {
      title: '请求方法',
      dataIndex: 'method',
      valueType: 'text',
      ellipsis: true
    },
    {
      title: '接口地址',
      dataIndex: 'url',
      valueType: 'text',
      ellipsis: true
    },
    {
      title: '请求参数',
      dataIndex: 'requestParams',
      valueType: 'jsonCode',
      width: 200
    },
    {
      title: '请求头',
      dataIndex: 'requestHeader',
      valueType: 'jsonCode',
      width: 200
    },
    {
      title: '响应头',
      dataIndex: 'responseHeader',
      valueType: 'jsonCode',
      width: 200
    },
    {
      title: '状态',
      dataIndex: 'status',
      valueEnum: {
        0: {
          text: '关闭',
          status: 'Default',
        },
        1: {
          text: '开启',
          status: 'Processing',
        },
      },
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      valueType: 'dateTime',
      ellipsis: true
    },
    {
      title: '更新时间',
      dataIndex: 'updateTime',
      valueType: 'dateTime',
      ellipsis: true
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => [
        // <a
        //   key="updateConfig"
        //   onClick={() => {
        //     handleUpdateModalVisible(true);
        //     setCurrentRow(record);
        //   }}
        // >
        //   修改
        // </a>
        <UpdateDrawer value={record}/>,
        record.status === 0 ? (
          <a
            key="onlineConfig"
            onClick={async () => {
              await handleOnline(record);
            }}
          >
            发布
          </a>
        ) : null,
        record.status === 1 ? (
          <a
            style={{color: 'red'}}
            key="offlineConfig"
            onClick={async () => {
              await handleOffline(record);
            }}
          >
            下线
          </a>
        ) : null,
        <a
          style={{color: 'red'}}
          key="deleteConfig"
          onClick={async () => {
            const ids: any = [record.id];
            await handleRemove(ids);
          }}
        >
          删除
        </a>,
      ],
    },
  ];
  return (
    <PageContainer>
      <ProTable<API.listInterfaceInfoByPageUsingGETParams>
        headerTitle={'接口信息'}
        actionRef={actionRef}
        rowKey={(record) => {
          return record.id;
        }}
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [<CreateDrawer/>]}
        request={async (
          params,
          sort: Record<string, SortOrder>,
          filter: Record<string, React.ReactText[] | null>,
        ) => {
          const res: API.BaseResponsePageInterfaceInfo = await listInterfaceInfoByPageUsingGET({
            ...params,
          });
          if (res?.data) {
            return {
              data: res.data.records || [],
              success: true,
              total: res.data.total,
            };
          } else {
            return {
              data: [],
              success: false,
              total: 0,
            };
          }
        }}
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
              <a
                style={{
                  fontWeight: 600,
                }}
              >
                {selectedRowsState.length}
              </a>{' '}
              项
            </div>
          }
        >
          <Button
            onClick={async () => {
              const ids = selectedRowsState.map((item) => item.id);
              await handleRemove(handleTurnNum(ids));
              setSelectedRows([]);
              actionRef.current?.reloadAndRest?.();
            }}
          >
            批量删除
          </Button>
        </FooterToolbar>
      )}
      <Drawer
        width={600}
        visible={showDetail}
        onClose={() => {
          setCurrentRow(undefined);
          setShowDetail(false);
        }}
        closable={false}
      >
        {currentRow?.name && (
          <ProDescriptions<API.RuleListItem>
            column={2}
            title={currentRow?.name}
            request={async () => ({
              data: currentRow || {},
            })}
            params={{
              id: currentRow?.name,
            }}
            columns={columns as ProDescriptionsItemProps<API.RuleListItem>[]}
          />
        )}
      </Drawer>
    </PageContainer>
  );
};
export default InterfaceInfo;
