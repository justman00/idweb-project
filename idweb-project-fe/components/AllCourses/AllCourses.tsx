import { css } from '@emotion/react';
import styled from '@emotion/styled';
import { Col, Grid, Row } from '@sumup/circuit-ui';
import React from 'react';

import { CoursePreview, ICoursePreview } from '../CoursePreview/CoursePreview';
import { Hero } from '../Hero/Hero';

interface Props {
  courses: ICoursePreview[];
}

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

export const AllCourses: React.FC<Props> = ({ courses }) => (
  <div>
    <Hero title="Immerse into one of our courses" />

    <StyledGrid>
      <StyledRow>
        {courses.map((coursePreview) => (
          <StyledCol>
            <CoursePreview {...coursePreview} />
          </StyledCol>
        ))}
      </StyledRow>
    </StyledGrid>
  </div>
);
