import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAbonnement, Abonnement } from 'app/shared/model/abonnement.model';
import { AbonnementService } from './abonnement.service';
import { AbonnementComponent } from './abonnement.component';
import { AbonnementDetailComponent } from './abonnement-detail.component';
import { AbonnementUpdateComponent } from './abonnement-update.component';

@Injectable({ providedIn: 'root' })
export class AbonnementResolve implements Resolve<IAbonnement> {
  constructor(private service: AbonnementService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAbonnement> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      if (route.routeConfig && route.routeConfig.path === ':id/newAB') {
        return of(id);
      } else {
        return this.service.find(id).pipe(
          flatMap((abonnement: HttpResponse<Abonnement>) => {
            if (abonnement.body) {
              return of(abonnement.body);
            } else {
              this.router.navigate(['404']);
              return EMPTY;
            }
          })
        );
      }
    }
    return of(new Abonnement());
  }
}

export const abonnementRoute: Routes = [
  {
    path: '',
    component: AbonnementComponent,
    data: {
      authorities: [Authority.ADMIN],
      defaultSort: 'id,asc',
      pageTitle: 'appironApp.abonnement.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AbonnementDetailComponent,
    resolve: {
      abonnement: AbonnementResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'appironApp.abonnement.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AbonnementUpdateComponent,
    resolve: {
      abonnement: AbonnementResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'appironApp.abonnement.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AbonnementUpdateComponent,
    resolve: {
      abonnement: AbonnementResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'appironApp.abonnement.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/newAB',
    component: AbonnementUpdateComponent,
    resolve: {
      abonnement: AbonnementResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'appironApp.abonnement.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
