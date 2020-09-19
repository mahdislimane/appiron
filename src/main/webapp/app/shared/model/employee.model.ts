import { Moment } from 'moment';
import { IPayment } from 'app/shared/model/payment.model';
import { JobTitle } from 'app/shared/model/enumerations/job-title.model';

export interface IEmployee {
  id?: string;
  fullName?: string;
  jobTitle?: JobTitle;
  phoneNumber?: string;
  cin?: string;
  hireDate?: Moment;
  salary?: number;
  endDate?: Moment;
  payments?: IPayment;
}

export class Employee implements IEmployee {
  constructor(
    public id?: string,
    public fullName?: string,
    public jobTitle?: JobTitle,
    public phoneNumber?: string,
    public cin?: string,
    public hireDate?: Moment,
    public salary?: number,
    public endDate?: Moment,
    public payments?: IPayment
  ) {}
}
