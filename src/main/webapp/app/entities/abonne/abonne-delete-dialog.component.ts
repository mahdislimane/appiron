import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAbonne } from 'app/shared/model/abonne.model';
import { AbonneService } from './abonne.service';

@Component({
  templateUrl: './abonne-delete-dialog.component.html',
})
export class AbonneDeleteDialogComponent {
  abonne?: IAbonne;

  constructor(protected abonneService: AbonneService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.abonneService.delete(id).subscribe(() => {
      this.eventManager.broadcast('abonneListModification');
      this.activeModal.close();
    });
  }
}
