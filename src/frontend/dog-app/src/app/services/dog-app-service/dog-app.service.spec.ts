import { TestBed } from '@angular/core/testing';

import { DogAppService } from './dog-app.service';

describe('DogAppService', () => {
  let service: DogAppService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DogAppService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
