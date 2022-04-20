import dayjs from 'dayjs';
import { IApp } from 'app/shared/model/app.model';

export interface IAppPromo {
  id?: number;
  title?: string;
  text?: string;
  date?: string;
  banid?: string;
  rate?: number;
  app?: IApp;
}

export const defaultValue: Readonly<IAppPromo> = {};
