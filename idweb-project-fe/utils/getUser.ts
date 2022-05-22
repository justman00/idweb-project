import { IUser } from '../components/Navigation/Navigation';

export async function getUser(accessToken: string): Promise<IUser> {
  const res = await fetch(
    `http://idweb-project.westeurope.cloudapp.azure.com:8080/api/users?accessToken=${accessToken}`,
  );

  // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment
  const { data } = await res.json();

  console.log(data);

  return data as IUser;
}
