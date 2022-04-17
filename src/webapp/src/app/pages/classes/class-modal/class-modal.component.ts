import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, timer } from 'rxjs';
import { Speciality } from 'src/app/core/models/speciality.model';
import { RefService } from 'src/app/core/services';
import { ActionEnum } from 'src/app/shared/components/cm-table-container/models/config-column.model';
import { DataValue } from 'src/app/shared/components/cm-table-container/models/data-value.model';

@Component({
    templateUrl: './class-modal.component.html',
    styleUrls: ['./class-modal.component.scss']
})
export class ClassModalComponent implements OnInit {

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

    public specialities$: Observable<Speciality[]>;

    constructor(private fb: FormBuilder,
        private activeModal: NgbActiveModal,
        private refService: RefService) {
    }

    ngOnInit() {
        this.initForm();
        this.specialities$ = this.refService.getSpecialities();
        if (this.update === true) {
            this.form.patchValue({
                classId: this.data.classId,
                email: this.data.email,
                label: this.data.label,
                nbrStudents: this.data.nbrStudents,
                speciality: this.data.speciality ? this.data.speciality.specialityId : null,
                category: this.data.category,
                group: this.data.group
            })
        }
    }

    public setIsSaved(event) {
        if (event.isSaved === true) {
            this.showLoaderSuccess = true;
            this.saveSuccess = "La classe " + this.form.get("label").value + " ajouté avec succès";
            timer(1000).subscribe(() => this.reset());
        } else if (event.isUpdated === true) {
            this.showLoaderSuccess = true;
            this.saveSuccess = "La classe " + this.form.get("label").value + " modifié avec succès";
            timer(1000).subscribe(() => this.reset());
        } else if (event.isSaved === false) {
            if (event.code === 701) {
                this.showLoaderError = true;
                this.saveError = "La classe " + this.form.get("label").value + "  déjà existe";
                timer(2000).subscribe(() => this.showLoaderError = false);
            }
        }
    }

    public save() {
        if (this.form.valid) {
            if (this.update) {
                let arg = this.form.value
                arg.classId = this.data.classId;
                let dataValue: DataValue = { action: ActionEnum.UPDATE, value: arg };
                this.triggerSave.emit(dataValue);
            } else {
                let dataValue: DataValue = { action: ActionEnum.CREATE, value: this.form.value };
                this.triggerSave.emit(dataValue);
            }
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
            classId: [null, Validators.required],
            label: [null],
            email: [null, [Validators.required, Validators.pattern('^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$')]],
            speciality: [null, Validators.required],
            category: [null, Validators.required],
            group: [null]
        })
    }

}