module.exports = require('@sumup/foundry/eslint')(
  {
    language: 'TypeScript',
    environments: ['Browser', 'Node'],
  },
  {
    overrides: [
      {
        files: ['*/**/*.ts', '*/**/*.tsx'],
        rules: {
          '@typescript-eslint/require-await': 'off',
          '@typescript-eslint/no-floating-promises': 'off',
        },
      },
    ],
  },
);
