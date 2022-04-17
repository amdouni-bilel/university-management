import { Injectable } from '@angular/core';
import * as FileSaver from 'file-saver';
import * as XLSX from 'xlsx';
import { DatePipe } from '@angular/common';
import * as _ from 'lodash';
const CSV_EXTENSION = '.csv';
const CSV_TYPE = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8';

@Injectable()
export class ExcelDispTeacherService {

    public exportAsExcelFile(json: any): void {
        var datePipe = new DatePipe("en_US");
        let result: any[] = [];
        json.forEach(element => {
            element.disponibilities.forEach(item => {
                result.push({
                    A: element.teacherId,
                    B: element.fullName,
                    C: item.startDate ? datePipe.transform(item.startDate, 'dd/MM/yyyy') : null,
                    D: item.endDate ? datePipe.transform(item.endDate, 'dd/MM/yyyy') : null,
                    E: item.startHour ? this.hourFormat(item.startHour) : null,
                    F: item.endHour ? this.hourFormat(item.endHour) : null,
                    G: item.motif,
                    H: item.commentd
                });
            });
        });
        const headerFiche: XLSX.WorkSheet = XLSX.utils.json_to_sheet([
            {
                A: "Identidient", B: "Enseignant", C: "Date début", D: "Date fin",
                E: "Heure début", F: "Heure fin", G: "Motif", H: "Commentaire"
            }
        ], { header: ["A", "B", "C", "D", "E", "F", "G", "H"], skipHeader: true });

        const worksheetFiche: XLSX.WorkSheet = XLSX.utils.sheet_add_json(headerFiche, result,
            { header: ["A", "B", "C", "D", "E", "F", "G", "H"], skipHeader: true, origin: -1 });

        const csv = XLSX.utils.sheet_to_csv(worksheetFiche, { FS: ';' });
        const new_csv = '\uFEFF' + csv;
        const data: Blob = new Blob([new_csv], { type: CSV_TYPE });
        var datePipe = new DatePipe("en_US");
        let formattedDt = datePipe.transform(new Date(), 'dd/MM/yyyy');

        FileSaver.saveAs(data, "unavailability_teachers_" + formattedDt + CSV_EXTENSION);
    }

    public hourFormat(hour): any {
        let result;
        if (hour.toString().length === 3) {
            result = "0" + hour.toString();
        } else if (hour.toString().length === 1) {
            result = "000" + hour.toString();
        } else if (hour.toString().length === 2) {
            result = "00" + hour.toString();
        } else {
            result = hour.toString();
        }
        return result.substring(0, 2) + "H:" + result.substring(2, 4);
    }
}