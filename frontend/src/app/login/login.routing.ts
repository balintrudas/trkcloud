import {ModuleWithProviders} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {LoginComponent} from './login.component';
import {LoginGuard} from './login.guard';

const routes: Routes = [
  {path: '', component: LoginComponent, canActivate: [LoginGuard]}
];

export const routing: ModuleWithProviders = RouterModule.forChild(routes);
