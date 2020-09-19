import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AppironTestModule } from '../../../test.module';
import { CaisseUpdateComponent } from 'app/entities/caisse/caisse-update.component';
import { CaisseService } from 'app/entities/caisse/caisse.service';
import { Caisse } from 'app/shared/model/caisse.model';

describe('Component Tests', () => {
  describe('Caisse Management Update Component', () => {
    let comp: CaisseUpdateComponent;
    let fixture: ComponentFixture<CaisseUpdateComponent>;
    let service: CaisseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AppironTestModule],
        declarations: [CaisseUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(CaisseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CaisseUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CaisseService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Caisse('123');
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
        const entity = new Caisse();
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
