import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ChartsModule } from 'ng2-charts';
import { TooltipModule, TooltipOptions } from 'ng2-tooltip-directive';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { OrderModule } from 'ngx-order-pipe';
import { TableModule } from 'primeng/table';
import { NgxSpinnerModule } from "ngx-spinner";
import { NgSelectModule } from '@ng-select/ng-select';
import { RemovePopupComponent } from './components/comfirmation-popup/remove/remove-popup.component';

export const MyDefaultTooltipOptions: TooltipOptions = {
    'show-delay': 500
}

@NgModule({
    declarations: [
        RemovePopupComponent
    ],
    imports: [
        CommonModule,
        FormsModule,
        RouterModule,
        TranslateModule,
        ReactiveFormsModule,
        OrderModule,
        TooltipModule.forRoot(MyDefaultTooltipOptions as TooltipOptions),
        NgSelectModule
    ],
    exports: [
        CommonModule,
        TableModule,
        RouterModule,
        NgbModule,
        TranslateModule,
        FormsModule,
        ReactiveFormsModule,
        NgbModule,
        NgSelectModule,
        NgxSpinnerModule,
        ChartsModule
    ]
})
export class SharedModule { }