import { Component } from '@angular/core';
import { ChartDataSets, ChartOptions, ChartType } from 'chart.js';
import { Label } from 'ng2-charts';
import * as _ from 'lodash';
import { TeacherService, DisponibilityService, RoomService, ClassService, ExamService } from 'src/app/core/services';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Component({
    selector: 'statistic',
    templateUrl: './statistic.component.html',
    styleUrls: ['./statistic.component.scss']
})
export class StatisticComponent {

    public barChartOptions: ChartOptions = {
        responsive: true,
        maintainAspectRatio: false,
        // We use these empty structures as placeholders for dynamic theming.
        scales: {
            yAxes: [
                {
                    ticks: {
                        min: 0,
                        max: 100,
                    },
                    display: true
                },
            ]
        },
        plugins: {
            datalabels: {
                anchor: 'end',
                align: 'end',
            }
        }
    };

    public eventsT$: Observable<any>;
    public eventsR$: Observable<any>;
    public eventsC$: Observable<any>;

    public teachers$: Observable<any>;
    public classes$: Observable<any>;
    public rooms$: Observable<any>;

    public barChartLabelsT: Label[];
    public barChartDataT: ChartDataSets[];

    public barChartLabelsR: Label[];
    public barChartDataR: ChartDataSets[];

    public barChartLabelsC: Label[];
    public barChartDataC: ChartDataSets[];

    public barChartLegend = true;
    public barChartType: ChartType = 'bar';

    constructor(private teacherService: TeacherService,
        private roomService: RoomService,
        private classService: ClassService,
        private examService: ExamService,
        private disponibilityService: DisponibilityService) { }

    ngOnInit(): void {
        this.teachers$ = this.teacherService.getTeachers();
        this.rooms$ = this.roomService.getRooms();
        this.classes$ = this.classService.getClasses();
    }

    public onChangeTeacher(event) {
        this.eventsT$ = this.disponibilityService.getDisponibilitiesByTeacher(event.teacherId)
            .pipe(
                map((rs: any) => {
                    let result = rs.filter(item => item.motif == "Surveillance" && item.commentd != "Absent(e)");
                    let teachers = result.map((item: any) => {
                        return { examDuration: item.examDuration, year: item.startDate.split('-')[0] };
                    });
                    const groupByYear = Object.entries(
                        teachers.reduce((acc, el) => {
                            if (!acc[el.year]) {
                                acc[el.year] = [el];
                            } else {
                                acc[el.year].push(el);
                            }
                            return acc;
                        }, {})
                    );
                    const res = groupByYear.map(([year, data]) => {
                        let result = (data as any).map(item => { return item.examDuration });
                        var num = result.reduce((a, b) => a + b, 0);
                        var hours = (num / 60);
                        var rhours = Math.floor(hours);
                        var minutes = (hours - rhours) * 60;
                        var rminutes = Math.round(minutes);
                        return {
                            nbr: (data as any).length,
                            nbrHour: rhours + "." + rminutes,
                            year,
                        }
                    });
                    return res;
                })
            );
        this.eventsT$.subscribe(items => {
            this.barChartLabelsT = items.map(el => el.year);
            let a: any[] = []
            this.barChartLabelsT.forEach(element => a.push(items.find(el => el.year == element).nbr));
            let b: any[] = []
            this.barChartLabelsT.forEach(element => {
                let x = items.find(el => el.year == element).nbrHour;
                let rst;
                if (x.toString().length == 3) {
                    rst = parseFloat(x.toString().substring(0, 1) + "." + x.toString().substring(1, 3))
                } else if (x.toString().length == 4) {
                    rst = parseFloat(x.toString().substring(0, 2) + "." + x.toString().substring(2, 4));
                } else {
                    rst = x;
                }
                b.push(x);
            });
            this.barChartDataT = [
                { data: b, label: "Nombre d'heure surveiller" },
                { data: a, label: 'Nombre des surveillances' }
            ];
        })
    }

