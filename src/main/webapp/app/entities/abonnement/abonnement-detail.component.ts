import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAbonnement } from 'app/shared/model/abonnement.model';

@Component({
  selector: 'jhi-abonnement-detail',
  templateUrl: './abonnement-detail.component.html',
})
export class AbonnementDetailComponent implements OnInit {
  abonnement: IAbonnement | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ abonnement }) => (this.abonnement = abonnement));
  }

  previousState(): void {
    window.history.back();
  }
}
