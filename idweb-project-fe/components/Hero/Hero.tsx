import { css } from '@emotion/react';
import { Headline } from '@sumup/circuit-ui';
import React from 'react';

interface Props {
  title: string;
}

export const Hero: React.FC<Props> = ({ title }) => {
  console.log('hi');
  return (
    <>
      <Headline as="h1" size="one">
        {title}
      </Headline>
      <div
        css={css`
          background-color: lightgrey;
          width: 100%;
          min-height: 350px;
          border-radius: 1rem;
          background-image: url(https://images.unsplash.com/photo-1552793494-111afe03d0ca?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&w=1920&q=80);
          background-size: cover;
          background-repeat: no-repeat;
          background-position: center;
        `}
      />
    </>
  );
};
