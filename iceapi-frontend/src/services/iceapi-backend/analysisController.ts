// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** listTopInterfaceInvoke GET /api/analysis/top/interface/invoke */
export async function listTopInterfaceInvokeUsingGET(options?: { [key: string]: any }) {
  return request<API.BaseResponseListInterfaceInfoVO>('/api/analysis/top/interface/invoke', {
    method: 'GET',
    ...(options || {}),
  });
}
