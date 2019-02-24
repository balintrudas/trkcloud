import { NgModule } from '@angular/core';
import { LayoutComponent } from './layout.component';
import {routing} from './layout.routing';
import {SharedModule} from '../shared/shared.module';
import {MaterialModule} from '../shared/material.module';

@NgModule({
  imports: [
    routing,
    SharedModule,
    MaterialModule
  ],
  declarations: [LayoutComponent]
})
export class LayoutModule { }
