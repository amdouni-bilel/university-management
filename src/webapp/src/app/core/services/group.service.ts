import { HttpClient} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';;
import { ConfigService } from '@ngx-config/core';

@Injectable()
export class GroupService {

    readonly domain: string;

    constructor(private config: ConfigService,
        private httpClient: HttpClient) {
        this.domain = this.config.getSettings('environment.serviceBaseUrl') + "group/";
    }

    getGroupes(): Observable<any[]> {
        return this.httpClient.get<any[]>(this.domain);
    }

}