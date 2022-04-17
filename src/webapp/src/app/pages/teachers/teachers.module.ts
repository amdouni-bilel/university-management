import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from 'src/app/shared';
import { MultiSelectModule } from 'primeng/multiselect';
import { DisponibilityModalComponent } from './disponibility/disponibility.component';
import { TeacherModalComponent } from './teacher-modal/teacher-modal.component';
import { TeachersComponent } from './teachers.component';

const routes: Routes = [
    { path: 'list', component: TeachersComponent },
    { path: 'unavailability', component: DisponibilityModalComponent }
];

const MODULE_COMPONENT = [
    TeachersComponent,
    TeacherModalComponent,
    DisponibilityModalComponent
];

@NgModule({
    declarations: MODULE_COMPONENT,
    imports: [RouterModule.forChild(routes), SharedModule, MultiSelectModule],
    entryComponents: [
        TeacherModalComponent
    ]
})
export class TeachersModule { }