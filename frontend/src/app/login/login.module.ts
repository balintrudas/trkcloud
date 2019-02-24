import {routing} from './login.routing';
import {NgModule} from '@angular/core';
import {LoginComponent} from './login.component';
import {SharedModule} from '../shared/shared.module';
import {MaterialModule} from '../shared/material.module';

@NgModule({
  imports: [
    routing,
    SharedModule,
    MaterialModule
  ],
  declarations: [LoginComponent]
})
export class LoginModule {
}
