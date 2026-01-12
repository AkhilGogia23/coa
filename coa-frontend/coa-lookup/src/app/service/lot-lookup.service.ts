import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CoaResultItem {
  name: string;
  lotNumber: string;
  coaPdfUrl: string;
  passed: boolean;
}

export interface LotResponse {
  lotNumber: string;
  productName: string;
  sku: string;
  status: string;
  manufactureDate: string;
  expiryDate: string;
  results: {
    ingredients: CoaResultItem[];
    blend: CoaResultItem[];
    finishedProduct: CoaResultItem[];
  };
}

@Injectable({ providedIn: 'root' })
export class LotLookupService {
  private http = inject(HttpClient);
  private baseUrl = 'https://coa-production.up.railway.app/api/lots';

  getLot(lotNumber: string): Observable<LotResponse> {
    return this.http.get<LotResponse>(
      `${this.baseUrl}/${encodeURIComponent(lotNumber)}`
    );
  }
}
