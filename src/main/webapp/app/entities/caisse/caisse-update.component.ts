import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ICaisse, Caisse } from 'app/shared/model/caisse.model';
import { CaisseService } from './caisse.service';

@Component({
  selector: 'jhi-caisse-update',
  templateUrl: './caisse-update.component.html',
})
export class CaisseUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    date: [],
    valeur: [],
    daylyType: [],
    operateur: [],
  });

  constructor(protected caisseService: CaisseService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ caisse }) => {
      if (!caisse.id) {
        const today = moment().startOf('day');
        caisse.date = today;
      }

      this.updateForm(caisse);
    });
  }

  updateForm(caisse: ICaisse): void {
    this.editForm.patchValue({
      id: caisse.id,
      date: caisse.date ? caisse.date.format(DATE_TIME_FORMAT) : null,
      valeur: caisse.valeur,
      daylyType: caisse.daylyType,
      operateur: caisse.operateur,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const caisse = this.createFromForm();
    if (caisse.id !== undefined) {
      this.subscribeToSaveResponse(this.caisseService.update(caisse));
    } else {
      this.subscribeToSaveResponse(this.caisseService.create(caisse));
    }
  }

  private createFromForm(): ICaisse {
    return {
      ...new Caisse(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? moment(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      valeur: this.editForm.get(['valeur'])!.value,
      daylyType: this.editForm.get(['daylyType'])!.value,
      operateur: this.editForm.get(['operateur'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICaisse>>): void {
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
}
