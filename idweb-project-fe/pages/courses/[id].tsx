import React from 'react';
import { GetServerSideProps, NextPage } from 'next';
import { getCookie } from 'cookies-next';
import {
  Anchor,
  Badge,
  Body,
  BodyLarge,
  Button,
  ButtonGroup,
  Card,
  Col,
  Grid,
  Headline,
  Image,
  ListItemGroup,
  Row,
} from '@sumup/circuit-ui';
import styled from '@emotion/styled';
import { css } from '@emotion/react';
import { File, Plus } from '@sumup/icons';
import Link from 'next/link';

import { Meta } from '../../components/Meta';
import Navigation from '../../components/Navigation';
import { IUser } from '../../components/Navigation/Navigation';

const title = 'Welcome to SumUp Next.js';

interface IAttachement {
  name: string;
  id: string;
  url: string;
}

interface IChapter {
  id: string;
  title: string;
  description: string;
  attachements: IAttachement[];
}

interface ICourse {
  id: string;
  title: string;
  description: string;
  thumbnail: string;
  date: string;
  authorName: string;
  authorID: string;
  href: string;
  status: string;
  chapters: IChapter[];
}

const StyledBadge = styled(Badge)(
  () => css`
    height: 26px;
    margin-left: 8px;
  `,
);

const Page: NextPage<{ course: ICourse; user: IUser }> = ({ course, user }) => {
  const isUserAuthor = course.authorID === user.id;
  const isPublished = course.status === 'published';
  const badge =
    course.status === 'published' ? (
      <StyledBadge variant="success">Published</StyledBadge>
    ) : (
      <StyledBadge variant="notify">Draft</StyledBadge>
    );

  return (
    <>
      <Meta title={title} path="/create-courses" />
      <main>
        <Navigation user={user}>
          <main
            css={css`
              max-width: 720px;
              margin: 0 auto;
              padding-bottom: 32px;
            `}
          >
            <div
              css={css`
                display: flex;
                align-items: baseline;
              `}
            >
              <Headline as="h1" size="one">
                {course.title}
              </Headline>
              {isUserAuthor ? badge : null}
            </div>
            <Image src={course.thumbnail} alt="something" />
            <BodyLarge
              css={css`
                margin: 24px 0;
              `}
            >
              {course.description}
            </BodyLarge>

            <Headline as="h2" size="two">
              Chapters
            </Headline>
            {course.chapters.map((chapter) => (
              <div
                css={css`
                  margin: 24px 0;
                  padding-bottom: 24px;
                  border-bottom: 1px solid #ccc;
                `}
              >
                <Headline as="h3" size="three">
                  {chapter.title}
                </Headline>
                <Body>{chapter.description}</Body>
                {chapter.attachements.length ? (
                  <ListItemGroup
                    label="Attachements"
                    items={chapter.attachements.map((att) => ({
                      key: att.id,
                      label: (
                        <Anchor
                          css={css`
                            color: #3063e9 !important;
                            font-weight: bold;
                          `}
                          href={att.url}
                          target="_blank"
                        >
                          <File
                            css={css`
                              margin-right: 8px;
                              color: #3063e9;
                            `}
                          />{' '}
                          {att.name}
                        </Anchor>
                      ),
                    }))}
                  />
                ) : null}
              </div>
            ))}
            {isUserAuthor && (
              <ButtonGroup>
                <Button
                  type="button"
                  variant={isPublished ? 'secondary' : 'primary'}
                >
                  Convert to {isPublished ? 'draft' : 'published'} state
                </Button>
                <Button type="button" variant="primary" destructive>
                  Delete course
                </Button>
              </ButtonGroup>
            )}
          </main>
        </Navigation>
      </main>
    </>
  );
};

export default Page;

const course: ICourse = {
  id: '4r34uiwfbwekfwe',
  authorName: 'Vlad',
  authorID: 'fueiwbfuiwebfi3ubv',
  date: 'Apr 13, 2022',
  description:
    'This is a very long and insightful description of one of the courses. Another sentence is here to show how insightful this can be.',
  href: '/courses/learn-go-deeply',
  thumbnail:
    'https://www.freecodecamp.org/news/content/images/2021/10/golang.png',
  title: 'Learn Go Deeply',
  status: 'published',
  chapters: [
    {
      id: 'dwevbfuiwbfuk3jfw',
      title: 'Get started with tooling',
      description:
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.',
      attachements: [
        {
          id: 'wfefiubw4ufi',
          name: 'Install Go',
          url: 'https://google.com',
        },
        {
          id: 'erbergberwg34gw34g',
          name: 'Go tooling',
          url: 'https://google.com',
        },
        {
          id: 'g34qgf4geghweg',
          name: 'Go compiler',
          url: 'https://google.com',
        },
        {
          id: 'f3uy4fvyu34fvyu3w4ge',
          name: 'Go linter',
          url: 'https://google.com',
        },
      ],
    },
    {
      id: 'v4gejkbrub4wuger',
      title: 'Syntax',
      description:
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.',
      attachements: [
        {
          id: 'verwgub4w3ugeb34g',
          name: 'Go syntax guide',
          url: 'https://google.com',
        },
      ],
    },
  ],
};

export const getServerSideProps: GetServerSideProps = async ({ req, res }) => {
  const userToken = getCookie('userToken', { req, res });

  if (!userToken) {
    return {
      redirect: {
        destination: '/login',
        permanent: false,
      },
    };
  }

  return {
    props: {
      user: {
        email: 'blabla',
        id: 'fueiwbfuiwebfi3ubv',
      },
      course,
    },
  };
};
