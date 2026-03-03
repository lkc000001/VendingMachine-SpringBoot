import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowMessageComponent } from './show-message.component';

describe('ShowMessageComponent', () => {
  let component: ShowMessageComponent;
  let fixture: ComponentFixture<ShowMessageComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ShowMessageComponent]
    });
    fixture = TestBed.createComponent(ShowMessageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
