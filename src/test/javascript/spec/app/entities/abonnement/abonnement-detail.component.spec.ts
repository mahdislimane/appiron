import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AppironTestModule } from '../../../test.module';
import { AbonnementDetailComponent } from 'app/entities/abonnement/abonnement-detail.component';
import { Abonnement } from 'app/shared/model/abonnement.model';

describe('Component Tests', () => {
  describe('Abonnement Management Detail Component', () => {
    let comp: AbonnementDetailComponent;
    let fixture: ComponentFixture<AbonnementDetailComponent>;
    const route = ({ data: of({ abonnement: new Abonnement('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AppironTestModule],
        declarations: [AbonnementDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(AbonnementDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AbonnementDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load abonnement on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.abonnement).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });
  });
});
