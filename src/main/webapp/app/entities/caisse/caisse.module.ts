import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AppironSharedModule } from 'app/shared/shared.module';
import { CaisseComponent } from './caisse.component';
import { CaisseDetailComponent } from './caisse-detail.component';
import { CaisseUpdateComponent } from './caisse-update.component';
import { CaisseDeleteDialogComponent } from './caisse-delete-dialog.component';
import { caisseRoute } from './caisse.route';

@NgModule({
  imports: [AppironSharedModule, RouterModule.forChild(caisseRoute)],
  declarations: [CaisseComponent, CaisseDetailComponent, CaisseUpdateComponent, CaisseDeleteDialogComponent],
  entryComponents: [CaisseDeleteDialogComponent],
})
export class AppironCaisseModule {}
