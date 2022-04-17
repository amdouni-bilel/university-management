import { DatePipe } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, EventEmitter, OnInit, Output, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { Observable, timer } from 'rxjs';
import * as _ from 'lodash';
import { ClassService, ModuleService, RoomService, RefService, TeacherService } from 'src/app/core/services';
import { ActionEnum } from 'src/app/shared/components/cm-table-container/models/config-column.model';
import { DataValue } from 'src/app/shared/components/cm-table-container/models/data-value.model';

@Component({
    templateUrl: './exam-modal.component.html',
    styleUrls: ['./exam-modal.component.scss'],
    encapsulation: ViewEncapsulation.None,
    changeDetection: ChangeDetectionStrategy.Default,
    providers: [DatePipe]
})
export class ExamModalComponent implements OnInit {

    @Output()
    public triggerSave: EventEmitter<DataValue> = new EventEmitter();

    public form: FormGroup;

    public endHourExam: string;
    public saveError: string;
    public saveSuccess: string;

    public dsex: string[];
    public examTypes: string[];
    public semesters: string[];
    public sessions: any[];

    public levels: any[];

    public minDate = new Date().toISOString();

    public disabled: boolean = true;
    public disabledRoom: boolean = true;
    public disabledTeacher: boolean = true;
    public disabledModule: boolean = true;

    public disabledClass: boolean = true;
    public showLoaderError: boolean = false;
    public showLoaderSuccess: boolean = false;
    public inProgress: boolean = false;
    public invalidDuration: boolean = false;
    public invalidHour: boolean = false;

    public groupValueFn;

    public rooms: any[];
    public teachers: any[];

    public department$: Observable<any[]>;
    public blocs$: Observable<any[]>;

    public classes: any[] = [];
    public classesBuffer: any[] = [];
    public loadingClasses = false;
    public bufferSizeClasses = 10;
    public numberOfItemsFromEndBeforeFetchingMoreClasses = 10;

    public modules: any[] = [];

    public totalItemsModule: number;
    public totalItemsClasse: number;

    public selectedClassesLenght: number;

    constructor(private fb: FormBuilder,
        private activeModal: NgbActiveModal,
        private cdRef: ChangeDetectorRef,
        private classService: ClassService,
        private spinner: NgxSpinnerService,
        private refService: RefService,
        private datePipe: DatePipe,
        private moduleService: ModuleService,
        private roomService: RoomService,
        private teacherService: TeacherService) {
    }

    ngOnInit() {
        this.initForm();
        this.department$ = this.refService.getDepartements();
        this.blocs$ = this.refService.getBlocs();
        this.refService.getLevels().subscribe(levels => {
            this.levels = levels;
            let sessions: any[] = [];
            this.levels.forEach(level => sessions.push(level.session));
            this.sessions = _.uniqBy([...new Set([].concat.apply([], sessions))], 'label');
        })
    }

    public onScrollToEndClasses() {
        this.fetchMoreClasses();
    }

    public onScrollClasses({ end }) {
        if (this.loadingClasses || this.classes.length <= this.classesBuffer.length) {
            return;
        }

        if (end + this.numberOfItemsFromEndBeforeFetchingMoreClasses >= this.classesBuffer.length) {
            this.fetchMoreClasses();
        }
    }

    private fetchMoreClasses() {
        const len = this.classesBuffer.length;
        const more = this.classes.slice(len, this.bufferSizeClasses + len);
        this.loadingClasses = true;
        setTimeout(() => {
            this.loadingClasses = false;
            this.classesBuffer = this.classesBuffer.concat(more);
        }, 200)
    }

