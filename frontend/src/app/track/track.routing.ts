import {ModuleWithProviders} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {TrackComponent} from './track.component';

const routes: Routes = [
  {path: '', component: TrackComponent, pathMatch: 'full', data: {title: 'Tracking'}}
];

export const routing: ModuleWithProviders = RouterModule.forChild(routes);
