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
import { TranslateModule } from '@ngx-translate/core';
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
    HistoryComponent,
    PublicProfileComponent,
    ResultsComponent, 
    RecommendationsComponent
    
    
  ],
  imports: [
    CommonModule,
    RouterModule,
    CoachRoutingModule,
    TranslateModule,
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
  
})],

})
export class CoachModule { }
