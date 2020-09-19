import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAbonnement } from 'app/shared/model/abonnement.model';
import { AbonnementService } from './abonnement.service';

@Component({
  templateUrl: './abonnement-delete-dialog.component.html',
})
export class AbonnementDeleteDialogComponent {
  abonnement?: IAbonnement;

  constructor(
    protected abonnementService: AbonnementService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.abonnementService.delete(id).subscribe(() => {
      this.eventManager.broadcast('abonnementListModification');
      this.activeModal.close();
    });
  }
}
