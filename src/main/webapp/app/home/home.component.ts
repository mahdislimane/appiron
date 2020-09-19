import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';

import { LoginModalService } from 'app/core/login/login-modal.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { CaisseService } from 'app/entities/caisse/caisse.service';
import { Caisse } from 'app/shared/model/caisse.model';
import { AbonnementService } from 'app/entities/abonnement/abonnement.service';
import { Abonnement } from 'app/shared/model/abonnement.model';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  authSubscription?: Subscription;
  chosenDate1: Date = new Date();
  chosenDate2: Date = new Date();
  chosenDate3: Date = new Date();
  caisseData: Caisse[] = [];
  caisseOfDay: Caisse[] = [];
  caisseOfDayValue = 0;
  caisseOfDayIncome = 0;
  caisseOfDayOutcome = 0;
  abonnements: Abonnement[] = [];
  abonnementsOfMonth: Abonnement[] = [];
  depMUSCULATION = 0;
  depKICKBOXING = 0;
  depTAEKWONDO = 0;
  depBOX = 0;
  depDANCE = 0;
  depGYMNASTIQUE = 0;
  depKARATE = 0;
  depJIUJITSU = 0;
  depJUDO = 0;

  constructor(
    private abonnementService: AbonnementService,
    private accountService: AccountService,
    private loginModalService: LoginModalService,
    private caisseService: CaisseService
  ) {}

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
    this.caisseService.query().subscribe(data => {
      if (data.body) {
        data.body.map(el => {
          if (el) {
            this.caisseData.push(el);
          }
        });
      }
    });
    this.abonnementService.query().subscribe(data => {
      if (data.body) {
        data.body.map(el => {
          if (el) {
            this.abonnements.push(el);
          }
        });
      }
    });
  }

  search2(date: Date): void {
    this.abonnementsOfMonth = [];
    this.depMUSCULATION = 0;
    this.depKICKBOXING = 0;
    this.depTAEKWONDO = 0;
    this.depBOX = 0;
    this.depDANCE = 0;
    this.depGYMNASTIQUE = 0;
    this.depKARATE = 0;
    this.depJIUJITSU = 0;
    this.depJUDO = 0;
    this.abonnements.map(el => {
      if (el.date?.get('M') === new Date(date).getMonth()) {
        this.abonnementsOfMonth.push(el);
      }
    });
    this.abonnementsOfMonth.map(el => {
      if (el.departement === 'MUSCULATION' && el.pay) {
        this.depMUSCULATION += el.pay;
      } else if (el.departement === 'KICKBOXING' && el.pay) {
        this.depKICKBOXING += el.pay;
      } else if (el.departement === 'TAEKWONDO' && el.pay) {
        this.depTAEKWONDO += el.pay;
      } else if (el.departement === 'BOX' && el.pay) {
        this.depBOX += el.pay;
      } else if (el.departement === 'DANCE' && el.pay) {
        this.depDANCE += el.pay;
      } else if (el.departement === 'GYMNASTIQUE' && el.pay) {
        this.depGYMNASTIQUE += el.pay;
      } else if (el.departement === 'KARATE' && el.pay) {
        this.depKARATE += el.pay;
      } else if (el.departement === 'JIUJITSU' && el.pay) {
        this.depJIUJITSU += el.pay;
      } else if (el.departement === 'JUDO' && el.pay) {
        this.depJUDO += el.pay;
      }
    });
  }

  search(date: Date): void {
    this.caisseOfDayValue = 0;
    this.caisseOfDayIncome = 0;
    this.caisseOfDayOutcome = 0;
    this.caisseOfDay = [];
    this.caisseData.map(el => {
      if (
        el.date?.get('D') === new Date(date).getDate() &&
        el.date?.get('M') === new Date(date).getMonth() &&
        el.date?.get('y') === new Date(date).getFullYear()
      ) {
        this.caisseOfDay.push(el);
        if (el.valeur && (el.daylyType === 'ABONNEMENT' || el.daylyType === 'OTHERS_INCOM')) {
          this.caisseOfDayIncome += el.valeur;
        }
        if (el.valeur && (el.daylyType === 'SALARY' || el.daylyType === 'CHARGE')) {
          this.caisseOfDayOutcome += el.valeur;
        }
      }
    });
    this.caisseOfDayValue = this.caisseOfDayIncome - this.caisseOfDayOutcome;
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.loginModalService.open();
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }
}
