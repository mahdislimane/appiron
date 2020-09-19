import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, ParamMap, Router, Data } from '@angular/router';
import { Subscription, combineLatest } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAbonne } from 'app/shared/model/abonne.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { AbonneService } from './abonne.service';
import { AbonneDeleteDialogComponent } from './abonne-delete-dialog.component';
import { AbonnementService } from '../abonnement/abonnement.service';
import { IAbonnement } from 'app/shared/model/abonnement.model';

@Component({
  selector: 'jhi-abonne',
  templateUrl: './abonne.component.html',
})
export class AbonneComponent implements OnInit, OnDestroy {
  abonnes?: IAbonne[];
  difAbonnes?: IAbonne[];
  abonnements?: IAbonnement[];
  eventSubscriber?: Subscription;
  currentSearch: any;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  abonnementsEmployee: any = [];

  constructor(
    protected abonneService: AbonneService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected abonnementService: AbonnementService
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    if (this.currentSearch) {
      this.abonneService
        .search({
          page: pageToLoad - 1,
          query: this.currentSearch,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<IAbonne[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
          () => this.onError()
        );
      return;
    }

    this.abonneService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IAbonne[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
        () => this.onError()
      );
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadPage(1);
  }

  ngOnInit(): void {
    this.handleNavigation();
    this.registerChangeInAbonnes();
  }

  protected handleNavigation(): void {
    combineLatest(this.activatedRoute.data, this.activatedRoute.queryParamMap, (data: Data, params: ParamMap) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'asc';
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    }).subscribe();
  }

  rest(price: any, pay: any): any {
    return price - pay;
  }
  resetSearch(): any {
    this.currentSearch = '';
    this.searchAbonne();
  }
  searchAbonne(): any {
    if (this.currentSearch === '') {
      this.difAbonnes = this.abonnes;
    } else {
      this.difAbonnes = [];
      this.abonnes?.map(el => {
        if (
          el.firstName?.toUpperCase().includes(this.currentSearch.toUpperCase()) ||
          el.lastName?.toUpperCase().includes(this.currentSearch.toUpperCase()) ||
          el.cin?.includes(this.currentSearch)
        ) {
          this.difAbonnes?.push(el);
        }
        // el.firstName?.toUpperCase().includes(this.currentSearch.toUpperCase());
      });
    }
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IAbonne): string {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInAbonnes(): void {
    this.eventSubscriber = this.eventManager.subscribe('abonneListModification', () => this.loadPage());
  }

  delete(abonne: IAbonne): void {
    const modalRef = this.modalService.open(AbonneDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.abonne = abonne;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IAbonne[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.ngbPaginationPage = this.page;
    if (navigate) {
      this.router.navigate(['/abonne'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          search: this.currentSearch,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.abonnes = data || [];
    this.searchAbonne();
    this.abonnementService.query().subscribe((res: HttpResponse<IAbonnement[]>) => {
      this.abonnements = res.body || [];
      this.abonnements.map(el => {
        if (el.abonnes) {
          this.abonnementsEmployee.push(el);
        }
      });
    });
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
