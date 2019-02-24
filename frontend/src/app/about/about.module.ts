import { NgModule } from '@angular/core';
import {SharedModule} from '../shared/shared.module';
import {MaterialModule} from '../shared/material.module';
import {AboutComponent} from './about.component';
import {routing} from './about.routing';

@NgModule({
  imports: [
    routing,
    SharedModule,
    MaterialModule
  ],
  declarations: [AboutComponent]
})
export class AboutModule { }
