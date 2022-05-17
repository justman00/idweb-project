import React from 'react';
import { GetServerSideProps, NextPage } from 'next';
import { getCookie } from 'cookies-next';
import { Card, Col, Grid, Headline, Row } from '@sumup/circuit-ui';
import styled from '@emotion/styled';
import { css } from '@emotion/react';
import { Plus } from '@sumup/icons';
import Link from 'next/link';

import { Meta } from '../components/Meta';
import Navigation from '../components/Navigation';
import {
  CoursePreview,
  ICoursePreview,
} from '../components/CoursePreview/CoursePreview';
import { IUser } from '../components/Navigation/Navigation';

const title = 'Welcome to SumUp Next.js';

const StyledRow = styled(Row)(
  () => css`
    margin: 1rem 0;
  `,
);

const StyledCol = styled(Col)(
  () => css`
    margin: 0.5rem;
    width: 250px;
  `,
);

const StyledGrid = styled(Grid)(
  () => css`
    max-width: unset !important;
    padding: 0;
  `,
);

const StyledAddCard = styled(Card)(
  () => css`
    width: 100%;
    padding: 0;
    overflow: hidden;
    border: none;
    padding-bottom: 12px;
    height: 300px;
  `,
);

const Page: NextPage<{ courses: ICoursePreview[]; user: IUser }> = ({
  courses,
  user,
}) => (
  <>
    <Meta title={title} path="/create-courses" />
    <main>
      <Navigation user={user}>
        <Headline as="h1" size="one">
          Your place to edit and create masterful courses.
        </Headline>
        <StyledGrid>
          <StyledRow>
            <Link href="/create-course">
              <a
                css={css`
                  color: unset;
                  text-decoration: none;
                  cursor: pointer;
                `}
              >
                <StyledCol>
                  <StyledAddCard
                    css={css`
                      display: flex;
                      justify-content: center;
                      align-items: center;
                      cursor: pointer;

                      &:hover {
                        svg {
                          color: #999;
                        }
                      }
                    `}
                  >
                    <Plus
                      css={css`
                        width: 50%;
                        height: 50%;
                        color: #ccc;
                        transition: color 0.4s;
                      `}
                    />
                  </StyledAddCard>
                </StyledCol>
              </a>
            </Link>
            {courses.map((coursePreview) => (
              <StyledCol>
                <CoursePreview {...coursePreview} />
              </StyledCol>
            ))}
          </StyledRow>
        </StyledGrid>
      </Navigation>
    </main>
  </>
);

export default Page;

const coursePreviews: ICoursePreview[] = [
  {
    authorName: 'Vlad',
    date: 'Apr 13, 2022',
    description:
      'This is a very long and insightful description of one of the courses. Another sentence is here to show how insightful this can be.',
    href: '/courses/learn-go-deeply',
    thumbnail:
      'https://www.freecodecamp.org/news/content/images/2021/10/golang.png',
    title: 'Learn Go Deeply',
    status: 'published',
  },
  {
    authorName: 'Vlad',
    date: 'Mar 10, 2022',
    description:
      'This is a very long and insightful description of one of the courses. Another sentence is here to show how insightful this can be.',
    href: '/courses/advanced-go',
    thumbnail:
      'https://upload.wikimedia.org/wikipedia/commons/thumb/0/05/Go_Logo_Blue.svg/1200px-Go_Logo_Blue.svg.png',
    title: 'Advanced Go',
    status: 'published',
  },
  {
    authorName: 'Vlad',
    date: 'Jan 23, 2022',
    description:
      'This is a very long and insightful description of one of the courses. Another sentence is here to show how insightful this can be.',
    href: '/courses/master-javascript',
    thumbnail:
      'https://www.cloudsavvyit.com/p/uploads/2021/02/c123ee3a.jpg?width=1198&trim=1,1&bg-color=000&pad=1,1',
    title: 'Master JavaScript',
    status: 'draft',
  },
];

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
      courses: coursePreviews,
    },
  };
};
