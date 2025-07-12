import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { ForgotPasswordComponent } from './pages/forgot-password/forgot-password.component';
import { VerifyCodeComponent } from './pages/verify-code/verify-code.component';
import { ResetPasswordComponent } from './pages/reset-password/reset-password.component';
import { SignUpComponent } from './pages/sign-up/sign-up.component';
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

