import {ModuleWithProviders} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {LoginGuard} from './login/login.guard';
import {AuthGuard} from './core/guard/auth.guard';

const routes: Routes = [
  {path: 'login', loadChildren: './login/login.module#LoginModule', canActivate: [LoginGuard]},
  {path: '', loadChildren: './layout/layout.module#LayoutModule', canActivate: [AuthGuard]}
];
export const routing: ModuleWithProviders = RouterModule.forRoot(routes);
