import { Moment } from 'moment';
import { IEmployee } from 'app/shared/model/employee.model';
import { Month } from 'app/shared/model/enumerations/month.model';

export interface IPayment {
  id?: string;
  date?: Moment;
  avance?: number;
  month?: Month;
  year?: number;
  employees?: IEmployee[];
}

export class Payment implements IPayment {
  constructor(
    public id?: string,
    public date?: Moment,
    public avance?: number,
    public month?: Month,
    public year?: number,
    public employees?: IEmployee[]
  ) {}
}
