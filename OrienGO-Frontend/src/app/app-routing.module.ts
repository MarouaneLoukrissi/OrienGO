import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './pages/common/login/login.component';
import { ForgotPasswordComponent } from './pages/common/forgot-password/forgot-password.component';
import { VerifyCodeComponent } from './pages/common/verify-code/verify-code.component';
import { ResetPasswordComponent } from './pages/common/reset-password/reset-password.component';
import { SignUpComponent } from './pages/student/sign-up/sign-up.component';
import { NgModule } from '@angular/core';
import { ResultsComponent } from './pages/student/results/results.component';
import { JobsComponent } from './pages/student/jobs/jobs.component';
import { HistoryComponent } from './pages/student/history/history.component';
import { ProfileComponent } from './pages/student/profile/profile.component';
import { NetworksComponent } from './pages/student/networks/networks.component';
import { FavoritesComponent } from './pages/student/favorites/favorites.component';
import { SavedComponent } from './pages/student/saved/saved.component';
import { RecommendationsComponent } from './pages/student/recommendations/recommendations.component';
import { ExpressTestComponent } from './pages/student/express-test/express-test.component';
import { FullTestComponent } from './pages/student/full-test/full-test.component';
import { AdminDashboardComponent } from './pages/admin/admin-dashboard/admin-dashboard.component';
import { ManageUsersComponent } from './pages/admin/manage-users/manage-users.component';
import { AdminQuestionnairesComponent } from './pages/admin/admin-questionnaires/admin-questionnaires.component';
import { AdminCareersComponent } from './pages/admin/admin-careers/admin-careers.component';
import { SuperAdminComponent } from './pages/admin/super-admin/super-admin.component';
import { ManageAdminsComponent } from './pages/admin/manage-admins/manage-admins.component';
import { NotAuthorizedComponent } from './pages/admin/not-authorized/not-authorized.component';
import { RiasecTestPageComponent } from './pages/common/riasec-test-page/riasec-test-page.component';
import { CoachDashboardComponent } from './pages/coach/dashboard/coachDashboard.component';
import { NetworkCoachComponent } from './pages/coach/network-coach/network-coach.component';
import { ProfileEditComponent } from './pages/coach/profile-edit/profile-edit.component';
import { HomeComponent } from './pages/common/home/home.component';
import { SidebarComponent } from './layout/sidebar/sidebar.component';
import { SuperAdminGuard } from './guards/super-admin.guard';
import { NotFoundComponent } from './pages/common/not-found/not-found.component';
import { SettingsComponent } from './pages/common/settings/settings.component';
import { NotificationsComponent } from './pages/common/notifications/notifications.component';
import { NavbarComponent } from './layout/navbar/navbar.component';
import { ProfileCoachComponent } from './pages/coach/profile-coach/profile-coach.component';
import { NavFooterComponent } from './layout/nav-footer/nav-footer.component';
import { JobsRecommendationComponent } from './pages/student/jobs-recommendation/jobs-recommendation.component';
import { TestComponent } from './pages/student/test/test.component';
import { StudentDashboardComponent } from './pages/student/dashboard/student-dashboard.component';

export const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: '', component: NavFooterComponent,
    children: [
      { path: 'learn-more',component: RiasecTestPageComponent },
      { path: 'home', component: HomeComponent },
      { path: 'login', component: LoginComponent },
      { path: 'signup', component: SignUpComponent },
    ]
  },
  { path: '',
    component: SidebarComponent,
    children: [
      // Super Admin Routes
      { path: 'superAdmin',
        children: [
          {
            path: 'dashboard',
            component: SuperAdminComponent,
            // data: { roles: ['ADMIN'] }
          },
          {
            path: 'users',
            component: ManageUsersComponent,
          },
          {
            path: 'questions',
            component: AdminQuestionnairesComponent,
          },
          {
            path: 'jobs',
            component: AdminCareersComponent,
            data: { roles: ['ADMIN'] }
          },
          {
            path: 'manager',
            component: ManageAdminsComponent,
          },
          {
            path: 'admins',
            component: ManageAdminsComponent,
            canActivate: [SuperAdminGuard],
          },
          { path: 'settings',
            component: SettingsComponent
          },
          {
            path: 'not-authorized',
            component: NotAuthorizedComponent,
          },
          { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
      ]
      },
      // Admin Routes
      { path: 'admin',
        children: [
          {
            path: 'dashboard',
            component: AdminDashboardComponent,
            // data: { roles: ['ADMIN'] }
          },
          {
            path: 'users',
            component: ManageUsersComponent,
          },
          {
            path: 'questions',
            component: AdminQuestionnairesComponent,
          },
          {
            path: 'jobs',
            component: AdminCareersComponent,
            data: { roles: ['ADMIN'] }
          },
          {
            path: 'manager',
            component: SuperAdminComponent,
          },
          {
            path: 'admins',
            component: ManageAdminsComponent,
            canActivate: [SuperAdminGuard],
          },
          { path: 'settings',
            component: SettingsComponent
          },
          {
            path: 'not-authorized',
            component: NotAuthorizedComponent,
          },
          { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
      ]
      },

      // Coach Routes
      { path: 'coach',
        children: [
          { path: 'settings',
            component: SettingsComponent
          },
          { path: 'dashboard',
            component: CoachDashboardComponent
          },
          { path: 'network',
            component: NetworkCoachComponent
          },
          { path: 'profile',
            component: ProfileCoachComponent
          },
          { path: 'history',
            component: HistoryComponent
          },
          { path: 'profile/:id',
            component: ProfileCoachComponent
          },
          { path: 'results/eleve/:id',
            component: ResultsComponent
          },
          { path: 'recommendations/:id',
            component: RecommendationsComponent
          },
          { path: 'notifications',
            component: NotificationsComponent
          },
          { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
        ]
      },

      // Student Routes
      { path: 'student',
        children: [
          { path: 'dashboard',
            component: StudentDashboardComponent
          },
          { path: 'test',
            component: TestComponent
          },
          { path: 'test/results/:testId',
            component: ResultsComponent
          },
          { path: 'test/recommendations',
            component: RecommendationsComponent
          },
          { path: 'test/personalizedjobs',
            component: JobsRecommendationComponent
          },
          { path: 'jobs',
            component: JobsComponent
          },
          { path: 'history',
            component: HistoryComponent
          },
          { path: 'profile',
            component: ProfileComponent
          },
          { path: 'profile/:id',
            component: ProfileComponent
          },
          { path: 'network',
            component: NetworksComponent
          },
          { path: 'favorites',
            component: FavoritesComponent
          },
          { path: 'saved',
            component: SavedComponent
          },
          { path: 'settings',
            component: SettingsComponent
          },
          { path: 'notifications',
            component: NotificationsComponent
          },
          { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
        ]
      },
    ]
  },

  // test Routes
  { path: 'student/test/express', 
    children: [
      // Create new test (no testId)
      { path: 'new', component: ExpressTestComponent },

      // Open existing test by ID, question is optional query param
      { path: ':testId', component: ExpressTestComponent },
    ]
  },
  { path: 'student/test/full',
    children: [
      // Create new test (no testId)
      { path: 'new', component: FullTestComponent },

      // Open existing test by ID, question is optional query param
      { path: ':testId', component: FullTestComponent },
    ]
  },

  // Public Routes
  { path: 'forgot-password', component: ForgotPasswordComponent },
  { path: 'verify-code', component: VerifyCodeComponent},
  { path: 'reset-password', component: ResetPasswordComponent},
  { path: '404', component: NotFoundComponent },
  { path: '**', redirectTo: '/404' }
];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
