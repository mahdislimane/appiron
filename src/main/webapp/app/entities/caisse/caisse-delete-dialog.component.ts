import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICaisse } from 'app/shared/model/caisse.model';
import { CaisseService } from './caisse.service';

@Component({
  templateUrl: './caisse-delete-dialog.component.html',
})
export class CaisseDeleteDialogComponent {
  caisse?: ICaisse;

  constructor(protected caisseService: CaisseService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.caisseService.delete(id).subscribe(() => {
      this.eventManager.broadcast('caisseListModification');
      this.activeModal.close();
    });
  }
}
