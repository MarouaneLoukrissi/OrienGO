import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { VerifyCodeComponent } from './verify-code/verify-code.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { NgModule } from '@angular/core';
export const routes: Routes = [
    { path: '', component: LoginComponent },
    { path: 'forgot-password', component: ForgotPasswordComponent },
    { path: 'verify-code', component: VerifyCodeComponent },
    {path: 'reset-password', component:ResetPasswordComponent},
    {path:'sign-up',component:SignUpComponent}
];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}

