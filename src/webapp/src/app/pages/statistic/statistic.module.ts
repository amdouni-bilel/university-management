import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { SharedModule } from 'src/app/shared';
import { StatisticComponent } from './statistic.component';

const routes: Routes = [
  { path: '', component: StatisticComponent }
];

const MODULE_COMPONENT = [
    StatisticComponent
];

@NgModule({
  declarations: MODULE_COMPONENT,
  imports: [RouterModule.forChild(routes), SharedModule, ButtonModule]
})
export class StatisticModule { }