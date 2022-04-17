import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { timer } from 'rxjs';
import { ClassService } from 'src/app/core/services';
import { ActionEnum } from 'src/app/shared/components/cm-table-container/models/config-column.model';
import { DataValue } from 'src/app/shared/components/cm-table-container/models/data-value.model';

@Component({
    templateUrl: './student-modal.component.html',
    styleUrls: ['./student-modal.component.scss']
})
export class StudentModalComponent implements OnInit {

    @Output()
    public triggerSave: EventEmitter<DataValue> = new EventEmitter();

    @Input()
    public update: boolean;

    @Input()
    public data: any;

    public form: FormGroup;

    public saveError: string;
    public saveSuccess: string;

    public showLoaderSuccess: boolean = false;
    public showLoaderError: boolean = false;

    public classes: any[];
    public groups: string[] = ["Groupe 1", "Groupe 2"];

    constructor(private fb: FormBuilder,
        private classService: ClassService,
        private activeModal: NgbActiveModal) {
    }

    ngOnInit() {
        this.initForm();
        this.classService.getClasses().subscribe(classes => this.classes = classes);
        if (this.update === true) {
            this.form.patchValue({
                firstName: this.data.firstName,
                lastName: this.data.lastName,
                email: this.data.email,
                cin: this.data.cin,
                groupId: this.data.group.label,
                classId: this.data.classs.classId
            })
        }
    }

    public setIsSaved(event) {
        if (event.isSaved === true) {
            this.showLoaderSuccess = true;
            this.saveSuccess = "L'étudiant " + this.form.get("firstName").value + " ajouté avec succès";
            timer(1000).subscribe(() => this.reset());
        } else if (event.isUpdated === true) {
            this.showLoaderSuccess = true;
            this.saveSuccess = "L'étudiant " + this.form.get("firstName").value + " modifié avec succès";
            timer(1000).subscribe(() => this.reset());
        } else if (event.isSaved === false) {
            if (event.code === 701) {
                this.showLoaderError = true;
                this.saveError = "L'étudiant " + this.form.get("firstName").value + "  déjà existe";
                timer(2000).subscribe(() => this.showLoaderError = false);
            }
        }
    }

    public save() {
        if (this.form.valid) {
            if (this.update) {
                let arg = this.form.value
                arg.studentId = this.data.studentId;
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
            firstName: [null, Validators.required],
            lastName: [null, Validators.required],
            email: [null, [Validators.required, Validators.pattern("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")]],
            cin: [null, Validators.required],
            groupId: [null],
            classId: [null, Validators.required]
        })
    }

}