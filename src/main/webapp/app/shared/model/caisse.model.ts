import { Moment } from 'moment';
import { DaylyType } from 'app/shared/model/enumerations/dayly-type.model';

export interface ICaisse {
  id?: string;
  date?: Moment;
  valeur?: number;
  daylyType?: DaylyType;
  operateur?: string;
}

export class Caisse implements ICaisse {
  constructor(public id?: string, public date?: Moment, public valeur?: number, public daylyType?: DaylyType, public operateur?: string) {}
}
