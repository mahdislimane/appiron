import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAbonne, Abonne } from 'app/shared/model/abonne.model';
import { AbonneService } from './abonne.service';
import { AbonneComponent } from './abonne.component';
import { AbonneDetailComponent } from './abonne-detail.component';
import { AbonneUpdateComponent } from './abonne-update.component';

@Injectable({ providedIn: 'root' })
export class AbonneResolve implements Resolve<IAbonne> {
  constructor(private service: AbonneService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAbonne> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((abonne: HttpResponse<Abonne>) => {
          console.error(route);
          if (abonne.body) {
            if (route.routeConfig?.path === ':id/newAB') {
              // delete abonne.body.id;
              return of(abonne.body);
            } else {
              return of(abonne.body);
            }
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Abonne());
  }
}

export const abonneRoute: Routes = [
  {
    path: '',
    component: AbonneComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'appironApp.abonne.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AbonneDetailComponent,
    resolve: {
      abonne: AbonneResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'appironApp.abonne.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AbonneUpdateComponent,
    resolve: {
      abonne: AbonneResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'appironApp.abonne.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AbonneUpdateComponent,
    resolve: {
      abonne: AbonneResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'appironApp.abonne.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/newAB',
    component: AbonneUpdateComponent,
    resolve: {
      abonne: AbonneResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'appironApp.abonne.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
