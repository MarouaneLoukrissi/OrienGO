import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { ForgotPasswordComponent } from './pages/forgot-password/forgot-password.component';
import { VerifyCodeComponent } from './pages/verify-code/verify-code.component';
import { ResetPasswordComponent } from './pages/reset-password/reset-password.component';
import { SignUpComponent } from './pages/sign-up/sign-up.component';
import { NgModule } from '@angular/core';
import { ExpressTestComponent } from './pages/express-test/express-test.component';
import { FullTestComponent } from './pages/full-test/full-test.component';
import { ResultsComponent } from './pages/results/results.component';
import { RecommendationsComponent } from './pages/recommendations/recommendations.component';
import { JobsComponent } from './pages/jobs/jobs.component';
import { HistoryComponent } from './pages/history/history.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { NetworksComponent } from './pages/networks/networks.component';
import { FavoritesComponent } from './pages/favorites/favorites.component';
import { SavedComponent } from './pages/saved/saved.component';
import { SettingsComponent } from './pages/settings/settings.component';
import { NotificationsComponent } from './pages/notifications/notifications.component';

export const routes: Routes = [
    { path: '', component: LoginComponent },
    { path: 'forgot-password', component: ForgotPasswordComponent },
    { path: 'verify-code', component: VerifyCodeComponent },
    { path: 'reset-password', component: ResetPasswordComponent },
    { path: 'sign-up', component: SignUpComponent },

    // Test routes
    { path: 'test/express', component: ExpressTestComponent },
    { path: 'test/full', component: FullTestComponent },
    { path: 'test/results', component: ResultsComponent },
    { path: 'test/recommendations', component: RecommendationsComponent},
    { path: 'jobs', component: JobsComponent},
    { path: 'history', component: HistoryComponent},
    { path: 'profile/:id', component: ProfileComponent },
    { path: 'networks', component: NetworksComponent },
    { path: 'favorites', component: FavoritesComponent },
    { path: 'saved', component: SavedComponent },
    { path: 'settings', component: SettingsComponent },
    { path: 'notifications', component: NotificationsComponent }
];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}

