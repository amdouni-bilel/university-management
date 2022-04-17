import { KeycloakService } from 'keycloak-angular';
import { HttpClient } from '@angular/common/http';
import * as moment from 'moment';

function getPageInfo(http: HttpClient): Promise<any> {
  return new Promise(async (resolve, reject) => {
    let infoUrl = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/actuator/info';
    if (window.location.hostname === 'localhost') {
      infoUrl = window.location.protocol + '//' + window.location.hostname + ':8997/actuator/info';
    }
    http.get<any>(infoUrl).subscribe(list => { resolve(list) });
  })
}

export namespace Dates {
  export const dateFormats = {
    format1: 'DD/MM/YYYY',
    format2: 'DD-MM-YYYY',
    format3: 'YYYY-MM-DD'
  }
  export function toDate(value: string,
    formats = [
      dateFormats.format1,
      dateFormats.format2,
      dateFormats.format3
    ]
  ): { valid: boolean, date: Date | null } {
    if (!value || !value.toString().trim().length) {
      return { valid: true, date: null };
    }
    let trimmed = value.toString().trim();
    if (!formats.some(format => format.length == trimmed.length)) {
      return { valid: false, date: null };
    }
    let date = moment(value.toString(), formats, true);
    if (!date.isValid()) {
      return { valid: false, date: null };
    }
    return { valid: true, date: date.toDate() };
  }
}

export function initializer(keycloak: KeycloakService, http: HttpClient): () => Promise<any> {
  let configOptions: any;
  return (): Promise<any> => {
    return new Promise(async (resolve, reject) => {
      try {
        await getPageInfo(http).then(opt => configOptions = opt);
        await keycloak.init({
          config: {
            url: configOptions.environment.keycloak.url,
            realm: configOptions.environment.keycloak.realm,
            clientId: configOptions.environment.keycloak.clientId
          },
          initOptions: {
            onLoad: 'login-required'
          },
          enableBearerInterceptor: true
        });
        resolve();
      } catch (error) {
        reject(error);
      }
    });
  };

}