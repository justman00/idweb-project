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

const title = 'Create a profile';

const Page: NextPage = () => {
  const router = useRouter();
  const [err, setErr] = React.useState('');

  const onSubmit: React.FormEventHandler<HTMLFormElement> = async (e) => {
    e.preventDefault();
    // do API request
    const formData = new FormData(e.target as HTMLFormElement);

    const objectData = {};
    formData.forEach((value, key) => {
      objectData[key] = value;
    });
    const jsonData = JSON.stringify(objectData);

    const formBody: string[] = [];
    formData.forEach((val, key) => {
      let encodedKey: string;
      if (key === 'email') {
        encodedKey = encodeURIComponent('username');
      } else {
        encodedKey = encodeURIComponent(key);
      }
      const encodedValue = encodeURIComponent(val.toString());
      formBody.push(`${encodedKey}=${encodedValue}`);
    });

    try {
      await fetch(
        'http://idweb-project.westeurope.cloudapp.azure.com:8080/api/users',
        {
          method: 'POST',
          body: jsonData,
          headers: {
            'Content-Type': 'application/json',
          },
        },
      );

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

      setCookies('userToken', accessToken);
      router.push('/');
    } catch (error) {
      setErr('Something went wrong. Please try again');
    }
  };

  return (
    <>
      <Meta title={title} path="/register" />
      <Main>
        <Bitcoin />
        <Card
          css={css`
            min-width: 400px;
          `}
        >
          <Headline
            size="one"
            as="h1"
            noMargin
            css={cx(centeredStyles, spacing({ bottom: 'giga' }))}
          >
            {title}
          </Headline>
          <form onSubmit={onSubmit}>
            <Input name="firstName" type="text" required label="First Name" />
            <Input name="lastName" type="text" required label="Last Name" />
            <Input name="email" type="email" required label="Email" />
            <Input name="password" type="password" required label="Password" />
            <Input
              name="confirmPassword"
              type="password"
              required
              label="Confirm password"
            />
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
            Already have an account?{' '}
            <Link href="/login">
              <Anchor>Log in.</Anchor>
            </Link>
          </Body>{' '}
          {!!err && <NotificationInline body={err} variant="alert" />}
        </Card>
      </Main>
    </>
  );
};

export default Page;
