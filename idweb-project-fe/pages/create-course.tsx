import React, { useState } from 'react';
import { useRouter } from 'next/router';
import { GetServerSideProps, NextPage } from 'next';
import { getCookie } from 'cookies-next';
import {
  Avatar,
  Button,
  Headline,
  ImageInput,
  Input,
  TextArea,
} from '@sumup/circuit-ui';
import { css } from '@emotion/react';
import { Delete, Plus } from '@sumup/icons';

import { Meta } from '../components/Meta';
import Navigation from '../components/Navigation';
import { IUser } from '../components/Navigation/Navigation';
import { getUser } from '../utils/getUser';

const title = 'Welcome to SumUp Next.js';

interface IChapter {
  title: string;
  description: string;
  attachements: string[];
}

const Page: NextPage<{ user: IUser }> = ({ user }) => {
  const [chapters, setChapters] = useState<IChapter[]>([]);
  const router = useRouter();

  const onAddChapter: React.MouseEventHandler<HTMLDivElement> = () => {
    setChapters([
      ...chapters,
      { title: '', description: '', attachements: [] },
    ]);
  };

  const createOnDeleteChapterHandler = (idx) => () => {
    const filteredChapters = chapters.filter((chap, i) => i !== idx);
    setChapters(filteredChapters);
  };

  const handleSubmit: React.FormEventHandler<HTMLFormElement> = async (e) => {
    e.preventDefault();

    const formData = new FormData(e.target as HTMLFormElement);

    try {
      const userToken = getCookie('userToken');

      // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment
      await fetch(
        'http://idweb-project.westeurope.cloudapp.azure.com:8080/api/courses',
        {
          method: 'POST',
          body: formData,
          headers: {
            Authorization: `Bearer ${userToken as string}`,
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
      <Meta title={title} path="/create-course" />
      <main>
        <Navigation user={user}>
          <Headline as="h1" size="one">
            Create a course
          </Headline>

          <form
            css={css`
              margin-bottom: 48px;
            `}
            onSubmit={handleSubmit}
          >
            <Input name="courseTitle" label="Course title" required />
            <TextArea
              name="courseDescription"
              label="Course description"
              required
            />
            <ImageInput
              required
              name="thumbnail"
              label="Thumbnail"
              component={Avatar}
              clearButtonLabel="clear"
              loadingLabel="loading"
              onChange={() => {}}
              onClear={() => {}}
            />

            <Headline as="h2" size="two">
              Create one or more chapters
            </Headline>

            <div>
              {chapters.map((chapter, idx) => (
                <div
                  css={css`
                    padding: 24px 0 12px;
                    border-bottom: 1px solid #ccc;
                    margin-bottom: 12px;
                  `}
                >
                  <div
                    css={css`
                      display: flex;
                    `}
                  >
                    <Headline as="h3" size="three">
                      Chapter #{idx + 1}
                    </Headline>
                    <Delete
                      onClick={createOnDeleteChapterHandler(idx)}
                      css={css`
                        cursor: pointer;
                        color: red;
                        margin-left: 16px;
                      `}
                    />
                  </div>
                  <Input
                    name={`chapter-title-${idx}`}
                    label="Chapter title"
                    required
                  />
                  <TextArea
                    name={`chapter-description-${idx}`}
                    label="Chapter description"
                    required
                  />
                </div>
              ))}
              <div
                onClick={onAddChapter}
                css={css`
                  display: flex;
                  align-items: center;
                  cursor: pointer;
                  color: #3063e9;
                  font-weight: 700;
                  margin-bottom: 32px;
                `}
              >
                <span
                  css={css`
                    margin-right: 8px;
                  `}
                >
                  Add a chapter
                </span>
                <Plus />
              </div>
            </div>

            <Button type="submit" variant="primary">
              Submit
            </Button>
          </form>
        </Navigation>
      </main>
    </>
  );
};

export default Page;

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

  try {
    const user = await getUser(userToken as string);

    return {
      props: {
        user,
      },
    };
  } catch (error) {
    return {
      redirect: {
        destination: '/login',
        permanent: false,
      },
    };
  }
};
