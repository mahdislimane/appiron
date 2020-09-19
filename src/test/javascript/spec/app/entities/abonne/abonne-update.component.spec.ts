import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AppironTestModule } from '../../../test.module';
import { AbonneUpdateComponent } from 'app/entities/abonne/abonne-update.component';
import { AbonneService } from 'app/entities/abonne/abonne.service';
import { Abonne } from 'app/shared/model/abonne.model';

describe('Component Tests', () => {
  describe('Abonne Management Update Component', () => {
    let comp: AbonneUpdateComponent;
    let fixture: ComponentFixture<AbonneUpdateComponent>;
    let service: AbonneService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AppironTestModule],
        declarations: [AbonneUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(AbonneUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AbonneUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AbonneService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Abonne('123');
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Abonne();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
