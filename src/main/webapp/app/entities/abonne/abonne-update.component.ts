import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IAbonne, Abonne } from 'app/shared/model/abonne.model';
import { AbonneService } from './abonne.service';
import { IAbonnement } from 'app/shared/model/abonnement.model';
import { AbonnementService } from 'app/entities/abonnement/abonnement.service';

@Component({
  selector: 'jhi-abonne-update',
  templateUrl: './abonne-update.component.html',
})
export class AbonneUpdateComponent implements OnInit {
  isSaving = false;
  canChange = true;
  abonnements: IAbonnement[] = [];
  currentAbonne: any;
  abonnementsEmployee: any = [];

  editForm = this.fb.group({
    id: [],
    firstName: [null, Validators.required],
    lastName: [null, Validators.required],
    phoneNumber: [null, Validators.required],
    cin: [],
    card: [],
    abonnements: [],
  });

  constructor(
    protected abonneService: AbonneService,
    protected abonnementService: AbonnementService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    if (this.activatedRoute.snapshot.routeConfig?.path === ':id/newAB') {
      this.canChange = false;
    } else {
      this.canChange = true;
    }
    this.activatedRoute.data.subscribe(({ abonne }) => {
      this.updateForm(abonne);
      this.currentAbonne = abonne;
      this.abonnementService.query().subscribe((res: HttpResponse<IAbonnement[]>) => {
        this.abonnements = res.body || [];
        this.abonnements.map(el => {
          if (el.abonnes) {
            this.abonnementsEmployee.push(el);
          }
        });
      });
    });
  }

  updateForm(abonne: IAbonne): void {
    this.editForm.patchValue({
      id: abonne.id,
      firstName: abonne.firstName,
      lastName: abonne.lastName,
      phoneNumber: abonne.phoneNumber,
      cin: abonne.cin,
      card: abonne.card,
      abonnements: abonne.abonnements,
    });
  }

  rest(price: any, pay: any): any {
    return price - pay;
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const abonne = this.createFromForm();
    if (abonne.id !== undefined) {
      this.subscribeToSaveResponse(this.abonneService.update(abonne));
    } else {
      this.subscribeToSaveResponse(this.abonneService.create(abonne));
    }
  }

  private createFromForm(): IAbonne {
    return {
      ...new Abonne(),
      id: this.editForm.get(['id'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      cin: this.editForm.get(['cin'])!.value,
      card: this.editForm.get(['card'])!.value,
      abonnements: this.editForm.get(['abonnements'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAbonne>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IAbonnement): any {
    return item.id;
  }
}
