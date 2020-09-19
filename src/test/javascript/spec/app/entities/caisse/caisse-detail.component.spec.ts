import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AppironTestModule } from '../../../test.module';
import { CaisseDetailComponent } from 'app/entities/caisse/caisse-detail.component';
import { Caisse } from 'app/shared/model/caisse.model';

describe('Component Tests', () => {
  describe('Caisse Management Detail Component', () => {
    let comp: CaisseDetailComponent;
    let fixture: ComponentFixture<CaisseDetailComponent>;
    const route = ({ data: of({ caisse: new Caisse('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AppironTestModule],
        declarations: [CaisseDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(CaisseDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CaisseDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load caisse on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.caisse).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });
  });
});
