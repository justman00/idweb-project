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
  Headline,
  Image,
  ListItemGroup,
} from '@sumup/circuit-ui';
import styled from '@emotion/styled';
import { css } from '@emotion/react';
import { File } from '@sumup/icons';

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
  id: number;
  title: string;
  description: string;
  thumbnail: string;
  date: string;
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
  const isPublished = course.status === 'PUBLISHED';
  const badge = isPublished ? (
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

export const getServerSideProps: GetServerSideProps = async ({
  req,
  res,
  params,
}) => {
  const userToken = getCookie('userToken', { req, res });

  if (!userToken) {
    return {
      redirect: {
        destination: '/login',
        permanent: false,
      },
    };
  }

  // TODO: endpoint for get user
  try {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment
    const { data }: { data: ICourse } = await fetch(
      `http://idweb-project.westeurope.cloudapp.azure.com:8080/api/courses/${
        params.id as string
      }`,
      {
        headers: {
          Authorization: `Bearer ${userToken.toString()}`,
        },
      },
    ).then((response) => response.json());

    console.log(data, userToken);

    return {
      props: {
        user: {
          email: 'blabla',
          id: 'fueiwbfuiwebfi3ubv',
        },
        course: data,
      },
    };
  } catch (error) {
    console.error(error);
    return {
      props: {},
    };
  }
};
