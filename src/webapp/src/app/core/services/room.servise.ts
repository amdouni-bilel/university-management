import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ConfigService } from '@ngx-config/core';
import { Room } from '../models';

@Injectable()
export class RoomService {

    readonly domain: string;

    constructor(private config: ConfigService,
        private httpClient: HttpClient) {
        this.domain = this.config.getSettings('environment.serviceBaseUrl') + "room/";
    }

    addRoom(data): Observable<HttpResponse<HttpResponse<Room>>> {
        return this.httpClient.post<HttpResponse<Room>>(this.domain, data, { observe: 'response' });
    }

    updateRoom(data): Observable<any> {
        return this.httpClient.put<any>(this.domain, data, { observe: 'response' });
    }

    deleteRoom(id) {
        return this.httpClient.delete<void>(this.domain + id);
    }

    getRooms(): Observable<Room[]> {
        return this.httpClient.get<Room[]>(this.domain );
    }

    getRoomsWithoutDisponibilities(): any {
        return this.httpClient.get(this.domain + "without-disponibilities");
    }

    getDisponibilitiesRooms(page, sortField?: any, sortType?: any, filter?: any): any {
        let url = this.domain + "disponibilities?page=" + page + "&size=15";
        if (sortField && sortType) {
            url = url + "&sortField=" + sortField + "&sortType=" + sortType;
        }
        if (filter) {
            url = url + "&filter=" + filter;
        }
        return this.httpClient.get(url);
    }

    getBlocByRooms(data): any {
        return this.httpClient.post(this.domain + "bloc", data);
    }

    getRoomsByBlocs(data): Observable<Room[]> {
        return this.httpClient.post<Room[]>(this.domain + "blocs", data);
    }

    getSingleRoom(username: string): Observable<Room> {
        return this.httpClient.get<Room>(this.domain + '/' + username);
    }

}