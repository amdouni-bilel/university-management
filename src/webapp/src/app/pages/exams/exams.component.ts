import { DatePipe } from '@angular/common';
import { AbstractType, Component, ElementRef, Input, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { concat, EMPTY, Observable, timer } from "rxjs";
import { switchMapTo } from "rxjs/operators";
import * as _ from 'lodash';
import { ExamService, ExcelExamService, RefService, RoomService, StudentService, TeacherService, TokenService } from 'src/app/core/services';
import { ActionEnum } from 'src/app/shared/components/cm-table-container/models/config-column.model';
import { DataValue } from 'src/app/shared/components/cm-table-container/models/data-value.model';
import { RemovePopupComponent } from 'src/app/shared/components/comfirmation-popup/remove/remove-popup.component';
import { ExamModalComponent } from './exam-modal/exam-modal.component';
import jsPDF from "jspdf";
import "jspdf-autotable";

@Component({
    templateUrl: './exams.component.html',
    styleUrls: ['./exams.component.scss'],
    encapsulation: ViewEncapsulation.None,
    providers: [DatePipe]
})
export class ExamsComponent implements OnInit {

    @ViewChild('content')
    private content: ElementRef;

    public form: FormGroup;

    public saveError: string;
    public saveSuccess: string;
    public currentExam: any;
    public roles: string[];

    public nbrTeachers: number;

    public disabledDate: boolean = true;
    public isStudent: boolean = false;
    public isTeacher: boolean = false;
    public showLoaderError: boolean = false;
    public showLoaderSuccess: boolean = false;

    public nbrSurveillance: number = 0;
    public houreSurveillance: string;

    public exams: any[];
    public existClasses: any[] = [];
    public existDates: any[] = [];
    public teachers: any;
    public rooms: any;
    public sessionMail: any;

    private updatePopup: NgbModalRef;

    public emailClasses: any[];
    public classesEmail: any[];
    public sessions: any[];
    public dataSented: any[] = [];

    public department$: Observable<any[]>;
    public blocs$: Observable<any[]>;

    public exportColumns: any[];
    public cols: any[];
    public _selectedColumns: any[];

    @Input() get selectedColumns(): any[] {
        return this._selectedColumns;
    }

    set selectedColumns(val: any[]) {
        this._selectedColumns = this.cols.filter(col => val.includes(col));
    }

    constructor(private modalService: NgbModal,
        private spinner: NgxSpinnerService,
        private fb: FormBuilder,
        private refService: RefService,
        private studentService: StudentService,
        private excelExamService: ExcelExamService,
        private examService: ExamService,
        private roomService: RoomService,
        private datePipe: DatePipe,
        private tokenService: TokenService,
        private teacherService: TeacherService) {
    }

    ngOnInit() {
        this.spinner.show();
        this.refService.getLevels().subscribe(levels => {
            let sessions: any[] = [];
            levels.forEach(level => sessions.push(level.session));
            this.sessions = _.uniqBy([...new Set([].concat.apply([], sessions))], 'label');
        })
        this.tokenService.getRoles().then((data: any[]) => {
            this.roles = data;
            if (data.includes('STUDENT')) {
                this.isStudent = true;
                this.cols = [
                    { field: 'day', header: 'Jour' },
                    { field: 'examDate', header: 'Date', type: "date", format: 'dd/MM/yyyy' },
                    { field: 'examHour', header: 'Heure' },
                    { field: 'examDuration', header: 'Durée' },
                    { field: 'module', header: 'Module', type: "object", subField: 'designation' },
                    { field: 'dsex', header: 'DSEX' },
                    { field: 'type', header: 'Type' },
                    { field: 'currentTechers', header: 'Enseigant', type: "function", format: (teachers) => this.displayCurrentTechers(teachers) },
                    { field: 'class', header: 'Classe', type: "object", subField: 'classId' },
                    { field: 'group', header: 'Groupe', type: "object", subField: 'label' }
                ];
                this.studentService.getCurrentStudent().subscribe((data: any) => {
                    this.examService.getExams().subscribe(exams => {
                        if (exams && exams.length > 0) {
                            let array = exams.map(exam => exam.classes.map(cl => cl.classId));
                            let result: any[] = [];
                            array.forEach(item => {
                                item.forEach(element => {
                                    result.push(element);
                                });
                            })
                            this.existClasses = _.uniq(result, true);
                            this.initTable(exams).then(ex => {
                                this.exams = ex.filter(el => el.class.classId === data.classs.classId && el.group.groupId === data.group.groupId);
                            });
                        }
                        this.spinner.hide();
                    });
                })
            } else if (data.includes('TEACHER')) {
                this.isTeacher = true;
                this.cols = [
                    { field: 'day', header: 'Jour' },
                    { field: 'examDate', header: 'Date', type: "date", format: 'dd/MM/yyyy' },
                    { field: 'examHour', header: 'Heure' },
                    { field: 'examDuration', header: 'Durée' },
                    { field: 'module', header: 'Module', type: "object", subField: 'designation' },
                    { field: 'dsex', header: 'DSEX' },
                    { field: 'type', header: 'Type' },
                    { field: 'currentTechers', header: 'Enseigant', type: "function", format: (teachers) => this.displayCurrentTechers(teachers) },
                    { field: 'class', header: 'Classe', type: "object", subField: 'classId' },
                    { field: 'group', header: 'Groupe', type: "object", subField: 'label' },
                    { field: 'rooms', header: 'Salle', type: "function", format: (rooms) => this.displayRooms(rooms) },
                    { field: 'teachers', header: 'Surveillant', type: "function", format: (teachers) => this.displayTeachers(teachers) }
                ];
                this.teacherService.getCurrentTeacher().subscribe((data: any) => {
                    this.examService.getExams().subscribe((items: any[]) => {
                        if (items && items.length > 0) {
                            let array = items.map(exam => exam.classes.map(cl => cl.classId));
                            let result: any[] = [];
                            array.forEach(item => {
                                item.forEach(element => {
                                    result.push(element);
                                });
                            })
                            this.existClasses = _.uniq(result, true);
                            this.initTable(items).then(ex => {
                                this.exams = ex.filter(el => el.teachers.some(t => t.teacherId === data.teacherId));
                                this.nbrSurveillance = this.exams.length;
                                let num = this.exams.map(it => +it.examDuration.replace('min', '')).reduce((a, b) => a + b, 0);
                                var hours = (num / 60);
                                var rhours = Math.floor(hours);
                                var minutes = (hours - rhours) * 60;
                                var rminutes = Math.round(minutes);
                                this.houreSurveillance = rhours + " heure(s) et " + rminutes + " minute(s).";
                            });
                        }
                        this.spinner.hide();
                    });
                })
            } else {
                this.isStudent = false;
                this.isTeacher = false;
                this.cols = [
                    { field: 'day', header: 'Jour' },
                    { field: 'examDate', header: 'Date', type: "date", format: 'dd/MM/yyyy' },
                    { field: 'examHour', header: 'Heure' },
                    { field: 'examDuration', header: 'Durée' },
                    { field: 'module', header: 'Module', type: "object", subField: 'designation' },
                    { field: 'dsex', header: 'DSEX' },
                    { field: 'type', header: 'Type' },
                    { field: 'currentTechers', header: 'Enseigant', type: "function", format: (teachers) => this.displayCurrentTechers(teachers) },
                    { field: 'class', header: 'Classe', type: "object", subField: 'classId' },
                    { field: 'group', header: 'Groupe', type: "object", subField: 'label' },
                    { field: 'effective', header: 'Effectif' },
                    { field: 'rooms', header: 'Salle', type: "function", format: (rooms) => this.displayRooms(rooms) },
                    { field: 'teachers', header: 'Surveillant', type: "function", format: (teachers) => this.displayTeachers(teachers) }
                ];
                this.examService.getExams().subscribe(exams => {
                    if (exams && exams.length > 0) {
                        let array = exams.map(exam => exam.classes.map(cl => cl.classId));
                        let result: any[] = [];
                        array.forEach(item => {
                            item.forEach(element => {
                                result.push(element);
                            });
                        })
                        this.existClasses = _.uniq(result, true);
                        this.initTable(exams).then(data => this.exams = data);
                    }
                    this.spinner.hide();
                });
                this.form = this.fb.group({
                    teachers: [null],
                    rooms: [null],
                    bloc: [null, Validators.required],
                    up: [null, Validators.required]
                })
            }
            this._selectedColumns = this.cols.filter(col => col.field !== 'dsex' && col.field !== 'type' && col.field !== 'currentTechers' && col.field !== 'effective');
        })
    }

    public onChangeSession(event) {
        if (event && event.length > 0) {
            let list = event.map(item => item.label);
            if (this.exams) {
                let exams = this.exams.filter(item => list.includes(item.session));
                let array = exams.map(exam => exam.class.classId);
                this.classesEmail = _.uniq(array, true);
            }
        } else {
            this.classesEmail = [];
        }
    }

    public onRemoveSession(event) {
        this.emailClasses = [];
    }

    public displayRooms(rooms?): any[] {
        return rooms ? rooms.map(item => item.classRoomId).toString() : null;
    }

    public displayTeachers(teachers?): any[] {
        return teachers ? teachers.map(item => item.fullName).toString() : null;
    }

    public displayCurrentTechers(currentTechers?): any[] {
        return currentTechers ? currentTechers.map(item => item.fullName).toString() : null;
    }

    public onChangeClass(event): void {
        this.disabledDate = false;
        let result = this.exams.filter(exam => event.includes(exam.class.classId));
        this.existDates = result.map(exam => this.datePipe.transform(exam.examDate, "dd/MM/yyyy")).filter(function (elem, index, self) {
            return index === self.indexOf(elem);
        });
    }

    private async initTable(exams): Promise<any[]> {
        let result: any[] = [];
        for (let exam of exams) {
            let session: string;
            if (exam.session === "S1P1" || exam.session === "S2P1") {
                session = "DS"
            } else if (exam.session === "S1P2" || exam.session === "S2P2" || exam.session === "SP") {
                session = "Examen"
            } else if (exam.session === "SR") {
                session = "Rattrapage"
            }
            for (let item of exam.classes) {

                let currentTechers: any[] = []
                let assign = exam.module.assignClasses.filter(el => el.classs && el.classs.classId === item.classId)[0];
                if (assign && assign.teachers) {
                    assign.teachers.forEach(te => {
                        currentTechers.push({ teacherId: te.teacherId, fullName: te.fullName })
                    });
                }
                let resultGrps = item.groups.filter(group => group.teachers && group.teachers.some(el => el.examIds.includes(exam.examId)) && group.teachers.length > 0 && group.rooms && group.rooms.some(el => el.examIds.includes(exam.examId)) && group.rooms.length > 0);
                for (let group of resultGrps) {
                    let gt = group.teachers.filter(el => el.examIds.includes(exam.examId));
                    const uniqueTeacher = Array.from(new Set(gt.map(a => a.teacherId))).map(id => { return gt.find(a => a.teacherId === id) })

                    let gr = group.rooms.filter(el => el.examIds.includes(exam.examId));
                    const uniqueRoom = Array.from(new Set(gr.map(a => a.classRoomId))).map(id => { return gr.find(a => a.classRoomId === id) })

                    let nbr = await this.studentService.countByClassAndGroup(item.classId, group.groupId).toPromise();
                    result.push({
                        examId: exam.examId,
                        session: exam.session,
                        day: this.getDayName(exam.examDate),
                        examDate: exam.examDate,
                        examHour: exam.examHour ? (exam.examHour.toString().length === 4 ? exam.examHour.toString().substring(0, 2) + "H:" + exam.examHour.toString().substring(2, 4) : exam.examHour.toString().substring(0, 1) + "H:" + exam.examHour.toString().substring(1, 3)) : null,
                        examDuration: exam.examDuration + "min",
                        module: { designation: exam.module.designation },
                        dsex: session,
                        type: assign ? assign.typeExam : null,
                        currentTechers: currentTechers,
                        class: { classId: item.classId, label: item.label, email: item.email },
                        group: { groupId: group.groupId, label: group.label == "Groupe 1" ? "G1" : "G2" },
                        effective: nbr,
                        teachers: uniqueTeacher,
                        rooms: uniqueRoom
                    })
                }
            }
        }
        return Promise.resolve(result);
    }

    private getDayName(dateStr) {
        const d = dateStr.split('/');
        var date = new Date(d[2], d[1] - 1, d[0]);
        return date.toLocaleDateString("fr-FR", { weekday: 'long' });
    }

    public openModalUpdate(event) {
        var d1 = event.examDate.split("/");
        var examDate = new Date(d1[2], parseInt(d1[1]) - 1, d1[0]);
        this.currentExam = event;
        this.updatePopup = this.initPopUpUpdate(this.content);
        this.department$ = this.refService.getDepartements();
        this.blocs$ = this.refService.getBlocs();
        this.roomService.getBlocByRooms(event.rooms.map(room => room.classRoomId)).subscribe(blocs => {
            this.form.get("bloc").setValue(blocs);
            let examHour = +this.currentExam.examHour.replace("H:", "");
            let arg = { blocs: blocs, effectDate: examDate, hour: examHour };
            this.roomService.getRoomsByBlocs(arg).subscribe(rooms => this.rooms = rooms);
        })
        let data = event.rooms.map(room => room.classRoomId);
        data.examDate = examDate;
        this.teacherService.getUpByTeachers(event.teachers.map(teacher => teacher.teacherId)).subscribe(ups => {
            this.form.get("up").setValue(ups);
            let examHour = +this.currentExam.examHour.replace("H:", "");
            let arg = { ups: ups, effectDate: examDate, hour: examHour };
            this.teacherService.getTeachersByUps(arg).subscribe(teachers => this.teachers = teachers);
        })
        this.nbrTeachers = event.teachers.length;
        this.form.patchValue({
            teachers: event.teachers.map(teacher => teacher.teacherId),
            rooms: event.rooms.map(room => room.classRoomId),
            bloc: null,
            up: null
        });
    }

    public openDeleteModal(event) {
        this.currentExam = event;
        const modal: NgbModalRef = this.modalService.open(RemovePopupComponent,
            {
                size: 'lg',
                ariaLabelledBy: 'modal-basic-title',
                keyboard: false,
                backdrop: 'static',
                centered: true
            });
        modal.componentInstance.config = { title: "Confirmation de suppression", message: "Est-ce que vous confirmez la suppression définitive ?" };
        modal.componentInstance.sendData.subscribe((data: boolean) => this.deleteExam(this.currentExam));
    }

    private deleteExam(event): void {
        /*let data = this.exams.filter(item => item.examId = this.currentExam.examId).map(item => item.teachers);
        let teacherIds: string[] = [];
        if (data && data.length > 0) {
            data.forEach(item => {
                item.forEach(el => {
                    teacherIds.push(el.teacherId);
                })
            })
        }
        let data1 = this.exams.filter(item => item.examId = this.currentExam.examId).map(item => item.rooms);
        let roomIds: string[] = [];
        if (data1 && data1.length > 0) {
            data1.forEach(item => {
                item.forEach(el => {
                    roomIds.push(el.classRoomId);
                })
            })
        }*/
        let arg = {
            classe: event.class.classId,
            examId: event.examId,
            groupId: event.group.groupId,
            roomIds: event.rooms.map(item => item.classRoomId),
            teacherIds: event.teachers.map(item => item.teacherId)
        };
        this.spinner.show();
        concat(
            this.examService.deleteExam(arg).pipe(switchMapTo(EMPTY)),
            timer(1000).pipe(switchMapTo(EMPTY)),
            this.examService.getExams()
        ).subscribe((exams: any[]) => {
            this.spinner.hide();
            if (exams && exams.length > 0) {
                let array = exams.map(exam => exam.classes.map(cl => cl.classId));
                let result: any[] = [];
                array.forEach(item => {
                    item.forEach(element => {
                        result.push(element);
                    });
                })
                this.existClasses = _.uniq(result, true);
                this.initTable(exams).then(data => this.exams = data);
            } else {
                this.exams = [];
            }
            this.modalService.dismissAll();
        }, error => {
            this.spinner.hide();
            this.modalService.dismissAll();
        })
    }

    public sendMail() {
        this.dataSented = [];
        let result = this.exams.map(exam => {
            return {
                classId: exam.class.classId,
                email: exam.class.email,
                groupId: exam.group.label,
                day: exam.day,
                examDate: exam.examDate,
                examHour: exam.examHour,
                examDuration: exam.examDuration,
                module: exam.module.designation,
                dsex: exam.dsex,
                type: exam.type,
                room: exam.rooms[0].classRoomId,
                session: exam.session
            }
        })
        result.forEach(item => {
            if (this.emailClasses.includes(item.classId) && this.sessionMail.includes(item.session)) {
                this.dataSented.push(item);
            }
        })
        if (this.dataSented && this.dataSented.length > 0) {
            this.spinner.show();
            this.examService.sendMail(this.dataSented).subscribe(data => {
                if (data.status === 200) {
                    this.spinner.hide();
                }
            })
        }
    }

    private dateCheck(from1: any, to1: any, check1: any) {
        var d1 = from1.split("/");
        var d2 = to1.split("/");
        var c = check1.split("/");
        var from = new Date(d1[2], parseInt(d1[1]) - 1, d1[0]);
        var to = new Date(d2[2], parseInt(d2[1]) - 1, d2[0]);
        var check = new Date(c[2], parseInt(c[1]) - 1, c[0]);
        if ((check <= to && check >= from)) {
            return true;
        }
        return false;
    }

    public onChangeBoc(event) {
        this.onClearBloc(event);
    }

    public onRemoveBloc(event) {
        this.onClearBloc(event);
    }

    public onClearBloc(event) {
        let examHour = +this.currentExam.examHour.replace("H:", "");
        let data = this.currentExam.examDate.split('/');
        let arg = { blocs: this.form.get("bloc").value, effectDate: this.datePipe.transform(new Date(data[2], data[1] - 1, data[0]), "yyyy-MM-dd"), hour: examHour };
        this.roomService.getRoomsByBlocs(arg).subscribe(rooms => {
            this.rooms = rooms;
            this.form.get("rooms").setValue(null);
        });
    }

    public onChangeDepartmenet(event) {
        this.onClearDepartmenet(event);
    }

    public onRemoveDepartmenet(event) {
        this.onClearDepartmenet(event);
    }

    public onClearDepartmenet(event) {
        let examHour = +this.currentExam.examHour.replace("H:", "");
        let data = this.currentExam.examDate.split('/');
        let arg = { ups: this.form.get("up").value, effectDate: this.datePipe.transform(new Date(data[2], data[1] - 1, data[0]), "yyyy-MM-dd"), hour: examHour };
        this.teacherService.getTeachersByUps(arg).subscribe(teachers => {
            this.teachers = teachers;
            this.form.get("teachers").setValue(null);
        });
    }

    public changedNbrTeacher(event) {
        if (event.target.value) {
            if (this.teachers && this.teachers.length > 0) {
                if (event.target.value <= this.teachers.length) {
                    this.form.get("teachers").setValue(this.teachers.map(item => item.teacherId).slice(0, event.target.value));
                } else {
                    this.form.get("teachers").setValue(this.teachers.map(item => item.teacherId));
                }
            }
        } else {
            this.form.get("teachers").setValue(null);
        }
    }

    public openModal() {
        const modal: NgbModalRef = this.initPopUpCrud(ExamModalComponent);
        modal.componentInstance.triggerSave.subscribe((dataValue: DataValue) => {
            this.spinner.show();
            if (dataValue.action === ActionEnum.CREATE) {
                concat(
                    this.examService.addExam(dataValue.value).pipe(switchMapTo(EMPTY)),
                    timer(1000).pipe(switchMapTo(EMPTY)),
                    this.examService.getExams()
                ).subscribe((exams: any[]) => {
                    if (exams && exams.length > 0) {
                        let array = exams.map(exam => exam.classes.map(cl => cl.classId));
                        let result: any[] = [];
                        array.forEach(item => {
                            item.forEach(element => {
                                result.push(element);
                            });
                        })
                        this.existClasses = _.uniq(result, true);
                        this.initTable(exams).then(data => this.exams = data);
                    }
                    this.spinner.hide();
                    modal.componentInstance.setIsSaved({ isSaved: true });
                }, error => {
                    this.spinner.hide();
                    if (error.error.code) {
                        modal.componentInstance.setIsSaved({ isSaved: false, code: error.error.code, errors: error.error.errors });
                    }
                })
            }
        });
    }

    public updateExam() {
        let args = this.form.value;
        args.examId = this.currentExam.examId;
        args.groupId = this.currentExam.group.groupId;
        concat(
            this.examService.updateExam(args).pipe(switchMapTo(EMPTY)),
            timer(1000).pipe(switchMapTo(EMPTY)),
            this.examService.getExams()
        ).subscribe((exams: any[]) => {
            if (exams && exams.length > 0) {
                let array = exams.map(exam => exam.classes.map(cl => cl.classId));
                let result: any[] = [];
                array.forEach(item => {
                    item.forEach(element => {
                        result.push(element);
                    });
                })
                this.existClasses = _.uniq(result, true);
                this.initTable(exams).then(data => this.exams = data);
            }
            this.spinner.hide();
            this.setIsSaved({ isSaved: true });
        }, error => {
            this.spinner.hide();
            if (error.error.code === 701) {
                this.setIsSaved({ isSaved: false, code: error.error.code });
            }
        })
    }

    public setIsSaved(event) {
        if (event.isSaved === true) {
            this.showLoaderSuccess = true;
            this.saveSuccess = "Le groupe est modifé avec succès";
            timer(1000).subscribe(() => {
                this.form.reset();
                this.updatePopup.close();
                this.showLoaderSuccess = false;
            });
        } else {
            if (event.code === 703) {
                this.showLoaderError = true;
                this.saveError = "Le nombre des salles est insuffisant";
                timer(3000).subscribe(() => this.showLoaderError = false);
            } else if (event.code === 704) {
                this.showLoaderError = true;
                this.saveError = "Le nombre des enseignant est insuffisant";
                timer(3000).subscribe(() => this.showLoaderError = false);
            } else if (event.code === 705 || event.code === 707 || event.code === 708) {
                this.showLoaderError = true;
                this.saveError = event.errors[0];
                console.log(event.errors)
                timer(3000).subscribe(() => this.showLoaderError = false);
            }
        }
    }

    public getArrayForm(event: DataValue) {
        if (event.action === ActionEnum.DELETE) {
            const modal: NgbModalRef = this.initPopUp(RemovePopupComponent);
            modal.componentInstance.config = { title: "Confirmation de suppression", message: "Est-ce que vous confirmez la suppression définitive ?" };
            modal.componentInstance.sendData.subscribe((data: boolean) => data ? this.deleteClass((event.value)) : null);
        } else if (event.action === ActionEnum.UPDATE) {
            const modal: NgbModalRef = this.initPopUpCrud(ExamModalComponent);
            modal.componentInstance.editExam = event.value;
            modal.componentInstance.triggerSave.subscribe((dataValue: DataValue) => {
                console.log(dataValue);

                this.spinner.show();
                if (dataValue.action === ActionEnum.UPDATE) {
                    concat(
                        this.examService.updateExam(dataValue.value).pipe(switchMapTo(EMPTY)),
                        timer(1000).pipe(switchMapTo(EMPTY)),
                        this.examService.getExams()
                    ).subscribe((exams: any[]) => {
                        if (exams && exams.length > 0) {
                            let array = exams.map(exam => exam.classes.map(cl => cl.classId));
                            let result: any[] = [];
                            array.forEach(item => {
                                item.forEach(element => {
                                    result.push(element);
                                });
                            })
                            this.existClasses = _.uniq(result, true);
                            this.initTable(exams).then(data => this.exams = data);
                        }
                        this.spinner.hide();
                        modal.componentInstance.setIsSaved({ isSaved: true });
                    }, error => {
                        this.spinner.hide();
                        if (error.error.code === 701) {
                            modal.componentInstance.setIsSaved({ isSaved: false, code: error.error.code });
                        }
                    })
                }
            });
        } else {
            this.teachers = null;
            this.rooms = null;
        }
    }

    private deleteClass(event): void {
        this.spinner.show();
        concat(
            this.examService.deleteExam({ examId: event.examId, teacherIds: event.teachersId, roomIds: event.roomsNames, classe: event.class }).pipe(switchMapTo(EMPTY)),
            timer(1000).pipe(switchMapTo(EMPTY)),
            this.examService.getExams()
        ).subscribe((exams: any[]) => {
            if (exams && exams.length > 0) {
                let array = exams.map(exam => exam.classes.map(cl => cl.classId));
                let result: any[] = [];
                array.forEach(item => {
                    item.forEach(element => {
                        result.push(element);
                    });
                })
                this.existClasses = _.uniq(result, true);
                this.initTable(exams).then(data => this.exams = data);
            } else {
                this.exams = [];
            }
            this.spinner.hide();
            this.modalService.dismissAll();
        })
    }

    private initPopUp(content: any): NgbModalRef {
        return this.modalService.open(content,
            {
                size: 'xl',
                ariaLabelledBy: 'modal-basic-title',
                keyboard: false,
                backdrop: 'static',
                centered: true
            });
    }

    private initPopUpCrud(content: any): NgbModalRef {
        return this.modalService.open(content,
            {
                size: 'xl',
                ariaLabelledBy: 'modal-basic-title',
                keyboard: false,
                backdrop: 'static',
                centered: true
            });
    }

    private initPopUpUpdate(content: any): NgbModalRef {
        return this.modalService.open(content,
            {
                size: 'xl',
                ariaLabelledBy: 'modal-basic-title',
                keyboard: false,
                backdrop: 'static',
                centered: true
            });
    }

    public export() {
        this.excelExamService.exportAsExcelFile(this.exams);
    }

    public exportPdf() {
        let array = this.selectedColumns.map(el => el.field);
        let result = this.exams.map(item => {
            let element: any = {};
            if (array.includes("day") === true) {
                element.day = item.day;
            }
            if (array.includes("examDate") === true) {
                element.examDate = item.examDate;
            }
            if (array.includes("examHour") === true) {
                element.examHour = item.examHour;
            }
            if (array.includes("examDuration") === true) {
                element.examDuration = item.examDuration;
            }
            if (array.includes("seance") === true) {
                element.seance = item.seance;
            }
            if (array.includes("module") === true) {
                element.module = item.module.designation;
            }
            if (array.includes("dsex") === true) {
                element.dsex = item.dsex;
            }
            if (array.includes("type") === true) {
                element.type = item.type;
            }
            if (array.includes("currentTechers") === true) {
                element.currentTechers = this.displayCurrentTechers(item.currentTechers);
            }
            if (array.includes("class") === true) {
                element.class = item.class.classId;
            }
            if (array.includes("group") === true) {
                element.group = item.group.label;
            }
            if (array.includes("effective") === true) {
                element.effective = item.effective;
            }
            if (array.includes("rooms") === true) {
                element.rooms = this.displayRooms(item.rooms);
            }
            if (array.includes("teachers") === true) {
                element.teachers = this.displayTeachers(item.teachers);
            }
            return element;
        })
        this.exportColumns = this.selectedColumns.map(col => ({
            title: col.header,
            dataKey: col.field
        }));
        const doc = new jsPDF('p', 'pt');
        doc['autoTable'](this.exportColumns, result);
        doc.save("epreuves.pdf");
    }

}
