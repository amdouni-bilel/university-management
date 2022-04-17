import { Component, OnInit } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { concat, EMPTY, timer } from "rxjs";
import { switchMapTo } from "rxjs/operators";
import { RefService, TeacherService } from 'src/app/core/services';
import { ActionEnum } from 'src/app/shared/components/cm-table-container/models/config-column.model';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Observable } from 'rxjs';
import { DataValue } from 'src/app/shared/components/cm-table-container/models/data-value.model';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { TeacherModalComponent } from './teacher-modal/teacher-modal.component';

@Component({
    templateUrl: './teachers.component.html',
    styleUrls: ['./teachers.component.scss']
})
export class TeachersComponent implements OnInit {

    public form: FormGroup;

    public teachers: any[];
    public cols: any[];

    public departements$: Observable<any[]>;

    constructor(private fb: FormBuilder,
        private modalService: NgbModal,
        private spinner: NgxSpinnerService,
        private refService: RefService,
        private teacherService: TeacherService) {
    }

    ngOnInit() {
        this.cols = [
            {
                header: "N°",
                field: "teacherId"
            },
            {
                header: "Enseignant",
                field: "fullName"
            },
            {
                header: "Téléphones",
                field: "phone"
            },
            {
                header: "E-mail",
                field: "email"
            },
            {
                header: "Département",
                field: "departement"
            },
            {
                header: "UP",
                field: "up"
            },
            {
                header: "Nbr surveillances",
                field: "nbr"
            },
            {
                header: "Nbr-H surveiller",
                field: "nbrHour"
            }
        ];
        this.initForm();
        this.spinner.show();
        this.departements$ = this.refService.getDepartements();
        this.teacherService.getTeachers().subscribe((teachers: any[]) => {
            this.teachers = this.mapTeachers(teachers);
            this.spinner.hide();
        })
    }

    private mapTeachers(teachers): any {
        let data: any[] = [];
        teachers.forEach(teacher => {
            let els = teacher.disponibilities.filter(item => item.motif == "Surveillance");
            if (els && els.length > 0) {
                let result = els.map(item => {
                    let endHour = new Date(null);
                    endHour.setHours(item.endHour.toString().length == 3 ? +item.endHour.toString().substring(0, 1) : +item.endHour.toString().substring(0, 2));
                    endHour.setMinutes(item.endHour.toString().length == 3 ? +item.endHour.toString().substring(1, 3) : +item.endHour.toString().substring(2, 4));
                    endHour.setSeconds(0);
                    let startHour = new Date(null);
                    startHour.setHours(item.startHour.toString().length == 3 ? +item.startHour.toString().substring(0, 1) : +item.startHour.toString().substring(0, 2));
                    startHour.setMinutes(item.startHour.toString().length == 3 ? +item.startHour.toString().substring(1, 3) : +item.startHour.toString().substring(2, 4));
                    startHour.setSeconds(0);
                    console.log(item.startHour);
                    console.log(item.endHour);
                    return (Math.abs(startHour.getTime() - endHour.getTime()) / 3600000).toString()
                });
                console.log(result);

                let xx = result.reduce((accumulator, currentValue) => +accumulator + +currentValue);

                let hours = parseInt(xx), minutes = (+xx - hours) * 60;

                data.push({
                    teacherId: teacher.teacherId,
                    fullName: teacher.fullName,
                    email: teacher.email,
                    phone: teacher.phone,
                    up: teacher.up,
                    departement: teacher.departement ? teacher.departement.label : null,
                    nbr: result.length,
                    nbrHour: hours + 'h:' + minutes.toFixed(0) + 'min'
                })
            } else {
                data.push({
                    teacherId: teacher.teacherId,
                    fullName: teacher.fullName,
                    email: teacher.email,
                    phone: teacher.phone,
                    up: teacher.up,
                    departement: teacher.departement ? teacher.departement.label : null,
                    nbr: 0,
                    nbrHour: 0
                })
            }
        })
        return data;
    }

    public onSearch() {
        if (this.form.valid) {
            this.spinner.show();
            this.teacherService.searchTeachers(this.form.value).subscribe(teachers => {
                this.spinner.hide();
            });
        }
    }

    public openModal() {
        const modal: NgbModalRef = this.modalService.open(TeacherModalComponent,
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
                    this.teacherService.addTeacher(dataValue.value).pipe(switchMapTo(EMPTY)),
                    timer(1000).pipe(switchMapTo(EMPTY)),
                    this.teacherService.getTeachers()
                ).subscribe((teachers: any[]) => {
                    this.spinner.hide();
                    this.teachers = this.mapTeachers(teachers);
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

    public reset() {
        this.form.reset();
        this.spinner.show();
        this.teacherService.getTeachers().subscribe(teachers => {
            this.teachers = this.mapTeachers(teachers);
            this.spinner.hide();
        });
    }

    private initForm(): void {
        this.form = this.fb.group({
            teacherId: [null],
            cin: [null],
            departement: [null],
            classs: [null]
        })
    }

}