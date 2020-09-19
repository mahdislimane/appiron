import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IAbonnement } from 'app/shared/model/abonnement.model';

type EntityResponseType = HttpResponse<IAbonnement>;
type EntityArrayResponseType = HttpResponse<IAbonnement[]>;

@Injectable({ providedIn: 'root' })
export class AbonnementService {
  public resourceUrl = SERVER_API_URL + 'api/abonnements';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/abonnements';

  constructor(protected http: HttpClient) {}

  create(abonnement: IAbonnement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(abonnement);
    return this.http
      .post<IAbonnement>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(abonnement: IAbonnement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(abonnement);
    return this.http
      .put<IAbonnement>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<IAbonnement>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAbonnement[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAbonnement[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(abonnement: IAbonnement): IAbonnement {
    const copy: IAbonnement = Object.assign({}, abonnement, {
      date: abonnement.date && abonnement.date.isValid() ? abonnement.date.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? moment(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((abonnement: IAbonnement) => {
        abonnement.date = abonnement.date ? moment(abonnement.date) : undefined;
      });
    }
    return res;
  }
}
