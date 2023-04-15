import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import '@umijs/max';
const Footer: React.FC = () => {
  const defaultMessage = '冰块出品';
  const currentYear = new Date().getFullYear();
  return (
    <DefaultFooter
      style={{
        background: 'none',
      }}
      copyright={`${currentYear} ${defaultMessage}`}
      links={[
        {
          key: 'iceblock',
          title: 'iceblock',
          href: 'https://iceblock.site',
          blankTarget: true,
        },
        {
          key: 'github',
          title: <GithubOutlined />,
          href: 'https://github.com/myisiceblock',
          blankTarget: true,
        },
      ]}
    />
  );
};
export default Footer;
