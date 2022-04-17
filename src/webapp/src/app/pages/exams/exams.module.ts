import { NgModule } from '@angular/core';
import { ExamsComponent } from './exams.component';
import { ExamModalComponent } from './exam-modal/exam-modal.component';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from 'src/app/shared';
import { ButtonModule } from 'primeng/button';
import {RippleModule} from 'primeng/ripple';
const routes: Routes = [
    { path: '', component: ExamsComponent }
];

const MODULE_COMPONENT = [
    ExamsComponent,
    ExamModalComponent
];

@NgModule({
    declarations: MODULE_COMPONENT,
    imports: [RouterModule.forChild(routes), SharedModule, ButtonModule, RippleModule],
    entryComponents: [
        ExamModalComponent
    ]
})
export class ExamsModule { }