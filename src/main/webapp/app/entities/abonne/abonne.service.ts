import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IAbonne } from 'app/shared/model/abonne.model';

type EntityResponseType = HttpResponse<IAbonne>;
type EntityArrayResponseType = HttpResponse<IAbonne[]>;

@Injectable({ providedIn: 'root' })
export class AbonneService {
  public resourceUrl = SERVER_API_URL + 'api/abonnes';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/abonnes';

  constructor(protected http: HttpClient) {}

  create(abonne: IAbonne): Observable<EntityResponseType> {
    return this.http.post<IAbonne>(this.resourceUrl, abonne, { observe: 'response' });
  }

  update(abonne: IAbonne): Observable<EntityResponseType> {
    return this.http.put<IAbonne>(this.resourceUrl, abonne, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IAbonne>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAbonne[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAbonne[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
