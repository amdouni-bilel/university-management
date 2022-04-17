import { Component, ElementRef, ViewChild } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { concat, EMPTY, timer } from "rxjs";
import * as XLSX from 'xlsx';
import { switchMapTo } from "rxjs/operators";
import { StudentService } from 'src/app/core/services';
import { ActionEnum } from 'src/app/shared/components/cm-table-container/models/config-column.model';
import { DataValue } from 'src/app/shared/components/cm-table-container/models/data-value.model';
import { RemovePopupComponent } from 'src/app/shared/components/comfirmation-popup/remove/remove-popup.component';
import { StudentModalComponent } from './student-modal/student-modal.component';
import { CreateStudentRequest } from 'src/app/core/models';

@Component({
    selector: 'students',
    templateUrl: './students.component.html',
    styleUrls: ['./students.component.scss']
})
export class StudentsComponent {

    @ViewChild('fileInput') fileInput;

    @ViewChild('content')
    private content: ElementRef;

    public students: any[] = [];
    public cols: any[];

    public willDownload = false;
    public uploaded: boolean = false;
    public pdfName: string;
    public data: any;

    constructor(private modalService: NgbModal,
        private spinner: NgxSpinnerService,
        private studentService: StudentService) {
    }

    ngOnInit() {
        this.cols = [
            {
                header: "Nom",
                field: "firstName"
            },
            {
                header: "Prénom",
                field: "lastName"
            },
            {
                header: "Email",
                field: "email"
            },
            {
                header: "CIN",
                field: "cin"
            },
            {
                header: "Classe",
                field: "classs",
                subfield: 'classId'
            },
            {
                header: "Groupe",
                field: "group",
                subfield: 'label'
            }
        ];
        this.studentService.getStudents().subscribe(result => {
            this.students = this.mapStudents(result).sort((a, b) => a.classs.classId.localeCompare(b.classs.classId));
        })
    }

    private mapStudents(result): any {
        let array: any[] = [];
        result.forEach(item => {
            array.push({
                studentId: item.studentId,
                cin: item.cin,
                email: item.email,
                firstName: item.firstName,
                lastName: item.lastName,
                classs: item.classs,
                group: item.group
            });
        })
        return array;
    }

    public openModal(event?: any) {
        const modal: NgbModalRef = this.modalService.open(StudentModalComponent,
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
            if (dataValue.action === ActionEnum.CREATE) {
                concat(
                    this.studentService.addStudent(dataValue.value).pipe(switchMapTo(EMPTY)),
                    timer(1000).pipe(switchMapTo(EMPTY)),
                    this.studentService.getStudents()
                ).subscribe((students: any[]) => {
                    this.spinner.hide();
                    this.students = this.mapStudents(students).sort((a, b) => a.classs.classId.localeCompare(b.classs.classId));
                    modal.componentInstance.setIsSaved({ isSaved: true });
                }, error => {
                    this.spinner.hide();
                    if (error.error.code === 701) {
                        modal.componentInstance.setIsSaved({ isSaved: false, code: error.error.code });
                    }
                })
            } else if (dataValue.action === ActionEnum.UPDATE) {
                //let arg = new CreateStudentRequest(event.value.studentId, event.value.firstName, event.value.lastName, event.value.cin, event.value.classs.classId, event.value.group.label, event.value.email);
                concat(
                    this.studentService.updateStudent(dataValue.value).pipe(switchMapTo(EMPTY)),
                    timer(1000).pipe(switchMapTo(EMPTY)),
                    this.studentService.getStudents()
                ).subscribe((students: any[]) => {
                    this.students = this.mapStudents(students).sort((a, b) => a.classs.classId.localeCompare(b.classs.classId));
                    this.spinner.hide();
                    modal.componentInstance.setIsSaved({ isUpdated: true });
                }, error => {
                    this.spinner.hide();
                    if (error.error.code === 701) {
                        modal.componentInstance.setIsSaved({ isSaved: false, code: error.error.code });
                    }
                })
            }
        });
    }

    public deleteStudent(event): void {
        const modal: NgbModalRef = this.initPopUp(RemovePopupComponent);
        modal.componentInstance.config = { title: "Confirmation de suppression", message: "Est-ce que vous confirmez la suppression définitive ?" };
        modal.componentInstance.sendData.subscribe((data: boolean) => {
            if (data) {
                this.spinner.show();
                concat(
                    this.studentService.deleteStudent(event.studentId).pipe(switchMapTo(EMPTY)),
                    timer(1000).pipe(switchMapTo(EMPTY)),
                    this.studentService.getStudents()
                ).subscribe((students: any[]) => {
                    this.spinner.hide();
                    this.students = this.mapStudents(students).sort((a, b) => a.classs.classId.localeCompare(b.classs.classId));
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

    public onInvalidFiles(fileList: Array<File>) {
        if (fileList.length > 0) {
            this.dismissDocument();
        }
    }

    public dismissDocument() {
        this.uploaded = false;
        if (this.fileInput.nativeElement.files.length > 0) {
            this.fileInput.nativeElement.value = '';
        }
    }

    uploadListener(ev: any): void {
        let workBook = null;
        let jsonData = null;
        const reader = new FileReader();
        const file = ev.target.files[0];
        this.pdfName = file.name;
        this.uploaded = true;
        reader.onload = (event) => {
            const data = reader.result;
            workBook = XLSX.read(data, { type: 'binary' });
            jsonData = workBook.SheetNames.reduce((initial, name) => {
                const sheet = workBook.Sheets[name];
                initial[name] = XLSX.utils.sheet_to_json(sheet);
                return initial;
            }, {});
            this.data = JSON.stringify(jsonData);
        }
        reader.readAsBinaryString(file);
    }

    public importDocument() {
        let students = JSON.parse(this.data)["Liste des etudiants"];
        this.spinner.show();
        let studentsR: CreateStudentRequest[] = [];
        students.forEach((student, index) => studentsR.push(new CreateStudentRequest(student.id, student.prenom, student.nom, student.cin, student.classe, student.email)));
        concat(
            this.studentService.addAllStudent(studentsR).pipe(switchMapTo(EMPTY)),
            timer(1000).pipe(switchMapTo(EMPTY)),
            this.studentService.getStudents()
        ).subscribe((students: any[]) => {
            this.spinner.hide();
            this.students = this.mapStudents(students).sort((a, b) => a.classs.classId.localeCompare(b.classs.classId));
            this.modalService.dismissAll();
        })
    }

    public import() {
        this.initPopUp(this.content);
    }

}