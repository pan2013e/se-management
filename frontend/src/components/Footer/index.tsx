import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-layout';

const Footer: React.FC = () => {
    const defaultMessage = 'SE Group';

    const currentYear = new Date().getFullYear();

    return (
        <DefaultFooter
            copyright={`${currentYear} ${defaultMessage}`}
            links={[
                {
                    key: 'github',
                    title: (
                        <p>
                            <GithubOutlined /> Check out this project on Github
                        </p>
                    ),
                    href: 'https://github.com/pan2013e/se-management-frontend',
                    blankTarget: true,
                },
            ]}
        />
    );
};

export default Footer;
