import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SidebarComponent } from './sidebar.component';
import { RouterModule } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

@NgModule({
  declarations: [SidebarComponent],
  imports: [
    CommonModule,
    RouterModule,
    TranslateModule
  ],
  exports: [SidebarComponent]
})
export class SidebarModule {}
