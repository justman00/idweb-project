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
  },
  {
    authorName: 'Ksiusa',
    date: 'Feb 22, 2022',
    description:
      'This is a very long and insightful description of one of the courses. Another sentence is here to show how insightful this can be.',
    href: '/courses/sql-deep-dive',
    thumbnail:
      'https://i0.wp.com/learn.onemonth.com/wp-content/uploads/2019/07/image2-1.png?fit=600%2C315&ssl=1',
    title: 'SQL Deep Dive',
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
  },
  {
    authorName: 'Ksiusa',
    date: 'Jam 1, 2022',
    description:
      'This is a very long and insightful description of one of the courses. Another sentence is here to show how insightful this can be.',
    href: '/courses/get-good-at-java',
    thumbnail:
      'https://abac.software/wp-content/uploads/2021/09/og-social-java-logo.gif',
    title: 'Get good at Java',
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
