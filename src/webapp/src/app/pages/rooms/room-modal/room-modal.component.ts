import { DatePipe } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, timer } from 'rxjs';
import { PoleEnum } from 'src/app/core/models';
import { RefService } from 'src/app/core/services';
import { ActionEnum } from 'src/app/shared/components/cm-table-container/models/config-column.model';
import { DataValue } from 'src/app/shared/components/cm-table-container/models/data-value.model';

@Component({
    templateUrl: './room-modal.component.html',
    styleUrls: ['./room-modal.component.scss'],
    encapsulation: ViewEncapsulation.None,
    providers: [DatePipe]
})
export class RoomModalComponent implements OnInit {

    @Output()
    public triggerSave: EventEmitter<DataValue> = new EventEmitter();

    @Input()
    public update: boolean;

    @Input()
    public data: any;

    public form: FormGroup;

    public saveError: string;
    public saveSuccess: string;

    public showLoaderError: boolean = false;
    public showLoaderSuccess: boolean = false;
    public disabledBloc: boolean = true;
    public inProgress: boolean = false;

    public periods: any[];
    public weeks: any[];
    public sites$: string[];
    public blocs$: string[];

    public semesters$: Observable<any[]>;
    public seances$: Observable<any[]>;
    public days$: Observable<any[]>;

    constructor(private fb: FormBuilder,
        private activeModal: NgbActiveModal,
        private refService: RefService) {
    }

    ngOnInit() {
        this.initForm();
        this.sites$ = Object.keys(PoleEnum).map(key => PoleEnum[key]);
        if (this.update === true) {
            this.disabledBloc = false;
            this.refService.getBlocs().subscribe(item => {
                let result = item.find(el => el.pole === Object.keys(PoleEnum)[Object.values(PoleEnum).indexOf(this.data.pole)]);
                this.blocs$ = result.blocs;
            })
            this.form.patchValue({
                classRoomId: this.data.classRoomId,
                capacity: this.data.capacity,
                pole: this.data.pole,
                bloc: this.data.bloc,
            })
            this.form.controls["classRoomId"].disable();
        } else {
            this.form.controls["classRoomId"].enable();
        }
    }

    public setIsSaved(event) {
        if (event.isSaved === true) {
            this.showLoaderSuccess = true;
            this.saveSuccess = "La Salle " + this.form.get("classRoomId").value + " ajouté avec succès";
            timer(1000).subscribe(() => this.reset());
        } else if (event.isUpdated === true) {
            this.showLoaderSuccess = true;
            this.saveSuccess = "La Salle " + this.form.get("classRoomId").value + " modifié avec succès";
            timer(1000).subscribe(() => this.reset());
        } else if (event.isSaved === false) {
            if (event.code === 701) {
                this.showLoaderError = true;
                this.saveError = "La Salle " + this.form.get("classRoomId").value + "  déjà existe";
                timer(2000).subscribe(() => this.showLoaderError = false);
            } else if (event.code === 706) {
                this.showLoaderError = true;
                this.saveError = "La Salle déjà existe avec ce date";
                timer(2000).subscribe(() => this.showLoaderError = false);
            }
        }
    }

    public save() {
        if (this.form.valid) {
            if (this.update) {
                let arg = this.form.value
                arg.classRoomId = this.data.classRoomId;
                let dataValue: DataValue = { action: ActionEnum.UPDATE, value: arg };
                this.triggerSave.emit(dataValue);
            } else {
                let dataValue: DataValue = { action: ActionEnum.CREATE, value: this.form.value };
                this.triggerSave.emit(dataValue);
            }
        }
    }

    public onChangeSite(event): void {
        if (event) {
            this.disabledBloc = false;
            this.refService.getBlocs().subscribe(item => {
                let result = item.find(el => el.pole === Object.keys(PoleEnum)[Object.values(PoleEnum).indexOf(event)]);
                this.blocs$ = result.blocs;
            })
        } else {
            this.disabledBloc = true;
            this.form.get("blocId").setValue(null);
            this.blocs$ = [];
        }
    }


    public reset() {
        this.form.reset();
        this.closeModal();
    }

    public closeModal(): void {
        this.activeModal.close();
    }

    private initForm(): void {
        this.form = this.fb.group({
            classRoomId: [null, Validators.required],
            capacity: [null, Validators.required],
            pole: [null, Validators.required],
            bloc: [null, Validators.required]
        })
    }

}