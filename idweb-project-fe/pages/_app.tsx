import React, { FC } from 'react';
import { AppProps } from 'next/app';
import { ThemeProvider, CacheProvider } from '@emotion/react';
import { cache } from '@emotion/css';
import { BaseStyles } from '@sumup/circuit-ui';
import { light } from '@sumup/design-tokens';

const App = ({ Component, pageProps }: AppProps): JSX.Element => {
  const Comp = Component as FC;

  return (
    <CacheProvider value={cache}>
      <ThemeProvider theme={light}>
        <BaseStyles />
        <Comp {...pageProps} />
      </ThemeProvider>
    </CacheProvider>
  );
};

export default App;
