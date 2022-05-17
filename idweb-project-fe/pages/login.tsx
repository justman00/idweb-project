import React from 'react';
import { NextPage } from 'next';
import { css } from '@emotion/react';
import styled from '@emotion/styled';
import {
  Card,
  Headline,
  Body,
  Anchor,
  cx,
  spacing,
  Input,
  Button,
  NotificationInline,
} from '@sumup/circuit-ui';
import { useRouter } from 'next/router';
import Link from 'next/link';
import { setCookies } from 'cookies-next';
import { Bitcoin } from '@sumup/icons';

import { Meta } from '../components/Meta';

const Main = styled('main')(
  ({ theme }) => css`
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    max-width: 450px;
    margin: 0 auto ${theme.spacings.mega};
  `,
);

const centeredStyles = css`
  text-align: center;
`;

const title = 'Login into the application';

const Page: NextPage = () => {
  const router = useRouter();
  const [err, setErr] = React.useState('');

  const onSubmit: React.FormEventHandler<HTMLFormElement> = async (e) => {
    e.preventDefault();
    // do API request

    const formData = new FormData(e.target as HTMLFormElement);

    const formBody: string[] = [];
    formData.forEach((val, key) => {
      const encodedKey = encodeURIComponent(key);
      const encodedValue = encodeURIComponent(val.toString());
      formBody.push(`${encodedKey}=${encodedValue}`);
    });

    try {
      // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment
      const { accessToken }: { accessToken: string } = await fetch(
        'http://idweb-project.westeurope.cloudapp.azure.com:8080/login',
        {
          body: formBody.join('&'),
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
          },
          method: 'POST',
        },
      ).then((res) => res.json());

      setCookies('userToken', { accessToken });
      router.push('/');
    } catch (error) {
      setErr('Something went wrong. Please try again');
    }
  };

  return (
    <>
      <Meta title={title} path="/login" />
      <Main>
        <Bitcoin />
        <Card>
          <Headline
            size="one"
            as="h1"
            noMargin
            css={cx(centeredStyles, spacing({ bottom: 'giga' }))}
          >
            {title}
          </Headline>

          <form onSubmit={onSubmit}>
            <Input name="username" type="email" required label="Email" />
            <Input name="password" type="password" required label="Password" />
            <Button type="submit" variant="primary">
              Submit
            </Button>
          </form>

          <Body
            noMargin
            css={css`
              margin-top: 24px;
              margin-bottom: 24px;
            `}
          >
            Don't have an account yet?{' '}
            <Link href="/register">
              <Anchor>Sign up here.</Anchor>
            </Link>
          </Body>
          {!!err && <NotificationInline body={err} variant="alert" />}
        </Card>
      </Main>
    </>
  );
};

export default Page;
