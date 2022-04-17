import { Component, ViewEncapsulation } from '@angular/core';
import { ExamService } from 'src/app/core/services';
import { DatePipe } from '@angular/common';

@Component({
    selector: 'home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss'],
    encapsulation: ViewEncapsulation.None,
    providers: [DatePipe]
})
export class HomeComponent {

    public cols: any[] = [];

    public data: any[];

    public result: any[] = [];

    constructor(private examService: ExamService,
        private datePipe: DatePipe) { }

    ngOnInit(): void {
        this.examService.getExams().subscribe(exams => {
            if (exams && exams.length > 0) {
                let array = exams.map(exam => exam.classes.map(cl => cl.classId));
                let result: any[] = [];
                array.forEach(item => {
                    item.forEach(element => {
                        result.push(element);
                    });
                })
                this.initTable(exams).then(data => {
                    Array.from(data[0].keys()).forEach(item => {
                        this.cols.push({
                            header: item,
                            field: item,
                            width: this.width(item)
                        })
                    })
                    let item: any = {};
                    data[0].forEach((value: string, key: string) => item[key] = value);
                    this.result.push(item);
                    console.log(this.result);
                });
            }
        })
    }

    private width(item): string {
        let result;
        if (item === "Jour") {
            result = "5%"
        } else if (item === "Date") {
            result = "7%"
        } else if (item === "Heure Début") {
            result = "9%"
        }
        return result;
    }

    private async initTable(exams): Promise<any[]> {
        let result: any[] = [];
        let columns1: Map<string, string> = new Map<string, string>();

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
                    assign.teachers.forEach(te => currentTechers.push(te.fullName));
                }
                let resultGrps = item.groups.filter(group => group.teachers && group.teachers.some(el => el.examIds.includes(exam.examId)) && group.teachers.length > 0 && group.rooms && group.rooms.some(el => el.examIds.includes(exam.examId)) && group.rooms.length > 0);
                let uniqueRoom;
                let uniqueTeacher;
                for (let group of resultGrps) {
                    let gt = group.teachers.filter(el => el.examIds.includes(exam.examId)).map(te => te.fullName);
                    let gr = group.rooms.filter(el => el.examIds.includes(exam.examId)).map(te => te.classRoomId);
                    if (group.label == "Groupe 1") {
                        let uniqueRoomG1 = Array.from(new Set(gr.map(a => a.classRoomId))).map(id => { return gr.find(a => a.classRoomId === id) })
                        let uniqueTeacherG1 = Array.from(new Set(gt.map(a => a.teacherId))).map(id => { return gt.find(a => a.teacherId === id) })
                        uniqueTeacher = "G1: " + uniqueTeacherG1.join();
                        uniqueRoom = "G1: " + uniqueRoomG1.join();
                    } else {
                        let uniqueRoomG2 = Array.from(new Set(gr.map(a => a.classRoomId))).map(id => { return gr.find(a => a.classRoomId === id) })
                        let uniqueTeacherG2 = Array.from(new Set(gt.map(a => a.teacherId))).map(id => { return gt.find(a => a.teacherId === id) })
                        if (uniqueTeacherG2) {
                            uniqueTeacher = uniqueTeacher + "/" + "G2: " + uniqueTeacherG2.join();
                        }
                        if (uniqueRoomG2) {
                            uniqueRoom = uniqueRoom + "/" + "G2: " + uniqueRoomG2.join();
                        }
                    }

                }
                var c = exam.examDate.split("/");
                var check = new Date(c[2], parseInt(c[1]) - 1, c[0]);


                let m = exam.module.designation + " (EX: " + (assign ? assign.typeExam : null) + ")";
                let s = "Salle: " + uniqueRoom;
                let e = "Enseigant: " + currentTechers;
                let sr = "Surveillant: " + uniqueTeacher;

                columns1.set('Jour', this.getDayName(exam.examDate));
                columns1.set('Date', this.datePipe.transform(check, " dd/MM/yyyy"));
                columns1.set('Heure Début', exam.examHour ? (exam.examHour.toString().length === 4 ? exam.examHour.toString().substring(0, 2) + "H:" + exam.examHour.toString().substring(2, 4) : exam.examHour.toString().substring(0, 1) + "H:" + exam.examHour.toString().substring(1, 3)) : null);
                columns1.set(item.classId, m + "<br\>" + s + "<br\>" + e + "<br\>" + sr);
            }
        }
        if (columns1) {
            result.push(columns1);
        }
        return result;
    }

    private groupBy(xs, key) {
        let xxx = xs.reduce(function (rv, x) {
            (rv[x[key]] = rv[x[key]] || []).push(x);
            return rv;
        }, {});

        let arr = [];
        Object.keys(xxx).map(function (key) {
            arr.push({ [key]: xxx[key] })
            return arr;
        });
        return arr;
    };

    private getDayName(dateStr) {
        const d = dateStr.split('/');
        var date = new Date(d[2], d[1] - 1, d[0]);
        return date.toLocaleDateString("fr-FR", { weekday: 'long' });
    }

}