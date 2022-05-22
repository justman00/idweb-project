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
import { useRouter } from 'next/router';
import styled from '@emotion/styled';
import { css } from '@emotion/react';
import { File } from '@sumup/icons';

import { Meta } from '../../components/Meta';
import Navigation from '../../components/Navigation';
import { IUser } from '../../components/Navigation/Navigation';
import { getUser } from '../../utils/getUser';

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
  authorId: string;
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
  const router = useRouter();

  const isUserAuthor = course.authorId === user.id;
  const isPublished = course.status === 'PUBLISHED';
  const badge = isPublished ? (
    <StyledBadge variant="success">Published</StyledBadge>
  ) : (
    <StyledBadge variant="notify">Draft</StyledBadge>
  );

  const onPublishOrDraft = async () => {
    const formData = new FormData();

    formData.append('status', isPublished ? 'draft' : 'published');

    try {
      const userToken = getCookie('userToken');

      await fetch(
        `http://idweb-project.westeurope.cloudapp.azure.com:8080/api/courses/${course.id}`,
        {
          method: 'PATCH',
          body: formData,
          headers: {
            Authorization: `Bearer ${userToken.toString()}`,
          },
        },
      );

      router.push('/create-courses');
    } catch (error) {
      console.error(error);
    }
  };

  const onDelete = async () => {
    try {
      const userToken = getCookie('userToken');

      await fetch(
        `http://idweb-project.westeurope.cloudapp.azure.com:8080/api/courses/${course.id}`,
        {
          method: 'DELETE',
          headers: {
            Authorization: `Bearer ${userToken.toString()}`,
          },
        },
      );
      router.push('/create-courses');
    } catch (error) {
      console.error(error);
    }
  };

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
                  onClick={onPublishOrDraft}
                  type="button"
                  variant={isPublished ? 'secondary' : 'primary'}
                >
                  Convert to {isPublished ? 'draft' : 'published'} state
                </Button>
                <Button
                  onClick={onDelete}
                  type="button"
                  variant="primary"
                  destructive
                >
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

  try {
    const user = await getUser(userToken.toString());

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

    return {
      props: {
        user,
        course: data,
      },
    };
  } catch (error) {
    console.error(error);
    return {
      redirect: {
        destination: '/login',
        permanent: false,
      },
    };
  }
};
