import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ConfigService } from '@ngx-config/core';
import { CreateModuleRequest } from '../models/request/create-module-request.model';

@Injectable()
export class ModuleService {

    readonly domain: string;

    constructor(private config: ConfigService,
        private httpClient: HttpClient) {
        this.domain = this.config.getSettings('environment.serviceBaseUrl') + "module";
    }

    addModule(data: CreateModuleRequest): Observable<any> {
        return this.httpClient.post<any>(this.domain, data, { observe: 'response' });
    }

    addAssignClass(data: any): Observable<any> {
        return this.httpClient.post<any>(this.domain + "/assign", data, { observe: 'response' });
    }

    updateModule(data): Observable<any> {
        return this.httpClient.put<any>(this.domain, data, { observe: 'response' });
    }

    updateAssignClass(data: any): Observable<any> {
        return this.httpClient.put<any>(this.domain + "/assign", data, { observe: 'response' });
    }

    deleteModule(id: string) {
        return this.httpClient.delete<void>(this.domain + "/" + id);
    }

    deleteAssign(id: string) {
        return this.httpClient.delete<void>(this.domain + "/assign/" + id);
    }

    getModulesWithouClasses(): Observable<any[]> {
        return this.httpClient.get<any[]>(this.domain + "/without-classes");
    }

    getModulesByLevel(levels): Observable<any[]> {
        let url: string;
        return this.httpClient.post<any[]>(this.domain + "/with-classes", levels);
    }

    getModules(page, sortField?: any, sortType?: any, filter?: any): Observable<any> {
        let url: string;
        if (sortField && sortType) {
            url = this.domain + "?page=" + page + "&size=15&sortField=" + sortField + "&sortType=" + sortType;
        } else {
            url = this.domain + "?page=" + page + "&size=15";
        }
        if (filter) {
            url = url + "&filter=" + filter;
        }
        return this.httpClient.get(url);
    }

    getModulesByClass(classId: string): Observable<any[]> {
        return this.httpClient.get<any[]>(this.domain + "/classId/" + classId);
    }

    getSingleModule(id: string): Observable<any> {
        return this.httpClient.get<any>(this.domain + "/" + id);
    }

}