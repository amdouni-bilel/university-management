import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { concat, EMPTY, timer } from "rxjs";
import { LazyLoadEvent } from 'primeng/api';
import { switchMapTo } from "rxjs/operators";
import { ModuleService } from 'src/app/core/services';
import { ActionEnum } from 'src/app/shared/components/cm-table-container/models/config-column.model';
import { DataValue } from 'src/app/shared/components/cm-table-container/models/data-value.model';
import { ModuleModalComponent } from './module-modal/module-modal.component';
import { DatePipe } from '@angular/common';
import { AssignModalComponent } from './assign-modal/assign-modal.component';
import { RemovePopupComponent } from 'src/app/shared/components/comfirmation-popup/remove/remove-popup.component';

@Component({
    selector: 'modules',
    templateUrl: './modules.component.html',
    styleUrls: ['./modules.component.scss'],
    providers: [DatePipe]
})
export class ModulesComponent implements OnInit {

    public form: FormGroup;

    public modules: any[];

    public totalRecords: number;

    constructor(private modalService: NgbModal,
        private spinner: NgxSpinnerService,
        private fb: FormBuilder,
        private datePipe: DatePipe,
        private moduleService: ModuleService) {
    }

    ngOnInit() {
        this.initForm();
    }

    public loadModules(event: LazyLoadEvent) {
        this.spinner.show();
        let order: string;
        if (event && event.sortOrder && event.sortOrder === 1) {
            order = "ASC";
        } else if (event && event.sortOrder && event.sortOrder === -1) {
            order = "DESC";
        }
        setTimeout(() => {
            this.moduleService.getModules(event.first / event.rows, event.sortField ? event.sortField : null, order, event.globalFilter ? event.globalFilter : null).subscribe(data => {
                this.modules = data.body.content;
                this.totalRecords = data.totalItems;
                this.spinner.hide();
            })
        }, 1000);
    }

    public openModalAssign(moduleId?: string, event?: any) {
        const modal: NgbModalRef = this.modalService.open(AssignModalComponent,
            {
                size: 'xl',
                ariaLabelledBy: 'modal-basic-title',
                keyboard: false,
                backdrop: 'static',
                centered: true
            });
        if (event) {
            modal.componentInstance.update = true;
            modal.componentInstance.data = event;
        } else {
            modal.componentInstance.update = false;
            console.log(moduleId);
            
            modal.componentInstance.moduleId = moduleId;
        }
        modal.componentInstance.triggerSave.subscribe((dataValue: DataValue) => {
            this.spinner.show();
            if (dataValue.action === ActionEnum.CREATE) {
                concat(
                    this.moduleService.addAssignClass(dataValue.value).pipe(switchMapTo(EMPTY)),
                    timer(1000).pipe(switchMapTo(EMPTY)),
                    this.moduleService.getModules(0)
                ).subscribe((data: any) => {
                    this.modules = data.body.content;
                    this.totalRecords = data.totalItems;
                    this.spinner.hide();
                    modal.componentInstance.setIsSaved({ isSaved: true });
                }, error => {
                    this.spinner.hide();
                    if (error.error.code === 701) {
                        modal.componentInstance.setIsSaved({ isSaved: false, code: error.error.code });
                    }
                })
            } else if (dataValue.action === ActionEnum.UPDATE) {
                concat(
                    this.moduleService.updateAssignClass(dataValue.value).pipe(switchMapTo(EMPTY)),
                    timer(1000).pipe(switchMapTo(EMPTY)),
                    this.moduleService.getModules(0)
                ).subscribe((data: any) => {
                    this.modules = data.body.content;
                    this.totalRecords = data.totalItems;
                    this.spinner.hide();
                    modal.componentInstance.setIsSaved({ isSaved: true });
                }, error => {
                    this.spinner.hide();
                    if (error.error.code === 701) {
                        modal.componentInstance.setIsSaved({ isSaved: false, code: error.error.code });
                    }
                })
            }
        })
    }

    public openModal() {
        const modal: NgbModalRef = this.modalService.open(ModuleModalComponent,
            {
                size: 'xl',
                ariaLabelledBy: 'modal-basic-title',
                keyboard: false,
                backdrop: 'static',
                centered: true
            });
        modal.componentInstance.triggerSave.subscribe((dataValue: DataValue) => {
            this.spinner.show();
            if (dataValue.action === ActionEnum.CREATE) {
                concat(
                    this.moduleService.addModule(dataValue.value).pipe(switchMapTo(EMPTY)),
                    timer(1000).pipe(switchMapTo(EMPTY)),
                    this.moduleService.getModules(0)
                ).subscribe((data: any) => {
                    this.spinner.hide();
                    this.modules = data.body.content;
                    this.totalRecords = data.totalItems;
                    modal.componentInstance.setIsSaved({ isSaved: true });
                }, error => {
                    this.spinner.hide();
                    if (error.error.code === 701) {
                        modal.componentInstance.setIsSaved({ isSaved: false, code: error.error.code });
                    }
                })
            }
        });
    }

    public deleteAssign(event) {
        const modal: NgbModalRef = this.initPopUp(RemovePopupComponent);
        modal.componentInstance.config = { title: "Confirmation de suppression", message: "Est-ce que vous confirmez la suppression définitive ?" };
        modal.componentInstance.sendData.subscribe((data: boolean) => {
            if (data) {
                this.spinner.show();
                concat(
                    this.moduleService.deleteAssign(event.assignId).pipe(switchMapTo(EMPTY)),
                    timer(1000).pipe(switchMapTo(EMPTY)),
                    this.moduleService.getModules(0)
                ).subscribe((data: any) => {
                    this.spinner.hide();
                    this.modules = data.body.content;
                    this.totalRecords = data.totalItems;
                    this.modalService.dismissAll();
                })
            }
        })
    }

    public deleteModule(event) {
        const modal: NgbModalRef = this.initPopUp(RemovePopupComponent);
        modal.componentInstance.config = { title: "Confirmation de suppression", message: "Est-ce que vous confirmez la suppression définitive ?" };
        modal.componentInstance.sendData.subscribe((data: boolean) => {
            if (data) {
                this.spinner.show();
                concat(
                    this.moduleService.deleteModule(event.moduleId).pipe(switchMapTo(EMPTY)),
                    timer(1000).pipe(switchMapTo(EMPTY)),
                    this.moduleService.getModules(0)
                ).subscribe((data: any) => {
                    this.spinner.hide();
                    this.modules = data.body.content;
                    this.totalRecords = data.totalItems;
                    this.modalService.dismissAll();
                })
            }
        });
    }

    private initPopUp(content: any): NgbModalRef {
        return this.modalService.open(content,
            {
                size: 'lg',
                ariaLabelledBy: 'modal-basic-title',
                keyboard: false,
                backdrop: 'static',
                centered: true
            });
    }

    public onSearch() {
        if (this.form.valid) {
        }
    }

    public reset() {
        this.form.reset();
        this.spinner.show();
        this.moduleService.getModules(0).subscribe(data => {
            this.spinner.hide();
            this.modules = data.body.content;
            this.totalRecords = data.totalItems;
        });
    }

    private initForm(): void {
        this.form = this.fb.group({
            moduleId: [null],
            typeExam: [null],
            semester: [null],
            period: [null]
        })
    }

    public mapTeachers(teachers): any {
        return teachers.map(item => item.fullName).toString();
    }

    public mapPerids(periods): any {
        return periods ? periods.map(item => item.code == "1" ? "P1" : "P2").toString() : null;
    }

}