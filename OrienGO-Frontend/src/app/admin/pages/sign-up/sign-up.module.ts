import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SignUpComponent } from './sign-up.component';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';

@NgModule({
  declarations: [SignUpComponent],
  imports: [
    CommonModule,
    FormsModule,
    TranslateModule,
    RouterModule.forChild([
      { path: '', component: SignUpComponent }
    ])
  ]
})
export class SignUpModule {}
