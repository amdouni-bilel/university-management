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
import { ExamService, StudentService, TeacherService } from 'src/app/core/services';
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
    templateUrl: "calendar-student.component.html"
})
export class CalendarStudentComponent implements OnInit {

    view: CalendarView = CalendarView.Month;

    CalendarView = CalendarView;

    viewDate: Date = new Date();

    events$: Observable<CalendarEvent<{ teacher: any }>[]>;

    activeDayIsOpen: boolean = false;

    constructor(private examService: ExamService,
        private studentService: StudentService) { }

    ngOnInit(): void {
        this.fetchEvents();
    }

    fetchEvents(): void {
        this.events$ = forkJoin([this.studentService.getCurrentStudent(), this.examService.getExams()])
            .pipe(
                map((result: any) => {
                    let exams = result[1];
                    let el = this.initTable(exams).filter(el => el.class.classId === result[0].classs.classId && el.group.groupId === result[0].group.groupId);
                    let results: any[] = [];
                    el.forEach(x => {
                        let date = x.examDate.split("/");
                        results.push({
                            title: result[0].firstName +" "+ result[0].lastName + " - " + x.dsex + " " + x.type + " " + x.module.designation,
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
                for (let group of item.groups) {
                    result.push({
                        examId: exam.examId,
                        examDate: exam.examDate,
                        examHour: exam.examHour ? exam.examHour.toString().substring(0, 2) + "H:" + exam.examHour.toString().substring(2, 4) : null,
                        examDuration: exam.examDuration + "min",
                        module: { designation: exam.module.designation },
                        dsex: session,
                        type: assign ? assign.typeExam : null,
                        currentTechers: currentTechers,
                        class: { classId: item.classId, label: item.label, email: item.email },
                        group: { groupId: group.groupId, label: group.label },
                        teachers: group.teachers,
                        rooms: group.rooms.map(room => { return { classRoomId: room.classRoomId } })
                    })
                }
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
