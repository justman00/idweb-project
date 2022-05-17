import { css } from '@emotion/react';
import {
  TopNavigation,
  ModalProvider,
  SideNavigation,
} from '@sumup/circuit-ui';
import { Add, Bitcoin, Like, Play } from '@sumup/icons';
import { removeCookies } from 'cookies-next';
import Link from 'next/link';
import { useRouter } from 'next/router';
import React, { useState } from 'react';

export interface IUser {
  email: string;
  id: string;
}

export const Navigation: React.FC<{ user: IUser }> = ({ children, user }) => {
  const router = useRouter();
  const [isSideNavigationOpen, setSideNavigationOpen] = useState(false);
  const hamburger = {
    activeLabel: 'Close side navigation',
    inactiveLabel: 'Open side navigation',
    isActive: isSideNavigationOpen,
    onClick: () => setSideNavigationOpen((prev) => !prev),
  };

  return (
    <ModalProvider>
      <TopNavigation
        logo={
          <Link href="/">
            <a>
              <Bitcoin />
            </a>
          </Link>
        }
        links={[
          {
            href: '/create-courses',
            icon: Add,
            label: 'Create courses',
          },
        ]}
        hamburger={hamburger}
        user={{
          name: user.email,
          id: user.id,
        }}
        profileMenu={{
          label: 'Open profile menu',
          actions: [
            {
              onClick: () => {
                removeCookies('userToken');
                router.push('/login');
              },
              children: 'Logout',
              destructive: true,
            },
          ],
        }}
      />
      <div
        css={css`
          display: flex;
        `}
      >
        <SideNavigation
          closeButtonLabel="hey"
          primaryLinks={[
            {
              icon: Play,
              label: 'All Courses',
              href: '/',
            },
            {
              icon: Like,
              label: 'My courses',
              href: '/my-courses',
            },
          ]}
          primaryNavigationLabel="hey"
          secondaryNavigationLabel="wassup"
          isOpen={isSideNavigationOpen}
          onClose={() => setSideNavigationOpen(false)}
        />
        <div
          css={css`
            margin: 1.5rem;
            width: 100%;
          `}
        >
          <div
            css={css`
              width: 100%;
              margin: 0 auto;
            `}
          >
            {children}
          </div>
        </div>
      </div>
    </ModalProvider>
  );
};
