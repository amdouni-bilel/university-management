import { Component, ChangeDetectionStrategy, OnInit } from '@angular/core';
import { CalendarEvent, CalendarView } from 'angular-calendar';
import {
    isSameMonth,
    isSameDay,
    startOfDay,
    endOfDay,
} from 'date-fns';
import { Observable } from 'rxjs';
import { RoomService, } from 'src/app/core/services';
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
    templateUrl: "calendar-room.component.html"
})
export class CalendarRoomComponent implements OnInit {

    view: CalendarView = CalendarView.Month;

    CalendarView = CalendarView;

    viewDate: Date = new Date();

    public rooms$: Observable<any>;
    public events: CalendarEvent[];

    activeDayIsOpen: boolean = false;

    constructor(private roomServise: RoomService) { }

    ngOnInit(): void {
        this.rooms$ = this.roomServise.getRooms();
    }

    public onChangeRoom(room) {
        this.events = [];
        if (room) {
            room.disponibilities.forEach(disp => {
                let startDate = disp.startDate.split("/");
                let endDate = disp.endDate.split("/");
                this.events.push({
                    title: room.classRoomId + " - " + disp.motif,
                    start: startOfDay(new Date(startDate)),
                    end: endOfDay(new Date(endDate)),
                    color: colors.yellow,
                    allDay: true,
                    meta: {
                        room,
                    },
                })
            });
        }
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
