import React from 'react';
import { GetServerSideProps, NextPage } from 'next';
import { getCookie } from 'cookies-next';

import { Meta } from '../components/Meta';
import Navigation from '../components/Navigation';
import { AllCourses } from '../components/AllCourses/AllCourses';
import { ICoursePreview } from '../components/CoursePreview/CoursePreview';
import { IUser } from '../components/Navigation/Navigation';

const title = 'Welcome to SumUp Next.js';

const Page: NextPage<{ courses: ICoursePreview[]; user: IUser }> = ({
  courses,
  user,
}) => (
  <>
    <Meta title={title} path="/" />
    <main>
      <Navigation user={user}>
        <AllCourses courses={courses} />
      </Navigation>
    </main>
  </>
);

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

  // TODO: endpoint for get user
  try {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment
    const { data }: { data: ICoursePreview[] } = await fetch(
      'http://idweb-project.westeurope.cloudapp.azure.com:8080/api/courses',
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
        courses: data
          ? data.filter((obj) => obj.status && obj.status === 'PUBLISHED')
          : [],
      },
    };
  } catch (error) {
    console.error(error);
    return {
      props: {},
    };
  }
};
