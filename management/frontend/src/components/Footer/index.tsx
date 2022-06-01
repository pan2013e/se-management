import { GithubOutlined, BookOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-layout';
import React from "react";

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
                        <span>
                            <GithubOutlined /> Github
                        </span>
                    ),
                    href: 'https://github.com/pan2013e/se-management',
                    blankTarget: true,
                },
                {
                    key: 'docs',
                    title: (
                        <span>
                            <BookOutlined /> Documentation
                        </span>
                    ),
                    href: 'https://zjuse-2022.github.io/docs/',
                    blankTarget: true,
                }
            ]}
        />
    );
};

export default Footer;
