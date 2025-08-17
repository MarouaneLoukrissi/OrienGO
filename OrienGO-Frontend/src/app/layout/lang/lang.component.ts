import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-lang',
  templateUrl: './lang.component.html',
  styleUrl: './lang.component.css'
})
export class LangComponent implements OnInit {
  supportedLangs = ['en', 'fr'];
  selectedLang = localStorage.getItem('lang') || 'en';

  constructor(private translateService: TranslateService) {}
  ngOnInit(): void {
    this.translateService.addLangs(this.supportedLangs);
    this.translateService.setDefaultLang('en');
    this.translateService.use(this.selectedLang);
  }

  onLangChange() {
    localStorage.setItem('lang', this.selectedLang);
    this.translateService.use(this.selectedLang);
  }
}
