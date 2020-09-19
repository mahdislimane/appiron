import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'payment',
        loadChildren: () => import('./payment/payment.module').then(m => m.AppironPaymentModule),
      },
      {
        path: 'employee',
        loadChildren: () => import('./employee/employee.module').then(m => m.AppironEmployeeModule),
      },
      {
        path: 'abonnement',
        loadChildren: () => import('./abonnement/abonnement.module').then(m => m.AppironAbonnementModule),
      },
      {
        path: 'caisse',
        loadChildren: () => import('./caisse/caisse.module').then(m => m.AppironCaisseModule),
      },
      {
        path: 'abonne',
        loadChildren: () => import('./abonne/abonne.module').then(m => m.AppironAbonneModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class AppironEntityModule {}
