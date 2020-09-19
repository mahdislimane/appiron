import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IPayment, Payment } from 'app/shared/model/payment.model';
import { PaymentService } from './payment.service';
import { EmployeeService } from '../employee/employee.service';
import { CaisseService } from '../caisse/caisse.service';
import { Caisse, ICaisse } from 'app/shared/model/caisse.model';
import { DaylyType } from 'app/shared/model/enumerations/dayly-type.model';

@Component({
  selector: 'jhi-payment-update',
  templateUrl: './payment-update.component.html',
})
export class PaymentUpdateComponent implements OnInit {
  isSaving = false;
  currentEmpl: any;
  giverEmployee: any;
  oldPay: any;
  allPayments: IPayment[] | null = null;
  oldPayment: IPayment | null = null;
  newValue: any;

  editForm = this.fb.group({
    id: [],
    date: [null, Validators.required],
    avance: [null, Validators.required],
    month: [null, Validators.required],
    year: [null, Validators.required],
    payments: [null, Validators.required],
    utilisateur: [null, Validators.required],
  });

  constructor(
    protected employeeService: EmployeeService,
    protected paymentService: PaymentService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private caisseService: CaisseService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ payment }) => {
      this.oldPay = payment;
      this.employeeService.query().subscribe(data => {
        this.giverEmployee = data.body;
      });
      this.paymentService.query().subscribe(data => {
        this.allPayments = data.body;
      });
      if (!payment.departement && payment) {
        this.employeeService.find(payment).subscribe(data => {
          this.currentEmpl = data.body;
        });
      } else if (!payment.id) {
        const today = moment().startOf('day');
        payment.date = today;
        this.updateForm(payment);
      } else {
        this.updateForm(payment);
      }
    });
  }

  updateForm(payment: IPayment): void {
    this.editForm.patchValue({
      id: payment.id,
      date: payment.date ? payment.date.format(DATE_TIME_FORMAT) : null,
      avance: payment.avance,
      month: payment.month,
      year: payment.year,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const payment = this.createFromForm();
    if (payment.id !== undefined && payment.id !== null) {
      this.allPayments?.map(el => {
        if (el.id === payment.id) {
          this.oldPayment = el;
        }
      });
      if (payment?.avance && this.oldPayment?.avance) {
        this.newValue = payment?.avance - this.oldPayment?.avance;
      }
      const caisse = {
        ...new Caisse(),
        id: undefined,
        date: payment.date,
        valeur: this.newValue,
        daylyType: DaylyType.SALARY,
        operateur: this.editForm.get(['utilisateur'])!.value,
      };
      this.subscribeToSaveCaisse(this.caisseService.create(caisse));
      this.subscribeToSaveResponse(this.paymentService.update(payment));
    } else {
      this.subscribeToSaveResponse(this.paymentService.create(payment));
      const caisse = {
        ...new Caisse(),
        id: undefined,
        date: payment.date,
        valeur: payment.avance,
        daylyType: DaylyType.SALARY,
        operateur: this.editForm.get(['utilisateur'])!.value,
      };
      this.subscribeToSaveCaisse(this.caisseService.create(caisse));
    }
  }

  private createFromForm(): IPayment {
    return {
      ...new Payment(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? moment(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      avance: this.editForm.get(['avance'])!.value,
      month: this.editForm.get(['month'])!.value,
      year: this.editForm.get(['year'])!.value,
      employees: this.currentEmpl ? this.currentEmpl : this.oldPay.employees,
    };
  }

  protected subscribeToSaveCaisse(result: Observable<HttpResponse<ICaisse>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }
  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPayment>>): void {
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
