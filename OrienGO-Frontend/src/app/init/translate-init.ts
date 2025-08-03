import { TranslateService } from '@ngx-translate/core';

export function appInitializerFactory(
  translate: TranslateService
): () => Promise<void> {
  return () => {
    const savedLang = localStorage.getItem('lang');
    const browserLang = navigator.language.split('-')[0];
    const lang = savedLang || (browserLang === 'fr' ? 'fr' : 'en');

    if (!savedLang) {
      localStorage.setItem('lang', lang);
    }

    translate.setDefaultLang('en');
    return translate.use(lang).toPromise().then(() => {});
  };
}