    public onChangeRoom(event) {
        this.eventsR$ = this.disponibilityService.getDisponibilitiesByRoom(event.classRoomId)
            .pipe(
                map((rs: any) => {
                    let result = rs.filter(item => item.motif == "DS" || item.motif == "Examen" || item.motif == "Rattrapage");
                    let rooms = result.map((item: any) => {
                        return { examDuration: item.examDuration, year: item.startDate.split('-')[0] };
                    });
                    const groupByYear = Object.entries(
                        rooms.reduce((acc, el) => {
                            if (!acc[el.year]) {
                                acc[el.year] = [el];
                            } else {
                                acc[el.year].push(el);
                            }
                            return acc;
                        }, {})
                    );
                    const res = groupByYear.map(([year, data]) => {
                        let result = (data as any).map(item => { return item.examDuration });
                        var num = result.reduce((a, b) => a + b, 0);
                        var hours = (num / 60);
                        var rhours = Math.floor(hours);
                        var minutes = (hours - rhours) * 60;
                        var rminutes = Math.round(minutes);
                        return {
                            nbr: (data as any).length,
                            nbrHour: rhours + "." + rminutes,
                            year,
                        }
                    });
                    return res;
                })
            );
        this.eventsR$.subscribe(items => {
            this.barChartLabelsR = items.map(el => el.year);
            let a: any[] = []
            this.barChartLabelsR.forEach(element => a.push(items.find(el => el.year == element).nbr));
            let b: any[] = []
            this.barChartLabelsR.forEach(element => {
                let x = items.find(el => el.year == element).nbrHour;
                let rst;
                if (x.toString().length == 3) {
                    rst = parseFloat(x.toString().substring(0, 1) + "." + x.toString().substring(1, 3))
                } else if (x.toString().length == 4) {
                    rst = parseFloat(x.toString().substring(0, 2) + "." + x.toString().substring(2, 4));
                } else {
                    rst = x;
                }
                b.push(x);
            });
            this.barChartDataR = [
                { data: b, label: "Nombre d'heure surveiller" },
                { data: a, label: 'Nombre des épreuves' }
            ];
        })
    }

    private initTable(exams): any[] {
        let result: any[] = [];
        for (let exam of exams) {
            for (let item of exam.classes) {
                let resultGrps = item.groups.filter(group => group.teachers && group.teachers.some(el => el.examIds.includes(exam.examId)) && group.teachers.length > 0 && group.rooms && group.rooms.some(el => el.examIds.includes(exam.examId)) && group.rooms.length > 0);
                for (let group of resultGrps) {
                    result.push({
                        examDate: exam.examDate,
                        examDuration: exam.examDuration,
                        classId: item.classId
                    })
                }
            }
        }
        return result;
    }

  public onChangeClass(event) {
    this.eventsC$ = this.examService.getExamInfoByClass(event.classId)
      .pipe(
        map((result: any) => {
          let teachers = result.map((item: any) => {
            return { examDuration: item.examDuration, year: item.examDate.split('-')[0] };
          });
          const groupByYear = Object.entries(
            teachers.reduce((acc, el) => {
              if (!acc[el.year]) {
                acc[el.year] = [el];
              } else {
                acc[el.year].push(el);
              }
              return acc;
            }, {})
          );
          const res = groupByYear.map(([year, data]) => ({
            nbr: (data as any).length,
            nbrHour: +(Math.floor((data as any).reduce((acc, { examDuration }) => acc + examDuration, 0) / 60) + "" + (data as any).reduce((acc, { examDuration }) => acc + examDuration, 0) % 60),
            year,
          }));
          return res;
        })
      );
    this.eventsC$.subscribe(items => {
      this.barChartLabelsC = items.map(el => el.year);
      let a: any[] = [];
      this.barChartLabelsC.forEach(element => a.push(items.find(el => el.year == element).nbr));
      let b: any[] = [];
      this.barChartLabelsC.forEach(element => {
        let x = items.find(el => el.year == element).nbrHour;
        let rst;
        if (x.toString().length == 3) {
          rst = parseFloat(x.toString().substring(0, 1) + "." + x.toString().substring(1, 3))
        } else if (x.toString().length == 4) {
          rst = parseFloat(x.toString().substring(0, 2) + "." + x.toString().substring(2, 4));
        } else {
          rst = x;
        }
        b.push(rst);
      });
      this.barChartDataC = [
        { data: b, label: "Nombre d'heure des épreuves" },
        { data: a, label: 'Nombre des épreuves' }
      ];
    })
  }

}