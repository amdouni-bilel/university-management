import { DatePipe } from '@angular/common';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { concat, EMPTY, Observable, timer } from "rxjs";
import { switchMapTo } from "rxjs/operators";
import { LazyLoadEvent } from 'primeng/api';
import { DisponibilityService, ExcelDispRoomService, RoomService } from 'src/app/core/services';
import { AbstractControl, FormBuilder, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { RemovePopupComponent } from 'src/app/shared/components/comfirmation-popup/remove/remove-popup.component';
import { Dates } from 'src/app/app-init';
import { ToastrService } from 'ngx-toastr';

@Component({
    templateUrl: './disponibility.component.html',
    styleUrls: ['./disponibility.component.scss'],
    providers: [DatePipe, NgbActiveModal],
})
export class DisponibilityModalComponent implements OnInit {

    @ViewChild('content')
    public content: ElementRef;

    public form: FormGroup;

    public minDate = new Date(Date.now() + 1 * 24 * 60 * 60 * 1000).toISOString();

    public rooms: any;

    public roomList: any[];

    public isUpdate: boolean = false;
    public showLoaderError: boolean = false;

    public currentDispo: any;
    public saveError: string;

    public totalRecords: number;
    public rooms$: Observable<any>;

    public roomsBuffer = [];
    public bufferSize = 10;
    public numberOfItemsFromEndBeforeFetchingMore = 10;
    public loading = false;

    constructor(private modalService: NgbModal,
        private toastr: ToastrService,
        private spinner: NgxSpinnerService,
        private roomService: RoomService,
        private fb: FormBuilder,
        private datePipe: DatePipe,
        private excelDispRoomService: ExcelDispRoomService,
        private disponibilityService: DisponibilityService) {
    }

    ngOnInit() {
        this.spinner.show();
        this.rooms$ = this.roomService.getRooms();
    }

    public loadRooms(event: LazyLoadEvent) {
        let order: string;
        if (event && event.multiSortMeta && event.multiSortMeta[0].order === 1) {
            order = "ASC";
        } else if (event && event.multiSortMeta && event.multiSortMeta[0].order === -1) {
            order = "DESC";
        }
        setTimeout(() => {
            this.roomService.getDisponibilitiesRooms(event.first / event.rows, event.multiSortMeta ? event.multiSortMeta[0].field : null, order, event.globalFilter ? event.globalFilter : null).subscribe(data => {
                this.rooms = data.body.content;
                this.totalRecords = data.totalItems;
                this.spinner.hide();
            })
        }, 1000);
    }

    private fetchMore() {
        const len = this.roomsBuffer.length;
        const more = this.roomList.slice(len, this.bufferSize + len);
        this.loading = true;
        setTimeout(() => {
            this.loading = false;
            this.roomsBuffer = this.roomsBuffer.concat(more);
        }, 200)
    }

    onScrollToEnd() {
        this.fetchMore();
    }

    onScroll({ end }) {
        if (this.loading || this.roomList.length <= this.roomsBuffer.length) {
            return;
        }

        if (end + this.numberOfItemsFromEndBeforeFetchingMore >= this.roomsBuffer.length) {
            this.fetchMore();
        }
    }

    public openModal() {
        this.initForm();
        this.form.setValidators(this.checkDate());
        this.spinner.show();
        this.roomService.getRoomsWithoutDisponibilities().subscribe(rooms => {
            this.roomList = rooms;
            this.roomsBuffer = this.roomList.slice(0, this.bufferSize);
            this.spinner.hide();
        });
        this.isUpdate = false;
        this.modalService.open(this.content,
            {
                size: 'lg',
                ariaLabelledBy: 'modal-basic-title',
                keyboard: false,
                backdrop: 'static',
                centered: true
            })
    }

    public openUpdateModal(data) {
        this.initForm();
        this.form.setValidators(this.checkDate());
        this.spinner.show();
        this.roomService.getRoomsWithoutDisponibilities().subscribe(rooms => {
            this.roomList = rooms;
            this.roomsBuffer = this.roomList.slice(0, this.bufferSize);
            this.spinner.hide();
        });
        this.modalService.open(this.content,
            {
                size: 'lg',
                ariaLabelledBy: 'modal-basic-title',
                keyboard: false,
                backdrop: 'static',
                centered: true
            })
        this.isUpdate = true;
        this.currentDispo = data;
        let startHour = data.startHour.toString().length == 3 ? "0" + data.startHour.toString() : data.startHour.toString();
        let endHour = data.endHour.toString().length == 3 ? "0" + data.endHour.toString() : data.endHour.toString();
        this.form.patchValue({
            classRoomId: data.classRoomId,
            startDate: this.datePipe.transform(data.startDate.toLocaleString().substring(0, 10), "yyyy-MM-dd"),
            endDate: this.datePipe.transform(data.endDate.toLocaleString().substring(0, 10), "yyyy-MM-dd"),
            startHour: startHour ? startHour.toString().substring(0, 2) + ":" + startHour.toString().substring(2, 4) : null,
            endHour: endHour ? endHour.toString().substring(0, 2) + ":" + endHour.toString().substring(2, 4) : null,
            motif: data.motif,
            commentd: data.commentd
        })
    }

    public disableF(): boolean {
        return this.form.get('motif').value === "DS" || this.form.get('motif').value === "Examen" || this.form.get('motif').value === "Rattrapage";
    }

    public openDeleteModal(data) {
        this.currentDispo = data;
        const modal: NgbModalRef = this.modalService.open(RemovePopupComponent,
            {
                size: 'lg',
                ariaLabelledBy: 'modal-basic-title',
                keyboard: false,
                backdrop: 'static',
                centered: true
            });
        modal.componentInstance.config = { title: "Confirmation de suppression", message: "Est-ce que vous confirmez la suppression définitive ?" };
        modal.componentInstance.sendData.subscribe((data: boolean) => this.deleteDisponibility(this.currentDispo.classRoomId, this.currentDispo.disponibilityId));
    }

    public reset() {
        this.form.reset();
    }

    public export() {
        this.roomService.getRooms().subscribe(data => this.excelDispRoomService.exportAsExcelFile(data))
    }

    public addDisponibility() {
        this.spinner.show();
        let arg = new CreateDisponibilityRequest(this.form.value.startDate, this.form.value.endDate, +this.form.value.startHour.replace(":", ""), +this.form.value.endHour.replace(":", ""), this.form.value.motif, this.form.value.commentd);
        concat(
            this.disponibilityService.addDisponibilityToRoom(this.form.value.classRoomId, arg).pipe(switchMapTo(EMPTY)),
            timer(1000).pipe(switchMapTo(EMPTY)),
            this.roomService.getDisponibilitiesRooms(0)
        ).subscribe((data: any) => {
            this.rooms = data.body.content;
            this.totalRecords = data.totalItems;
            this.modalService.dismissAll();
            this.spinner.hide();
        }, error => {
            this.spinner.hide();
            if (error.error.code === 706) {
                this.showLoaderError = true;
                this.saveError = "La Salle déjà existe avec ce date";
                timer(2000).subscribe(() => this.showLoaderError = false);
            }
        })
    }

    public updateDisponibility() {
        this.spinner.show();
        let arg = new CreateDisponibilityRequest(this.form.value.startDate, this.form.value.endDate, +this.form.value.startHour.replace(":", ""), +this.form.value.endHour.replace(":", ""), this.form.value.motif, this.form.value.commentd, this.currentDispo.disponibilityId);
        concat(
            this.disponibilityService.updateDisponibility(arg).pipe(switchMapTo(EMPTY)),
            timer(1000).pipe(switchMapTo(EMPTY)),
            this.roomService.getDisponibilitiesRooms(0)
        ).subscribe((data: any) => {
            this.rooms = data.body.content;
            this.totalRecords = data.totalItems;
            this.modalService.dismissAll();
            this.spinner.hide();
        }, error => {
            this.spinner.hide();
            if (error.error.code === 706) {
                this.showLoaderError = true;
                this.saveError = "La Salle déjà existe avec ce date";
                timer(2000).subscribe(() => this.showLoaderError = false);
            }
        })
    }

    public deleteDisponibility(classRoomId, disponibilityId) {
        this.spinner.show();
        concat(
            this.disponibilityService.deleteDisponibilityFromRoom(classRoomId, disponibilityId).pipe(switchMapTo(EMPTY)),
            timer(1000).pipe(switchMapTo(EMPTY)),
            this.roomService.getDisponibilitiesRooms(0)
        ).subscribe((data: any) => {
            this.rooms = data.body.content;
            this.totalRecords = data.totalItems;
            this.spinner.hide();
            this.modalService.dismissAll();
        }, error => {
            this.spinner.hide();
            if (error.error.code == "709") {
                this.modalService.dismissAll();
                this.toastr.error(error.error.errors[0], null, {
                    timeOut: 3000, closeButton: true
                });
            }
        })
    }

    public hourFormat(hour): any {
        let result;
        if (hour) {
            if (hour.toString().length === 3) {
                result = "0" + hour.toString();
            } else if (hour.toString().length === 1) {
                result = "000" + hour.toString();
            } else if (hour.toString().length === 2) {
                result = "00" + hour.toString();
            } else {
                result = hour.toString();
            }
            result = result.substring(0, 2) + "H:" + result.substring(2, 4);
        }
        return result;
    }

    public initDisponibiliesColomns(): any[] {
        return [
            {
                header: "salle",
                field: "classRoomId"
            },
            {
                header: "Date début",
                field: "startDate",
                date: true,
                format: 'dd/MM/yyyy'
            },
            {
                header: "Date fin",
                field: "endDate",
                date: true,
                format: 'dd/MM/yyyy'
            },
            {
                header: "Heure début",
                field: "startHour",
                hour: true
            },
            {
                header: "Heure fin",
                field: "endHour",
                hour: true
            },
            {
                header: "Motif",
                field: "motif"
            },
            {
                header: "Commentaire",
                field: "commentd"
            }
        ]
    }

    private checkDate(): ValidatorFn {
        return (c: AbstractControl): { [key: string]: boolean } | null => {
            if (c.get("startDate") && c.get("endDate")) {
                let startDateActivity = c.get("startDate").value;
                let startDateLegalStatus = c.get("endDate").value;

                if (startDateLegalStatus && startDateActivity && Dates.toDate(startDateLegalStatus).date.getTime() < Dates.toDate(startDateActivity).date.getTime()) {
                    return { 'dateInvalid': true };
                }
            }
            if (c.get("startHour") && c.get("endHour")) {
                let startDateActivity = c.get("startHour").value;
                let startDateLegalStatus = c.get("endHour").value;
                let startHour = startDateActivity ? +startDateActivity.replace(":", "") : null;
                let endHour = startDateLegalStatus ? +startDateLegalStatus.replace(":", "") : null;
                if (startHour > endHour) {
                    return { 'hourInvalid': true };
                }
            }
            return null;
        }
    }

    private initForm(): void {
        this.form = this.fb.group({
            classRoomId: [null, Validators.required],
            startDate: [null, Validators.required],
            endDate: [null, Validators.required],
            startHour: [null, Validators.required],
            endHour: [null, Validators.required],
            motif: [null],
            commentd: [null]
        })
    }

}

export class CreateDisponibilityRequest {
    public startDate: Date;
    public endDate: Date;
    public startHour: number;
    public endHour: number;
    public motif: string;
    public commentd: string;
    public disponibilityId: string;

    constructor(startDate: Date, endDate: Date, startHour: number, endHour: number, motif: string, commentd: string, disponibilityId?: string) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.startHour = startHour;
        this.endHour = endHour;
        this.motif = motif;
        this.commentd = commentd;
        this.disponibilityId = disponibilityId;
    }
}
