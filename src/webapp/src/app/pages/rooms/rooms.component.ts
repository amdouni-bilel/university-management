import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { concat, EMPTY, timer } from "rxjs";
import { switchMapTo } from "rxjs/operators";
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { RoomService } from 'src/app/core/services';
import { RemovePopupComponent } from 'src/app/shared/components/comfirmation-popup/remove/remove-popup.component';
import { DataValue } from 'src/app/shared/components/cm-table-container/models/data-value.model';
import { ActionEnum } from 'src/app/shared/components/cm-table-container/models/config-column.model';
import { RoomModalComponent } from './room-modal/room-modal.component';

@Component({
    selector: 'rooms',
    templateUrl: './rooms.component.html',
    styleUrls: ['./rooms.component.scss'],
    providers: [DatePipe]
})
export class RoomsComponent implements OnInit {

    public currentRoom: any;

    public roomsLength: number;

    public rooms: any[];

    constructor(private modalService: NgbModal,
        private spinner: NgxSpinnerService,
        private roomService: RoomService) {
    }

    ngOnInit() {
        this.spinner.show();
        this.roomService.getRooms().subscribe((rooms: any[]) => {
            this.rooms = rooms;
            this.spinner.hide();
        });
    }

    public openModal(event?: any) {
        const modal: NgbModalRef = this.modalService.open(RoomModalComponent,
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
        }
        modal.componentInstance.triggerSave.subscribe((dataValue: DataValue) => {
            this.spinner.show();
            if (dataValue.action === ActionEnum.UPDATE) {
                concat(
                    this.roomService.updateRoom(dataValue.value).pipe(switchMapTo(EMPTY)),
                    timer(1000).pipe(switchMapTo(EMPTY)),
                    this.roomService.getRooms()
                ).subscribe((rooms: any[]) => {
                    this.spinner.hide();
                    this.rooms = rooms;
                    this.roomsLength = rooms.length;
                    modal.componentInstance.setIsSaved({ isUpdated: true });
                }, error => {
                    this.spinner.hide();
                    if (error.error.code === 701) {
                        modal.componentInstance.setIsSaved({ isSaved: false, code: error.error.code });
                    }
                })
            } else if (dataValue.action === ActionEnum.CREATE) {
                concat(
                    this.roomService.addRoom(dataValue.value).pipe(switchMapTo(EMPTY)),
                    timer(1000).pipe(switchMapTo(EMPTY)),
                    this.roomService.getRooms()
                ).subscribe((rooms: any[]) => {
                    this.spinner.hide();
                    this.rooms = rooms;
                    this.roomsLength = rooms.length;
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

    public openDeleteModal(event) {
        this.currentRoom = event;
        const modal: NgbModalRef = this.modalService.open(RemovePopupComponent,
            {
                size: 'lg',
                ariaLabelledBy: 'modal-basic-title',
                keyboard: false,
                backdrop: 'static',
                centered: true
            });
        modal.componentInstance.config = { title: "Confirmation de suppression", message: "Est-ce que vous confirmez la suppression définitive ?" };
        modal.componentInstance.sendData.subscribe((data: boolean) => this.deleteClass(this.currentRoom.classRoomId));
    }

    private deleteClass(event): void {
        this.spinner.show();
        concat(
            this.roomService.deleteRoom(event).pipe(switchMapTo(EMPTY)),
            timer(1000).pipe(switchMapTo(EMPTY)),
            this.roomService.getRooms()
        ).subscribe((rooms: any[]) => {
            this.spinner.hide();
            this.rooms = rooms;
            this.roomsLength = rooms.length;
            this.modalService.dismissAll();
        })
    }

    public initRoomsColomns(): any[] {
        return [
            {
                header: "Salle",
                field: "classRoomId"
            },
            {
                header: "Capacité",
                field: "capacity"
            },
            {
                header: "Site",
                field: "pole"
            },
            {
                header: "Bloc",
                field: "bloc"
            }
        ]
    }

}