import { DatePipe } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, timer } from 'rxjs';
import { ExamTypeEnum } from 'src/app/core/models';
import { Teacher } from 'src/app/core/models/teacher.model';
import { ClassService, RefService, TeacherService } from 'src/app/core/services';
import { ActionEnum } from 'src/app/shared/components/cm-table-container/models/config-column.model';
import { DataValue } from 'src/app/shared/components/cm-table-container/models/data-value.model';

@Component({
    templateUrl: './assign-modal.component.html',
    styleUrls: ['./assign-modal.component.scss'],
    encapsulation: ViewEncapsulation.None,
    providers: [DatePipe]
})
export class AssignModalComponent implements OnInit {

    @Output()
    public triggerSave: EventEmitter<DataValue> = new EventEmitter();

    @Input()
    public update: boolean;

    @Input()
    public data: any;

    @Input()
    public moduleId: string;

    public form: FormGroup;

    public saveError: string;
    public saveSuccess: string;

    public bufferSize = 20;
    public numberOfItemsFromEndBeforeFetchingMore = 10;

    public loading = false;
    public inProgress: boolean = false;

    public disabledPeriod: boolean = true;

    public periods: any[];
    public examTypes: string[];
    public teachersBuffer = [];

    public semesters$: Observable<any[]>;
    public typeExam$: Observable<any[]>;
    public class$: Observable<any[]>;
    public teachers: Teacher[];

    constructor(private fb: FormBuilder,
        private activeModal: NgbActiveModal,
        private datePipe: DatePipe,
        private classService: ClassService,
        private teacherService: TeacherService,
        private refService: RefService) {
    }

    ngOnInit() {
        this.initForm();
        this.semesters$ = this.refService.getSemeters();
        this.typeExam$ = this.refService.getTypeExam();
        this.examTypes = Object.keys(ExamTypeEnum).map(key => ExamTypeEnum[key]);
        this.class$ = this.classService.getClasses();
        this.teacherService.getTeachers().subscribe(teachers => {
            this.teachers = teachers;
            this.teachersBuffer = this.teachers.slice(0, this.bufferSize);
        });
        if (this.update === true) {
            this.disabledPeriod = false;
            this.onChangeSemester(this.data.semester ? this.mapSemeter(this.data.semester) : null);
            this.form.patchValue({
                coefficient: this.data.coefficient,
                nbrHour: this.data.nbrHour,
                semesterId: this.data.semester.semestreId,
                periodId: this.data.periods ? this.data.periods.map(item => item.periodId) : null,
                classId: this.data.classs ? this.data.classs.classId : null,
                teacherIds: this.data.teachers ? this.data.teachers.map(item => item.teacherId) : null,
                typeExam: this.data.typeExam
            })
        }
    }

    public mapSemeter(semester: any): any {
        return {
            semestreId: semester.semestreId,
            label: (semester.code == "1" ? "S1" : "S2") + " - " + semester.startDate + " à " + semester.endDate,
            code: semester.code,
            startDate: semester.startDate,
            endDate: semester.endDate,
            periods: this.mapPeriods(semester.periods)
        };
    }

    private mapPeriods(periods: any[]): any[] {
        let result: any[] = [];
        if (periods) {
            periods.forEach(period => {
                result.push({
                    periodId: period.periodId ? period.periodId : null,
                    label: period.code ? (period.code == "1" ? "P1" : "P2") + " - " + period.startDate + " à " + period.endDate : null,
                    code: period.code ? period.code : null,
                    startDate: period.startDate ? period.startDate : null,
                    endDate: period.endDate ? period.endDate : null
                })
            })
        }
        return result;
    }

    private fetchMore() {
        const len = this.teachersBuffer.length;
        const more = this.teachers.slice(len, this.bufferSize + len);
        this.loading = true;
        setTimeout(() => {
            this.loading = false;
            this.teachersBuffer = this.teachersBuffer.concat(more);
        }, 200)
    }

    onScrollToEnd() {
        this.fetchMore();
    }

    onScroll({ end }) {
        if (this.loading || this.teachers.length <= this.teachersBuffer.length) {
            return;
        }

        if (end + this.numberOfItemsFromEndBeforeFetchingMore >= this.teachersBuffer.length) {
            this.fetchMore();
        }
    }

    public setIsSaved(event) {
        if (event.isSaved === true) {
            timer(1000).subscribe(() => this.reset());
        } else {
            if (event.code === 701) {
            }
        }
    }

    public save() {
        if (this.form.valid) {
            if (this.update === true) {
                let arg = this.form.value
                arg.assignId = this.data.assignId;
                let dataValue: DataValue = { action: ActionEnum.UPDATE, value: arg };
                this.triggerSave.emit(dataValue);
            } else {
                let arg = this.form.value
                arg.moduleId = this.moduleId;
                let dataValue: DataValue = { action: ActionEnum.CREATE, value: arg };
                this.triggerSave.emit(dataValue);
            }
        }
    }

    public onChangeSemester(event) {
        if (event) {
            this.disabledPeriod = false;
            this.periods = event.periods;
        } else {
            this.disabledPeriod = true;
            this.form.get("periodId").setValue(null);
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
            coefficient: [null, Validators.pattern(/^[0-9]+(.[0-9]{0,2})?$/)],
            nbrHour: [null, Validators.pattern(/^[0-9]+(.[0-9]{0,2})?$/)],
            semesterId: [null],
            periodId: [null],
            classId: [null],
            typeExam: [null],
            teacherIds: [null]
        })
    }

}