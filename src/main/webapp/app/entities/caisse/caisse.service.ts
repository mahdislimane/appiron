import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { ICaisse } from 'app/shared/model/caisse.model';

type EntityResponseType = HttpResponse<ICaisse>;
type EntityArrayResponseType = HttpResponse<ICaisse[]>;

@Injectable({ providedIn: 'root' })
export class CaisseService {
  public resourceUrl = SERVER_API_URL + 'api/caisses';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/caisses';

  constructor(protected http: HttpClient) {}

  create(caisse: ICaisse): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(caisse);
    return this.http
      .post<ICaisse>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(caisse: ICaisse): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(caisse);
    return this.http
      .put<ICaisse>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<ICaisse>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICaisse[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICaisse[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(caisse: ICaisse): ICaisse {
    const copy: ICaisse = Object.assign({}, caisse, {
      date: caisse.date && caisse.date.isValid() ? caisse.date.toJSON() : undefined,
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
      res.body.forEach((caisse: ICaisse) => {
        caisse.date = caisse.date ? moment(caisse.date) : undefined;
      });
    }
    return res;
  }
}
