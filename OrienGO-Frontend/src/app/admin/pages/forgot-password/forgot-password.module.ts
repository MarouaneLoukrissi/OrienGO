import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ForgotPasswordComponent } from './forgot-password.component';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';

const routes: Routes = [
  { path: '', component: ForgotPasswordComponent }
];

@NgModule({
  declarations: [ForgotPasswordComponent],
  imports: [
    CommonModule,
    FormsModule,
    TranslateModule,
    RouterModule.forChild(routes)
  ]
})
export class ForgotPasswordModule {}
