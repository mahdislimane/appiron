import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AppironSharedModule } from 'app/shared/shared.module';
import { AbonnementComponent } from './abonnement.component';
import { AbonnementDetailComponent } from './abonnement-detail.component';
import { AbonnementUpdateComponent } from './abonnement-update.component';
import { AbonnementDeleteDialogComponent } from './abonnement-delete-dialog.component';
import { abonnementRoute } from './abonnement.route';

@NgModule({
  imports: [AppironSharedModule, RouterModule.forChild(abonnementRoute)],
  declarations: [AbonnementComponent, AbonnementDetailComponent, AbonnementUpdateComponent, AbonnementDeleteDialogComponent],
  entryComponents: [AbonnementDeleteDialogComponent],
})
export class AppironAbonnementModule {}
