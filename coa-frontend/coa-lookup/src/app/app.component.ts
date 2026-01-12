import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LotLookupService, LotResponse } from './service/lot-lookup.service';
import { HttpClient } from '@angular/common/http';
@Component({
  selector: 'app-root',
  imports: [CommonModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
  title = 'COA Lookup';
  private apiBase = 'https://coa-production.up.railway.app/api/lots';
  // 🔹 inject the service
  private lotService = inject(LotLookupService);

  lotNumber = '';
  loading = signal(false);
  error = signal<string | null>(null);
  result = signal<LotResponse | null>(null);
  constructor(private http: HttpClient) {}

  onSearch() {
    const lot = this.lotNumber.trim();
    if (!lot) {
      this.error.set('Please enter a lot number.');
      return;
    }

    this.error.set(null);
    this.result.set(null);
    this.loading.set(true);

    this.lotService.getLot(lot).subscribe({
      next: (data) => {
        this.loading.set(false);
        this.result.set(data);
      },
      error: () => {
        this.loading.set(false);
        this.error.set('No data found for that lot.');
      },
    });
  }
  // downloadCoa(item: any) {
  //   console.log('PDF URL:', item?.coaPdfUrl);
  //   if (!item?.coaPdfUrl) {
  //     console.error('Missing PDF URL for item:', item);
  //     return;
  //   }

  //   const params = { url: item.coaPdfUrl };

  //   this.http
  //     .get(`${this.apiBase}/download`, {
  //       params,
  //       responseType: 'blob',
  //     })
  //     .subscribe({
  //       next: (blob) => {
  //         const blobUrl = window.URL.createObjectURL(blob);
  //         const a = document.createElement('a');
  //         a.href = blobUrl;
  //         a.download = (item.name || 'COA') + '.pdf';
  //         a.click();
  //         URL.revokeObjectURL(blobUrl);
  //       },
  //       error: (err) => {
  //         console.error('Error downloading COA PDF:', err);
  //         alert(
  //           'Could not download PDF – invalid URL or expired presigned link.'
  //         );
  //       },
  //     });
  // }
downloadCoa(item: any) {
  window.open(
    `${this.apiBase}/download/${item.lotNumber}`,
    '_blank'
  );
}



}
