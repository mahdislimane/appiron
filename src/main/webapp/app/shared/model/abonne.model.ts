import { IAbonnement } from 'app/shared/model/abonnement.model';

export interface IAbonne {
  id?: string;
  firstName?: string;
  lastName?: string;
  phoneNumber?: string;
  cin?: string;
  card?: string;
  abonnements?: IAbonnement;
}

export class Abonne implements IAbonne {
  constructor(
    public id?: string,
    public firstName?: string,
    public lastName?: string,
    public phoneNumber?: string,
    public cin?: string,
    public card?: string,
    public abonnements?: IAbonnement
  ) {}
}
