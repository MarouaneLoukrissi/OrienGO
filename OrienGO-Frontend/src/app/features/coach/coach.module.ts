import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, RouterOutlet } from '@angular/router';
//start
import { CoachRoutingModule } from './coach-routing.module';
import { CoachLayoutComponent } from './coach-layout/coach-layout.component';
import { SidebarComponent } from './sidebar/sidebar.component';
//icones de dashboard
import {
  Bell,
  Home,
  LogOut,
  User,
  Globe,
  Clock,
  Lock,
  Settings,
  LucideAngularModule,
  Calendar
} from 'lucide-angular';
import { TranslateModule } from '@ngx-translate/core';
import { SettingsComponent } from './settings/settings.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { NetworkCoachComponent } from './network-coach/network-coach.component';
import { ProfileComponent } from './profile/profile.component';
import { NotificationsComponent } from './notifications/notifications.component';
import { ProfileEditComponent } from './profile-edit/profile-edit.component';
import { HistoryComponent } from './history/history.component';
@NgModule({
  declarations: [
    CoachLayoutComponent,
    SidebarComponent,
    SettingsComponent,
    DashboardComponent,
    NetworkCoachComponent,
    ProfileComponent,
    NotificationsComponent,
    ProfileEditComponent,
    HistoryComponent
    
    
  ],
  imports: [
    CommonModule,
    RouterModule,
    CoachRoutingModule,
    TranslateModule,
    LucideAngularModule.pick({
  User,
  Home,
  LogOut,
  Bell,
  Globe,
  Clock,
  Lock,
  Settings, Calendar})
  ]
})
export class CoachModule { }
