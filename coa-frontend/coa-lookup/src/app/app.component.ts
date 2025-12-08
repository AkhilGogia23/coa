import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LotLookupService ,LotResponse} from './service/lot-lookup.service';
@Component({
  selector: 'app-root',
  imports: [CommonModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
   title = 'COA Lookup';

  // 🔹 inject the service
  private lotService = inject(LotLookupService);

  lotNumber = '';
  loading = signal(false);
  error = signal<string | null>(null);
  result = signal<LotResponse | null>(null);

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
      }
    });
  }
}
