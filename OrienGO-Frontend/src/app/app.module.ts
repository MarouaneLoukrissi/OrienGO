import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { CommonModule, NgClass, NgStyle } from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule, RouterOutlet } from '@angular/router';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { ForgotPasswordComponent } from './pages/forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './pages/reset-password/reset-password.component';
import { SignUpComponent } from './pages/sign-up/sign-up.component';
import { VerifyCodeComponent } from './pages/verify-code/verify-code.component';
import { AppRoutingModule } from './app-routing.module';
import { LoginComponent } from './pages/login/login.component';
import { ExpressTestComponent } from './pages/express-test/express-test.component';
import { FullTestComponent } from './pages/full-test/full-test.component';
import { ResultsComponent } from './pages/results/results.component';
import { RadarChartComponent } from './pages/radar-chart/radar-chart.component';
import { BarChartComponent } from './pages/bar-chart/bar-chart.component';
import { SidebarComponent } from './layout/sidebar/sidebar.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RecommendationsComponent } from './pages/recommendations/recommendations.component';
import { JobsComponent } from './pages/jobs/jobs.component';
import { HistoryComponent } from './pages/history/history.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { NetworksComponent } from './pages/networks/networks.component';
import { FavoritesComponent } from './pages/favorites/favorites.component';
import { SavedComponent } from './pages/saved/saved.component';
import { SettingsComponent } from './pages/settings/settings.component';
import { NotificationsComponent } from './pages/notifications/notifications.component';

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}
@NgModule({
  declarations: [
    AppComponent,
    ForgotPasswordComponent,
    ResetPasswordComponent,
    SignUpComponent,
    VerifyCodeComponent,
    LoginComponent,
    ExpressTestComponent,
    FullTestComponent,
    ResultsComponent,
    RadarChartComponent,
    BarChartComponent,
    SidebarComponent,
    RecommendationsComponent,
    JobsComponent,
    HistoryComponent,
    ProfileComponent,
    NetworksComponent,
    FavoritesComponent,
    SavedComponent,
    SettingsComponent,
    NotificationsComponent
  ], // ajoute ici les composants non-standalone
  imports: [
    RouterOutlet,
    CommonModule,
    BrowserModule,
    RouterModule,
    HttpClientModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient],
      }
    }),
    ReactiveFormsModule,
    FormsModule,
    CommonModule,
    TranslateModule,
    AppRoutingModule,
    CommonModule,
    BrowserModule,
    BrowserAnimationsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
