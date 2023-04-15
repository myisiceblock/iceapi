import { Settings as LayoutSettings } from '@ant-design/pro-components';

/**
 * @name
 */
const Settings: LayoutSettings & {
  pwa?: boolean;
  logo?: string;
} = {
  navTheme: 'light',
  // 拂晓蓝
  colorPrimary: '#1890ff',
  layout: 'mix',
  contentWidth: 'Fluid',
  fixedHeader: false,
  fixSiderbar: true,
  colorWeak: false,
  title: '冰块Api开放平台',
  pwa: false,
  logo: 'https://iceblock-1304225940.cos.ap-guangzhou.myqcloud.com/FgoWoutYbTIq_-STzyICIFcitdEB.jpeg',
  iconfontUrl: '',
  splitMenus: false,
  siderMenuType: "sub"
};

export default Settings;
