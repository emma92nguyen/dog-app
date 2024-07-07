import { TestBed } from '@angular/core/testing';

import { DogAppAnalyticsService } from './dog-app-analytics.service';

describe('DogAppAnalyticsService', () => {
  let service: DogAppAnalyticsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DogAppAnalyticsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
