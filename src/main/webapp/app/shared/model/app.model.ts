export interface IApp {
  id?: number;
  name?: string;
  codename?: string;
  banidAndroid?: string | null;
  banidIos?: string | null;
  logoContentType?: string | null;
  logo?: string | null;
}

export const defaultValue: Readonly<IApp> = {};
