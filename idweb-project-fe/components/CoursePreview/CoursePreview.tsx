import { css } from '@emotion/react';
import styled from '@emotion/styled';
import { Badge, Body, Card, Headline } from '@sumup/circuit-ui';
import Link from 'next/link';
import React from 'react';

export interface ICoursePreview {
  title: string;
  description: string;
  thumbnail: string;
  date: string;
  authorName: string;
  href: string;
  status?: string;
}

const StyledCard = styled(Card)(
  () => css`
    width: 100%;
    padding: 0;
    overflow: hidden;
    border: none;
    padding-bottom: 12px;
    height: 310px;
  `,
);

export const CoursePreview: React.FC<ICoursePreview> = ({
  authorName,
  date,
  description,
  href,
  thumbnail,
  title,
  status,
}) => (
  <Link href={href}>
    <a
      css={css`
        color: unset;
        text-decoration: none;
        cursor: pointer;
      `}
    >
      <StyledCard>
        <img
          css={css`
            height: 100px;
            margin-bottom: 24px;
          `}
          src={thumbnail}
          alt="something"
        />
        <Headline as="h2" size="four">
          {title}
        </Headline>
        <Body
          css={css`
            overflow: hidden;
            max-height: 50px;
            -webkit-box-orient: vertical;
            display: block;
            display: -webkit-box;
            overflow: hidden !important;
            text-overflow: ellipsis;
            -webkit-line-clamp: 2;
          `}
        >
          {description}
        </Body>
        <div
          css={css`
            display: flex;
          `}
        >
          <Body
            css={css`
              padding-right: 2px;
            `}
            variant="highlight"
            size="two"
          >
            {authorName}
          </Body>
          <Body size="two" variant="subtle">
            • {date}
          </Body>
        </div>
        <div>
          {
            // eslint-disable-next-line no-nested-ternary
            status ? (
              status === 'published' ? (
                <Badge
                  css={css`
                    display: inline;
                  `}
                  variant="success"
                >
                  Published
                </Badge>
              ) : (
                <Badge variant="notify">Draft</Badge>
              )
            ) : null
          }
        </div>
      </StyledCard>
    </a>
  </Link>
);
