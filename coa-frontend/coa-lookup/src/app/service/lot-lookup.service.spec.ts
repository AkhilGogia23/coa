import { TestBed } from '@angular/core/testing';

import { LotLookupService } from './lot-lookup.service';

describe('LotLookupService', () => {
  let service: LotLookupService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LotLookupService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
