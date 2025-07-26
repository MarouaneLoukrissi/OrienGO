import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CoachLayoutComponent } from './coach-layout/coach-layout.component';
import { SettingsComponent } from './settings/settings.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { NetworkCoachComponent } from './network-coach/network-coach.component';
import { ProfileComponent } from './profile/profile.component';
import { NotificationsComponent } from './notifications/notifications.component';
import { ProfileEditComponent } from './profile-edit/profile-edit.component';
import { HistoryComponent } from './history/history.component';
import { PublicProfileComponent } from './public-profile/public-profile.component';
import { ResultsComponent } from './results/results.component';
import { RecommendationsComponent } from './recommendations/recommendations.component';
const routes: Routes = [
   { path: 'coach',
    component: CoachLayoutComponent,
    children: [
      // ici viendront les routes du coach : dashboard, settings...
          { path: 'settings', component: SettingsComponent } ,
          { path: 'dashboard', component: DashboardComponent },
          { path: 'network', component: NetworkCoachComponent },
          { path: 'profile', component: ProfileComponent},
          { path: 'notifications', component: NotificationsComponent },
          { path: 'profile/edit',component: ProfileEditComponent},
          { path: 'history',component: HistoryComponent},

          
          {path: 'coach-profile/:id',component: PublicProfileComponent},
          {path: 'results/eleve/:id',component: ResultsComponent},
          
          {path: 'recommendations/:id',component: RecommendationsComponent}

         ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CoachRoutingModule{ }