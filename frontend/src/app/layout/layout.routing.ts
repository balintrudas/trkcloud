import {ModuleWithProviders} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {LayoutComponent} from './layout.component';

const routes: Routes = [
  {
    path: '', component: LayoutComponent, children: [
      {path: '', loadChildren: '../track/track.module#TrackModule', data: {title: 'Tracking'}},
      {path: 'about', loadChildren: '../about/about.module#AboutModule', data: {title: 'About'}}
    ]
  }
];

export const routing: ModuleWithProviders = RouterModule.forChild(routes);
