import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICaisse, Caisse } from 'app/shared/model/caisse.model';
import { CaisseService } from './caisse.service';
import { CaisseComponent } from './caisse.component';
import { CaisseDetailComponent } from './caisse-detail.component';
import { CaisseUpdateComponent } from './caisse-update.component';

@Injectable({ providedIn: 'root' })
export class CaisseResolve implements Resolve<ICaisse> {
  constructor(private service: CaisseService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICaisse> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((caisse: HttpResponse<Caisse>) => {
          if (caisse.body) {
            return of(caisse.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Caisse());
  }
}

export const caisseRoute: Routes = [
  {
    path: '',
    component: CaisseComponent,
    data: {
      authorities: [Authority.ADMIN],
      defaultSort: 'id,asc',
      pageTitle: 'appironApp.caisse.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CaisseDetailComponent,
    resolve: {
      caisse: CaisseResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'appironApp.caisse.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CaisseUpdateComponent,
    resolve: {
      caisse: CaisseResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'appironApp.caisse.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CaisseUpdateComponent,
    resolve: {
      caisse: CaisseResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'appironApp.caisse.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
