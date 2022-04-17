import { Component, ChangeDetectionStrategy, OnInit } from '@angular/core';
import { CalendarEvent, CalendarView } from 'angular-calendar';
import {
    isSameMonth,
    isSameDay,
    startOfDay,
    endOfDay,
} from 'date-fns';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { TeacherService, TokenService } from 'src/app/core/services';
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
    templateUrl: "calendar-teacher.component.html"
})
export class CalendarTeacherComponent implements OnInit {

    view: CalendarView = CalendarView.Month;

    CalendarView = CalendarView;

    viewDate: Date = new Date();

    public isTeacher: boolean;

    public teachers$: Observable<any>;
    public events: CalendarEvent[];

    public events$: Observable<CalendarEvent<{ teacher: any }>[]>;

    activeDayIsOpen: boolean = false;

    constructor(private tokenService: TokenService,
        private teacherService: TeacherService) { }

    ngOnInit(): void {
        this.tokenService.getRoles().then((data: any[]) => {
            if (data.includes('TEACHER')) {
                this.isTeacher = true;
                this.events$ = this.teacherService.getCurrentTeacher().pipe(
                    map((teacher: any) => {
                        let results: any[] = [];
                        teacher.disponibilities.forEach(disp => {
                            let startDate = disp.startDate.split("/");
                            let endDate = disp.endDate.split("/");
                            console.log(new Date(startDate[2], startDate[1] - 1, startDate[0]));
                            results.push({
                                title: teacher.fullName + " - " + disp.motif,
                                start: startOfDay(new Date(startDate)),
                                end: endOfDay(new Date(endDate)),
                                color: colors.yellow,
                                allDay: true,
                                meta: {
                                    teacher,
                                },
                            })
                        });
                        console.log(results)
                        return results;
                    })
                );
            } else {
                this.isTeacher = false;
                this.teachers$ = this.teacherService.getTeachers();
            }
        })
    }

    public onChangeTeacher(teacher) {
        this.events = [];
        if (teacher) {
            teacher.disponibilities.forEach(disp => {
                this.events.push({
                    title: teacher.fullName + " - " + disp.motif,
                    start: startOfDay(new Date(disp.startDate)),
                    end: endOfDay(new Date(disp.endDate)),
                    color: colors.yellow,
                    allDay: true,
                    meta: {
                        teacher,
                    },
                })
            });
        }
    }

    setView(view: CalendarView) {
        this.view = view;
    }

    dayClicked({ date, events }: { date: Date; events: CalendarEvent<{ teacher: any }>[]; }): void {
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
