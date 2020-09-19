import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AppironSharedModule } from 'app/shared/shared.module';
import { AbonneComponent } from './abonne.component';
import { AbonneDetailComponent } from './abonne-detail.component';
import { AbonneUpdateComponent } from './abonne-update.component';
import { AbonneDeleteDialogComponent } from './abonne-delete-dialog.component';
import { abonneRoute } from './abonne.route';

@NgModule({
  imports: [AppironSharedModule, RouterModule.forChild(abonneRoute)],
  declarations: [AbonneComponent, AbonneDetailComponent, AbonneUpdateComponent, AbonneDeleteDialogComponent],
  entryComponents: [AbonneDeleteDialogComponent],
})
export class AppironAbonneModule {}
