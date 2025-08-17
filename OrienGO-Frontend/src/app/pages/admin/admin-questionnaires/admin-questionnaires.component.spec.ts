import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminQuestionnairesComponent } from './admin-questionnaires.component';

describe('AdminQuestionnairesComponent', () => {
  let component: AdminQuestionnairesComponent;
  let fixture: ComponentFixture<AdminQuestionnairesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminQuestionnairesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminQuestionnairesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
