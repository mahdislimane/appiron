import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IAbonnement, Abonnement } from 'app/shared/model/abonnement.model';
import { AbonnementService } from './abonnement.service';
import { AbonneService } from '../abonne/abonne.service';
import { Caisse, ICaisse } from 'app/shared/model/caisse.model';
import { DaylyType } from 'app/shared/model/enumerations/dayly-type.model';
import { CaisseService } from '../caisse/caisse.service';
import { EmployeeService } from '../employee/employee.service';

@Component({
  selector: 'jhi-abonnement-update',
  templateUrl: './abonnement-update.component.html',
})
export class AbonnementUpdateComponent implements OnInit {
  isSaving = false;
  currentAbon: any;
  oldAbon: any;
  employees: any;
  allAbonnements: IAbonnement[] | null = null;
  oldAbonnement: IAbonnement | null = null;
  newValue: any;

  editForm = this.fb.group({
    id: [],
    departement: [null, Validators.required],
    abType: [null, Validators.required],
    date: [null, Validators.required],
    price: [null, Validators.required],
    pay: [null, Validators.required],
    abonnes: [],
    utilisateur: [],
  });

  constructor(
    protected abonnementService: AbonnementService,
    protected abonneService: AbonneService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private caisseService: CaisseService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ abonnement }) => {
      this.oldAbon = abonnement;
      this.employeeService.query().subscribe(data => {
        this.employees = data.body;
      });
      this.abonnementService.query().subscribe(data => {
        this.allAbonnements = data.body;
      });
      if (!abonnement.departement && abonnement) {
        this.abonneService.find(abonnement).subscribe(data => {
          this.currentAbon = data.body;
        });
      } else if (!abonnement.id) {
        const today = moment().startOf('day');
        abonnement.date = today;
        this.updateForm(abonnement);
      } else {
        this.updateForm(abonnement);
      }
    });
  }

  updateForm(abonnement: IAbonnement): void {
    this.editForm.patchValue({
      id: abonnement.id,
      departement: abonnement.departement,
      abType: abonnement.abType,
      date: abonnement.date ? abonnement.date.format(DATE_TIME_FORMAT) : null,
      price: abonnement.price,
      pay: abonnement.pay,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const abonnement = this.createFromForm();
    if (abonnement.id !== undefined && abonnement.id !== null) {
      this.allAbonnements?.map(el => {
        if (el.id === abonnement.id) {
          this.oldAbonnement = el;
        }
      });
      if (abonnement?.pay && this.oldAbonnement?.pay) {
        this.newValue = abonnement?.pay - this.oldAbonnement?.pay;
      }
      const caisse = {
        ...new Caisse(),
        id: undefined,
        date: abonnement.date,
        valeur: this.newValue,
        daylyType: DaylyType.ABONNEMENT,
        operateur: this.editForm.get(['utilisateur'])!.value,
      };

      this.subscribeToSaveCaisse(this.caisseService.create(caisse));
      this.subscribeToSaveResponse(this.abonnementService.update(abonnement));
    } else {
      this.subscribeToSaveResponse(this.abonnementService.create(abonnement));
      const caisse = {
        ...new Caisse(),
        id: undefined,
        date: abonnement.date,
        valeur: abonnement.pay,
        daylyType: DaylyType.ABONNEMENT,
        operateur: this.editForm.get(['utilisateur'])!.value,
      };
      console.error('caisse');
      console.error(caisse);
      this.subscribeToSaveCaisse(this.caisseService.create(caisse));
    }
  }

  private createFromForm(): IAbonnement {
    return {
      ...new Abonnement(),
      id: this.editForm.get(['id'])!.value,
      departement: this.editForm.get(['departement'])!.value,
      abType: this.editForm.get(['abType'])!.value,
      date: this.editForm.get(['date'])!.value ? moment(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      price: this.editForm.get(['price'])!.value,
      pay: this.editForm.get(['pay'])!.value,
      abonnes: this.currentAbon ? this.currentAbon : this.oldAbon.abonnes,
    };
  }

  protected subscribeToSaveCaisse(result: Observable<HttpResponse<ICaisse>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }
  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAbonnement>>): void {
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
