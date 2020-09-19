import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AppironTestModule } from '../../../test.module';
import { AbonneDetailComponent } from 'app/entities/abonne/abonne-detail.component';
import { Abonne } from 'app/shared/model/abonne.model';

describe('Component Tests', () => {
  describe('Abonne Management Detail Component', () => {
    let comp: AbonneDetailComponent;
    let fixture: ComponentFixture<AbonneDetailComponent>;
    const route = ({ data: of({ abonne: new Abonne('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AppironTestModule],
        declarations: [AbonneDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(AbonneDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AbonneDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load abonne on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.abonne).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });
  });
});
