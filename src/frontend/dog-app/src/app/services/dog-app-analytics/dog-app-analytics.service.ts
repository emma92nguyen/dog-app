import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { DogAppRequestLog } from '../../models/dog-app-request-log';

const baseUrl = 'http://localhost:8080/admin';

@Injectable({
  providedIn: 'root',
})
export class DogAppAnalyticsService {
  constructor(private http: HttpClient) {}

  getAllRequestsLast7Days(): Observable<DogAppRequestLog[]> {
    return this.http.get<DogAppRequestLog[]>(
      `${baseUrl}` + '/report/tracking',
      {
        headers: new HttpHeaders({
          Authorization: 'Basic ' + btoa('admin:admin'),
        }),
      },
    );
  }
}
