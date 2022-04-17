import { Component, ChangeDetectionStrategy, OnInit } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { CalendarEvent, CalendarView } from 'angular-calendar';
import {
    isSameMonth,
    isSameDay,
    startOfMonth,
    endOfMonth,
    startOfWeek,
    endOfWeek,
    startOfDay,
    endOfDay,
    format,
} from 'date-fns';
import { forkJoin, Observable } from 'rxjs';
import { ClassService, ExamService, TeacherService } from 'src/app/core/services';
const colors: any = {
    red: {
        primary: "#ad2121",
        secondary: "#FAE3E3"
    },
    blue: {
        primary: "#1e90ff",
        secondary: "#D1E8FF"
    },
    yellow: {
        primary: "#e3bc08",
        secondary: "#FDF1BA"
    }
};


@Component({
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: "calendar.class.component.html"
})
export class CalendarClassComponent implements OnInit {

    view: CalendarView = CalendarView.Month;

    CalendarView = CalendarView;

    viewDate: Date = new Date();

    public classes$: Observable<any>;

    events$: Observable<CalendarEvent<{ teacher: any }>[]>;

    activeDayIsOpen: boolean = false;

    constructor(private examService: ExamService,
        private classService: ClassService) { }

    ngOnInit(): void {
        this.classes$ = this.classService.getClasses();
    }

    public onChangeClass(classe): void {
        this.events$ = this.examService.getExams()
            .pipe(
                map((result: any) => {
                    let el = this.initTable(result).filter(el => el.class.classId === classe.classId);
                    let results: any[] = [];
                    el.forEach(x => {
                        console.log(x);

                        let date = x.examDate.split("/");
                        results.push({
                            title: x.class.classId + " - " + x.dsex + " " + x.type + " " + x.module.designation,
                            start: startOfDay(new Date(date[2], date[1] - 1, date[0])),
                            end: endOfDay(new Date(date[2], date[1] - 1, date[0])),
                            color: colors.yellow,
                            allDay: true,
                            meta: {
                                x,
                            }
                        });
                    })
                    return results;
                })
            );
    }

    private initTable(exams): any[] {
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
                result.push({
                    examId: exam.examId,
                    examDate: exam.examDate,
                    examHour: exam.examHour ? exam.examHour.toString().substring(0, 2) + "H:" + exam.examHour.toString().substring(2, 4) : null,
                    examDuration: exam.examDuration + "min",
                    module: { designation: exam.module.designation },
                    dsex: session,
                    type: assign ? assign.typeExam : null,
                    currentTechers: currentTechers,
                    class: { classId: item.classId, label: item.label, email: item.email }
                })
            }
        }
        return result;
    }


    setView(view: CalendarView) {
        this.view = view;
    }

    dayClicked({
        date,
        events,
    }: {
        date: Date;
        events: CalendarEvent<{ teacher: any }>[];
    }): void {
        if (isSameMonth(date, this.viewDate)) {
            if (
                (isSameDay(this.viewDate, date) && this.activeDayIsOpen === true) ||
                events.length === 0
            ) {
                this.activeDayIsOpen = false;
            } else {
                this.activeDayIsOpen = true;
                this.viewDate = date;
            }
        }
    }

    closeOpenMonthViewDay() {
        this.activeDayIsOpen = false;
    }

}
