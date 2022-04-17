import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ConfigService } from '@ngx-config/core';
import { CreateExamRequest, Exam, UpdateExamRequest } from '../models';

@Injectable()
export class ExamService {

    readonly domain: string;

    constructor(private config: ConfigService,
        private httpClient: HttpClient) {
        this.domain = this.config.getSettings('environment.serviceBaseUrl') + "exam/";
    }

    addExam(data: CreateExamRequest): Observable<HttpResponse<Exam>> {
        return this.httpClient.post<Exam>(this.domain, data, { observe: 'response' });
    }

    sendMail(data): Observable<any> {
        return this.httpClient.post<any>(this.domain + "mail", data, { observe: 'response' });
    }

    updateExam(data: UpdateExamRequest): Observable<HttpResponse<Exam>> {
        return this.httpClient.put<Exam>(this.domain, data, { observe: 'response' });
    }

    deleteExam(data) {
        return this.httpClient.patch<void>(this.domain, data);
    }

    getExams(): Observable<any[]> {
        return this.httpClient.get<any[]>(this.domain);
    }

    getExamInfoByClass(classId): Observable<any[]> {
        return this.httpClient.get<any[]>(this.domain + "class/" + classId);
    }

    getExamsByStudent(studentId): Observable<any[]> {
        return this.httpClient.get<any[]>(this.domain + studentId);
    }

    getSingleExam(id: string): Observable<Exam> {
        return this.httpClient.get<Exam>(this.domain + id);
    }

}