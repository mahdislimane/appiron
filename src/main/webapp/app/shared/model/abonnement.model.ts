import { Moment } from 'moment';
import { IAbonne } from 'app/shared/model/abonne.model';
import { Departement } from 'app/shared/model/enumerations/departement.model';
import { AbType } from 'app/shared/model/enumerations/ab-type.model';

export interface IAbonnement {
  id?: string;
  departement?: Departement;
  abType?: AbType;
  date?: Moment;
  price?: number;
  pay?: number;
  abonnes?: IAbonne[];
}

export class Abonnement implements IAbonnement {
  constructor(
    public id?: string,
    public departement?: Departement,
    public abType?: AbType,
    public date?: Moment,
    public price?: number,
    public pay?: number,
    public abonnes?: IAbonne[]
  ) {}
}
