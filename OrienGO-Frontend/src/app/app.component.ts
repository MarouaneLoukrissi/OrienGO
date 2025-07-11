import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';


@Component({
  selector: 'app-root',
  standalone: false,
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  constructor(private translateService: TranslateService){
    const savedLang = localStorage.getItem('lang');
    const browserLang = navigator.language.split('-')[0]; // Use 'en', 'fr', etc.

    const lang = savedLang || (browserLang === 'fr' ? 'fr' : 'en');

    // Save chosen language if not already saved
    if (!savedLang) {
      localStorage.setItem('lang', lang);
    }

    this.translateService.setDefaultLang('en');
    this.translateService.use(lang);
  }
  switchLang(event: Event) {
    const select = event.target as HTMLSelectElement;
    const lang = select.value;
    console.log('Switching language to:', lang);
    localStorage.setItem('lang', lang);
    this.translateService.setDefaultLang(lang);
    this.translateService.use(lang);
  }
  protected readonly localStorage = localStorage;
  ngOnInit() {
    // this.authInitService.init();
  }
}
