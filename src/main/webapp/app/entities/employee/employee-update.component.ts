import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IEmployee, Employee } from 'app/shared/model/employee.model';
import { EmployeeService } from './employee.service';
import { IPayment } from 'app/shared/model/payment.model';
import { PaymentService } from 'app/entities/payment/payment.service';

@Component({
  selector: 'jhi-employee-update',
  templateUrl: './employee-update.component.html',
})
export class EmployeeUpdateComponent implements OnInit {
  isSaving = false;
  payments: IPayment[] = [];
  currentEmployee: any;
  paymentsEmployee: any = [];

  editForm = this.fb.group({
    id: [],
    fullName: [null, Validators.required],
    jobTitle: [null, Validators.required],
    phoneNumber: [null, Validators.required],
    cin: [null, Validators.required],
    hireDate: [null, Validators.required],
    salary: [],
    endDate: [],
    payments: [],
  });

  constructor(
    protected employeeService: EmployeeService,
    protected paymentService: PaymentService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employee }) => {
      this.updateForm(employee);
      this.currentEmployee = employee;
      this.paymentService.query().subscribe((res: HttpResponse<IPayment[]>) => {
        this.payments = res.body || [];
        this.payments.map(el => {
          if (el.employees) {
            this.paymentsEmployee.push(el);
          }
        });
      });
    });
  }

  updateForm(employee: IEmployee): void {
    this.editForm.patchValue({
      id: employee.id,
      fullName: employee.fullName,
      jobTitle: employee.jobTitle,
      phoneNumber: employee.phoneNumber,
      cin: employee.cin,
      hireDate: employee.hireDate ? employee.hireDate.format(DATE_TIME_FORMAT) : null,
      salary: employee.salary,
      endDate: employee.endDate ? employee.endDate.format(DATE_TIME_FORMAT) : null,
      payments: employee.payments,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employee = this.createFromForm();
    if (employee.id !== undefined) {
      this.subscribeToSaveResponse(this.employeeService.update(employee));
    } else {
      this.subscribeToSaveResponse(this.employeeService.create(employee));
    }
  }

  private createFromForm(): IEmployee {
    return {
      ...new Employee(),
      id: this.editForm.get(['id'])!.value,
      fullName: this.editForm.get(['fullName'])!.value,
      jobTitle: this.editForm.get(['jobTitle'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      cin: this.editForm.get(['cin'])!.value,
      hireDate: this.editForm.get(['hireDate'])!.value ? moment(this.editForm.get(['hireDate'])!.value, DATE_TIME_FORMAT) : undefined,
      salary: this.editForm.get(['salary'])!.value,
      endDate: this.editForm.get(['endDate'])!.value ? moment(this.editForm.get(['endDate'])!.value, DATE_TIME_FORMAT) : undefined,
      payments: this.editForm.get(['payments'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployee>>): void {
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

  trackById(index: number, item: IPayment): any {
    return item.id;
  }
}
