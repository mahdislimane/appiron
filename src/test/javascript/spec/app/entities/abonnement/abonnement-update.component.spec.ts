import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AppironTestModule } from '../../../test.module';
import { AbonnementUpdateComponent } from 'app/entities/abonnement/abonnement-update.component';
import { AbonnementService } from 'app/entities/abonnement/abonnement.service';
import { Abonnement } from 'app/shared/model/abonnement.model';

describe('Component Tests', () => {
  describe('Abonnement Management Update Component', () => {
    let comp: AbonnementUpdateComponent;
    let fixture: ComponentFixture<AbonnementUpdateComponent>;
    let service: AbonnementService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AppironTestModule],
        declarations: [AbonnementUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(AbonnementUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AbonnementUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AbonnementService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Abonnement('123');
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
        const entity = new Abonnement();
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
