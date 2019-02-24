import { NgModule } from '@angular/core';
import {SharedModule} from '../shared/shared.module';
import {MaterialModule} from '../shared/material.module';
import { GridComponent } from './grid/grid.component';
import {
  MatTableModule,
  MatPaginatorModule,
  MatSortModule,
} from '@angular/material';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import { MomentModule } from 'ngx-moment';
import {TrackComponent} from './track.component';
import {routing} from './track.routing';

@NgModule({
  imports: [
    routing,
    SharedModule,
    MaterialModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    LeafletModule.forRoot(),
    MomentModule
  ],
  declarations: [TrackComponent, GridComponent],
  providers: [],
})
export class TrackModule { }
