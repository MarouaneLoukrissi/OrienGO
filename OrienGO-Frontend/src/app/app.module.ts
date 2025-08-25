import { APP_INITIALIZER, CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { CommonModule, NgClass, NgStyle } from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule, RouterOutlet } from '@angular/router';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TranslateLoader, TranslateModule, TranslateService } from '@ngx-translate/core';
import { ForgotPasswordComponent } from './pages/common/forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './pages/common/reset-password/reset-password.component';
import { SignUpComponent } from './pages/student/sign-up/sign-up.component';
import { VerifyCodeComponent } from './pages/common/verify-code/verify-code.component';
import { AppRoutingModule } from './app-routing.module';
import { LoginComponent } from './pages/common/login/login.component';
import { ResultsComponent } from './pages/student/results/results.component';
import { RadarChartComponent } from './pages/common/radar-chart/radar-chart.component';
import { BarChartComponent } from './pages/common/bar-chart/bar-chart.component';
import { SidebarComponent } from './layout/sidebar/sidebar.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { JobsComponent } from './pages/student/jobs/jobs.component';
import { HistoryComponent } from './pages/student/history/history.component';
import { ProfileComponent } from './pages/student/profile/profile.component';
import { NetworksComponent } from './pages/student/networks/networks.component';
import { FavoritesComponent } from './pages/student/favorites/favorites.component';
import { SavedComponent } from './pages/student/saved/saved.component';
import { AdminCareersComponent } from './pages/admin/admin-careers/admin-careers.component';
import { AdminDashboardComponent } from './pages/admin/admin-dashboard/admin-dashboard.component';
import { AdminQuestionnairesComponent } from './pages/admin/admin-questionnaires/admin-questionnaires.component';

import {
  Bell,
  Home,
  LogOut,
  Globe,
  Clock,
  Lock,
  Settings,
  LucideAngularModule,
  Calendar,
  ClipboardList,UserPlus, Send, Share2, MapPin, CalendarDays, GraduationCap,
    Timer, Star,
    FileDown, User,Phone, Eye,TrendingUp,CheckCircle, Target, Zap, Book, Briefcase, NotebookPen
  } from 'lucide-angular';
import { HomeComponent } from './pages/common/home/home.component';
import { ManageAdminsComponent } from './pages/admin/manage-admins/manage-admins.component';
import { ManageUsersComponent } from './pages/admin/manage-users/manage-users.component';
import { NetworkCoachComponent } from './pages/coach/network-coach/network-coach.component';
import { ProfileEditComponent } from './pages/coach/profile-edit/profile-edit.component';
import { SuperAdminComponent } from './pages/admin/super-admin/super-admin.component';
import { RecommendationsComponent } from './pages/student/recommendations/recommendations.component';
import { ExpressTestComponent } from './pages/student/express-test/express-test.component';
import { FullTestComponent } from './pages/student/full-test/full-test.component';
import { NotAuthorizedComponent } from './pages/admin/not-authorized/not-authorized.component';
import { RiasecTestPageComponent } from './pages/common/riasec-test-page/riasec-test-page.component';
import { NotFoundComponent } from './pages/common/not-found/not-found.component';
import { SettingsComponent } from './pages/common/settings/settings.component';
import { NotificationsComponent } from './pages/common/notifications/notifications.component';
import { NavbarComponent } from './layout/navbar/navbar.component';
import { LangComponent } from './layout/lang/lang.component';
import { ProfileCoachComponent } from './pages/coach/profile-coach/profile-coach.component';
import { FooterComponent } from './layout/footer/footer.component';
import { NavFooterComponent } from './layout/nav-footer/nav-footer.component';
import { appInitializerFactory } from './init/translate-init';
import { JobsRecommendationComponent } from './pages/student/jobs-recommendation/jobs-recommendation.component';
import { TestComponent } from './pages/student/test/test.component';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { CoachDashboardComponent } from './pages/coach/dashboard/coachDashboard.component';
import { StudentDashboardComponent } from './pages/student/dashboard/student-dashboard.component';


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
    NotificationsComponent,
    AdminCareersComponent,
    AdminDashboardComponent,
    AdminQuestionnairesComponent,
    CoachDashboardComponent,
    StudentDashboardComponent,
    HomeComponent,
    ManageAdminsComponent,
    ManageUsersComponent,
    NetworkCoachComponent,
    NotAuthorizedComponent,
    ProfileCoachComponent,
    ProfileEditComponent,
    SignUpComponent,
    SuperAdminComponent,
    RiasecTestPageComponent,
    NotFoundComponent,
    NavbarComponent,
    LangComponent,
    FooterComponent,
    NavFooterComponent,
    JobsRecommendationComponent,
    TestComponent,
  ],
  imports: [
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
    TranslateModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatSnackBarModule,
    LucideAngularModule.pick({
      Home,
      LogOut,
      Bell,
      Globe,
      Clock,
      Lock,Calendar,
      ClipboardList,Star,
        Timer,
        FileDown, UserPlus, Send, Share2, MapPin, CalendarDays, GraduationCap,
        Eye,TrendingUp,CheckCircle, Target, Zap, Book, Briefcase, NotebookPen,Settings,User, Phone

    })
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: appInitializerFactory,
      deps: [TranslateService],
      multi: true
    }
  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AppModule { }
