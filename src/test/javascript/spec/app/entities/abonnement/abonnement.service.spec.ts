import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { AbonnementService } from 'app/entities/abonnement/abonnement.service';
import { IAbonnement, Abonnement } from 'app/shared/model/abonnement.model';
import { Departement } from 'app/shared/model/enumerations/departement.model';
import { AbType } from 'app/shared/model/enumerations/ab-type.model';

describe('Service Tests', () => {
  describe('Abonnement Service', () => {
    let injector: TestBed;
    let service: AbonnementService;
    let httpMock: HttpTestingController;
    let elemDefault: IAbonnement;
    let expectedResult: IAbonnement | IAbonnement[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(AbonnementService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Abonnement('ID', Departement.MUSCULATION, AbType.SEANCE, currentDate, 0, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            date: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find('123').subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Abonnement', () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
            date: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.create(new Abonnement()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Abonnement', () => {
        const returnedFromService = Object.assign(
          {
            departement: 'BBBBBB',
            abType: 'BBBBBB',
            date: currentDate.format(DATE_TIME_FORMAT),
            price: 1,
            pay: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Abonnement', () => {
        const returnedFromService = Object.assign(
          {
            departement: 'BBBBBB',
            abType: 'BBBBBB',
            date: currentDate.format(DATE_TIME_FORMAT),
            price: 1,
            pay: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Abonnement', () => {
        service.delete('123').subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
