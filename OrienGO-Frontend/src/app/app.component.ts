import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';


@Component({
  selector: 'app-root',
  standalone: false,
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  supportedLangs = ['en', 'fr'];
  protected readonly localStorage = localStorage;
  selectedLang = this.supportedLangs.includes(localStorage.getItem('lang') ?? '') ? localStorage.getItem('lang')! : 'en';
  constructor(private translateService: TranslateService){
    const savedLang = localStorage.getItem('lang');
    const browserLang = navigator.language.split('-')[0]; // Use 'en', 'fr', etc.
    const lang = savedLang || (browserLang === 'fr' ? 'fr' : 'en');
    // Save chosen language if not already saved
    if (!savedLang) {
      localStorage.setItem('lang', lang);
      this.selectedLang = lang;
    }

    this.translateService.setDefaultLang('en');
    this.translateService.use(lang);
  }
  onLangChange() {
    localStorage.setItem('lang', this.selectedLang);
    this.translateService.use(this.selectedLang);
  }
  ngOnInit() {
  }
}