    public setIsSaved(event) {
        if (event.isSaved === true) {
            this.showLoaderSuccess = true;
            this.saveSuccess = "L'épreuve est ajouté avec succès";
            timer(1000).subscribe(() => {
                this.reset();
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

    public save() {
        if (this.form.valid) {
            let arg = Object.assign(this.form.value);
            let x = this.endHourExam.toString().replace("H:", "");
            let str;
            if (x.length == 3) {
                if (x.charAt(1) == "0") {
                    str = "0" + x
                } else {
                    str = x + "0"
                }
            } else {
                str = x;
            }
            arg.endHour = +str;
            arg.examHour = +arg.examHour.toString().replace(":", "");
            let dataValue: DataValue = { action: ActionEnum.CREATE, value: arg };
            this.triggerSave.emit(dataValue);
        }
    }

    public onChangeModule(event) {
        /*if (this.form.get("module").value) {
            this.classService.getClassesByModule(this.form.get("module").value).subscribe(items => {
                this.classes = items;
                this.classesBuffer = this.classes.slice(0, this.bufferSizeClasses);
            })
        }*/
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

    public onChangeClass(event) {
        if (event && event.length > 0) {
            this.moduleService.getModulesByLevel(this.form.get("levels").value.map(id => id.charAt(0))).subscribe((data: any[]) => {
                this.disabledModule = false;
                let classes = this.form.get("classes").value;
                let classIds = event.map(el => el.classId);

                if (this.form.get("session").value.label != "SR" && this.form.get("session").value.label != "SP") {
                    let results: any[] = [];
                    console.log(data);

                    let modules = data.filter(module => {
                        const moduleClasses = module.assignClasses.map(assign => assign.classs.classId)
                        return classIds.every(e => moduleClasses.includes(e))
                    })

                    modules.forEach(module => {
                        module.assignClasses.forEach(item => {
                            if (classIds.some(el => el == item.classs.classId) &&
                                item.semester.code == this.form.get("session").value.label.charAt(1) &&
                                (item.periods && item.periods.length > 0 && item.periods.some(x => x.code == this.form.get("session").value.label.charAt(3)))) {
                                results.push(module);
                            }
                        })
                    })

                    this.modules = Array.from(new Set(results.map(a => a.moduleId)))
                        .map(id => {
                            return results.find(a => a.moduleId === id)
                        });
                    this.cdRef.detectChanges();
                } else {
                    this.modules = data.filter(module => module.assignClasses.some(item => classIds.includes(item.classs.classId)));
                    this.cdRef.detectChanges();
                }
                this.cdRef.detectChanges();
                this.totalItemsModule = this.modules.length;
                this.selectedClassesLenght = classes.length;
            })

        } else {
            this.modules = [];
            this.disabledModule = true;
            this.form.get("rooms").setValue(null);
            this.form.get("teachers").setValue(null);
            this.totalItemsModule = this.modules.length;
            this.cdRef.detectChanges();
        }
    }

    public onRemoveClass(event) {
        /* console.log("okk");
         
         this.moduleService.getModulesByLevel(this.form.get("levels").value).subscribe((data: any[]) => {
             this.modules = data;
             this.totalItemsModule = data.length;
         })
         this.disabledModule = true;
         this.form.get("rooms").setValue(null);
         this.form.get("teachers").setValue(null);*/
    }

    public onClearClass(event) {
        this.moduleService.getModulesByLevel(this.form.get("levels").value.map(id => id.charAt(1))).subscribe((data: any[]) => {
            this.modules = data;
            this.totalItemsModule = data.length;
        })
        this.disabledModule = true;
        this.form.get("rooms").setValue(null);
        this.form.get("teachers").setValue(null);
    }

    public onChangeDate(event) {
        this.refService.getLevels().subscribe(levels => {
            let level = levels.find(item => this.form.get("levels").value.map(id => id.charAt(1)).includes(item.id.charAt(1)));
            if (level) {
                let result = level.session.find(item => item.label === this.form.get("session").value.label);
                if (result) {
                    var dateCheck = this.datePipe.transform(event, "dd/MM/yyyy") as any;
                    var startDatecheck = this.datePipe.transform(result.startDate, "dd/MM/yyyy") as any;
                    var endDatecheck = this.datePipe.transform(result.endDate, "dd/MM/yyyy") as any;

                    var d1 = startDatecheck.split("/");
                    var d2 = endDatecheck.split("/");
                    var c = dateCheck.split("/");

                    var from = new Date(d1[2], parseInt(d1[1]) - 1, d1[0]);
                    var to = new Date(d2[2], parseInt(d2[1]) - 1, d2[0]);
                    var check = new Date(c[2], parseInt(c[1]) - 1, c[0]);

                    if (!(check >= from && check <= to)) {
                        this.form.controls['examDate'].setErrors({ 'incorrect': true });
                    }
                }
            }
        })
        if (this.form.get("examDate").value && this.form.get("examHour").value && this.form.get("examDuration").value) {
            this.disabled = false;
        } else {
            this.disabled = true;
        }
    }

    public onChangeHour(event) {
        if (+event.replace(":", "") < 700 || +event.replace(":", "") >= 1800) {
            this.invalidHour = true;
        } else {
            this.invalidHour = false;
            if (this.form.get("examDate").value && this.form.get("examHour").value && this.form.get("examDuration").value) {
                this.disabled = false;
            } else {
                this.disabled = true;
            }
        }
    }

    public onChangeDuration(event) {
        if (event < 30) {
            this.invalidDuration = true;
        } else {
            this.invalidDuration = false;
            if (this.form.get("examDate").value && this.form.get("examHour").value && this.form.get("examDuration").value) {
                let form = Object.assign(this.form.value);
                const examHour = form.examHour.split(':');
                var a = examHour.length == 3 ? +("0" + examHour) : examHour;
                var minutes = (+a[0]) * 60 + (+a[1]);
                let num = minutes + form.examDuration;
                var hours = Math.floor(num / 60);
                var minutes = num % 60;
                this.endHourExam = hours + "H:" + (minutes.toString().length == 1 ? "0" + minutes : minutes);
                this.disabled = false;
            } else {
                this.disabled = true;
                this.endHourExam = null;
            }
        }
    }

    public onChangeBoc(event) {
        this.onClearBloc(event);
    }

    public onRemoveBloc(event) {
        this.onClearBloc(event);
    }

    public onClearBloc(event) {
        let examHour = +this.form.get("examHour").value.replace(":", "");
        let arg = { blocs: this.form.get("bloc").value, effectDate: this.form.get("examDate").value, hour: examHour };
        this.roomService.getRoomsByBlocs(arg).subscribe(rooms => {
            this.rooms = rooms;
            this.form.get("rooms").setValue(null);
            if (this.rooms.length > 0) {
                this.disabledRoom = false;
            } else {
                this.disabledRoom = true;
            }
        });
    }

    public onChangeDepartmenet(event) {
        this.onClearDepartmenet(event);
    }

    public onRemoveDepartmenet(event) {
        this.onClearDepartmenet(event);
    }

    public onClearDepartmenet(event) {
        let examHour = +this.form.get("examHour").value.replace(":", "");
        let arg = { ups: this.form.get("up").value, effectDate: this.form.get("examDate").value, hour: examHour };
        this.teacherService.getTeachersByUps(arg).subscribe(teachers => {
            this.teachers = teachers;
            this.form.get("teachers").setValue(null);
            if (this.teachers && this.teachers.length > 0) {
                this.disabledTeacher = false;
            } else {
                this.disabledTeacher = true;
            }
        });
    }

    public onChangeLavel(event) {
        if (event && event.length > 0) {
            this.spinner.show();
            this.classService.getClasses().subscribe(classes => {
                let indexLevel = event.map(item => item.label.charAt(0));
                this.classes = classes.filter(el => indexLevel.some(item => item === el.classId.charAt(0)));
                this.classesBuffer = this.classes.slice(0, this.bufferSizeClasses);
                this.spinner.hide();
                this.disabledClass = false;
            })
            this.moduleService.getModulesByLevel(event.map(item => item.label.charAt(0))).subscribe((data: any[]) => {
                this.modules = data;
                this.totalItemsModule = data.length;
            })
            if (event.every(item => item.id < 5)) {
                let level = this.levels.find(level => level.id === 1);
                if (level) {
                    this.sessions = level.session;
                    let disabledLevel = this.levels.find(level => level.id === 5);
                    if (disabledLevel && disabledLevel.disabled === false) {
                        disabledLevel.disabled = true;
                        this.levels = [...this.levels];
                    }
                }
            } else {
                let level = this.levels.find(level => level.id === 5);
                if (level) {
                    this.sessions = level.session;
                    let disabledLevels = this.levels.filter(level => level.id < 5);
                    if (disabledLevels.length > 0) {
                        disabledLevels.forEach(item => item.disabled = true);
                        this.levels = [...this.levels];
                    }
                }
            }
        } else {
            this.onClearLavel();
        }
    }

    public onRemoveLavel() {
        this.disabledClass = true;
        if (this.form.get("levels").value && this.form.get("levels").value.length > 0) {
            if (this.form.get("levels").value.map(id => id.charAt(1)).every(item => item.id < 5)) {
                let level = this.levels.find(level => level.id === 1);
                this.sessions = level.session;
                let disabledLevel = this.levels.find(level => level.id === 5);
                if (disabledLevel.disabled === false) {
                    disabledLevel.disabled = true;
                    this.levels = [...this.levels];
                }
            }
        } else {
            this.onClearLavel();
        }
    }

    public onClearLavel() {
        this.refService.getLevels().subscribe(levels => {
            this.form.get("session").setValue(null);
            this.levels = levels;
            let sessions: any[] = [];
            this.levels.forEach(level => sessions.push(level.session));
            this.sessions = [...new Set([].concat.apply([], sessions))];
        });
    }

    public onChangeSession(event) {
        this.disabledModule = true;
        this.disabledRoom = true;
        this.disabledTeacher = true;
        this.endHourExam = null;
        this.disabled = true;
        this.form.get("module").setValue(null);
        this.form.get("classes").setValue(null);
        this.form.get("teachers").setValue(null);
        this.form.get("examDate").setValue(null);
        this.form.get("examHour").setValue(null);
        this.form.get("examDuration").setValue(null);
        this.form.get("rooms").setValue(null);
        this.form.get("bloc").setValue(null);
        this.form.get("up").setValue(null);
        if (event) {
            this.refService.getLevels().subscribe(levels => {
                let result = levels.filter(level => level.session.some(item => item.label === event.label));
                this.levels = result;
            })
        } else {
            this.refService.getLevels().subscribe(levels => {
                this.form.get("levels").setValue(null);
                this.levels = levels;
                let sessions: any[] = [];
                this.levels.forEach(level => sessions.push(level.session));
                this.sessions = _.uniqBy([...new Set([].concat.apply([], sessions))], 'label');
            })
        }
    }

    public onRemoveSession() {
        this.disabledClass = true;
        this.disabledModule = true;
        this.disabledRoom = true;
        this.disabledTeacher = true;
        this.endHourExam = null;
        this.disabled = true;
        this.form.get("module").setValue(null);
        this.form.get("classes").setValue(null);
        this.form.get("teachers").setValue(null);
        this.form.get("examDate").setValue(null);
        this.form.get("examHour").setValue(null);
        this.form.get("examDuration").setValue(null);
        this.form.get("rooms").setValue(null);
        this.form.get("bloc").setValue(null);
        this.form.get("up").setValue(null);
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
            levels: [null, Validators.required],
            session: [null, Validators.required],
            module: [null, Validators.required],
            classes: [null, Validators.required],
            teachers: [null],
            examDate: [null, Validators.required],
            examHour: [null, Validators.required],
            examDuration: [null, Validators.required],
            rooms: [null],
            bloc: [null, Validators.required],
            up: [null, Validators.required]
        })
    }

}
