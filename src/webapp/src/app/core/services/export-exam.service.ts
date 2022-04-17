import { Injectable } from '@angular/core';
import * as FileSaver from 'file-saver';
import * as XLSX from 'xlsx';
import { DatePipe } from '@angular/common';
import * as _ from 'lodash';
const CSV_EXTENSION = '.csv';
const CSV_TYPE = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8';

@Injectable()
export class ExcelExamService {

    public exportAsExcelFile(json: any): void {
        const headerFiche: XLSX.WorkSheet = XLSX.utils.json_to_sheet([
            {
                A: "Jour", B: "Date", C: "Heure début",
                D: "Module", E: "DSEX", F: "Type",
                G: "Enseigant", H: "Classe", I: "Groupe",
                J: "Salle", K: "Surveillant" , L:"Durée" , M:"Effectif"
            }
        ], { header: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" , "L" ,"M"], skipHeader: true });

        const worksheetFiche: XLSX.WorkSheet = XLSX.utils.sheet_add_json(headerFiche, this.getExam(json),
            { header: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" , "L" ,"M"], skipHeader: true, origin: -1 });

        const csv = XLSX.utils.sheet_to_csv(worksheetFiche, { FS: ';' });
        const new_csv = '\uFEFF' + csv;
        const data: Blob = new Blob([new_csv], { type: CSV_TYPE });
        var datePipe = new DatePipe("en_US");
        let formattedDt = datePipe.transform(new Date(), 'dd/MM/yyyy');

        FileSaver.saveAs(data, "épreuves" + formattedDt + CSV_EXTENSION);
    }

    private getExam(data): any {
        let result: any[] = [];
        var datePipe = new DatePipe("en_US");
        data.forEach(item => {
            result.push({
                A: item.day,
                B: item.examDate,
                C: item.examHour,
                D: item.module.designation,
                E: item.dsex,
                F: item.type,
                G: item.currentTechers.map(item => item.fullName).toString(),
                H: item.class.classId,
                I: item.group.label,
                J: item.rooms.map(item => item.classRoomId).toString(),
                K: item.teachers.map(item => item.fullName).toString() ,
                L: item.examDuration,
                M: item.effective
            });
        })
        return result;
    }

}
