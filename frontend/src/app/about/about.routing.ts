import {ModuleWithProviders} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {AboutComponent} from './about.component';

const routes: Routes = [
  {path: '', component: AboutComponent, pathMatch: 'full'}
];

export const routing: ModuleWithProviders = RouterModule.forChild(routes);
