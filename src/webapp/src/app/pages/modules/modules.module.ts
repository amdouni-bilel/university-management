import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from 'src/app/shared';
import { ButtonModule } from 'primeng/button';
import { ModuleModalComponent } from './module-modal/module-modal.component';
import { ModulesComponent } from './modules.component';
import { AssignModalComponent } from './assign-modal/assign-modal.component';

const routes: Routes = [
    { path: '', component: ModulesComponent }
];

const MODULE_COMPONENT = [
    ModulesComponent,
    ModuleModalComponent,
    AssignModalComponent
];

@NgModule({
    declarations: MODULE_COMPONENT,
    imports: [RouterModule.forChild(routes), SharedModule, ButtonModule],
    entryComponents: [
        ModuleModalComponent,
        AssignModalComponent
    ]
})
export class ModulesModule { }