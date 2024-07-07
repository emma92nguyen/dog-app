import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { isNil } from 'lodash';
import { PageData } from '../../models/page-data';
import { DogBreed } from '../../models/dog-breed';

const baseUrl = 'http://localhost:8080/breeds';

@Injectable({
  providedIn: 'root',
})
export class DogAppService {
  constructor(private http: HttpClient) {}

  getPaginatedData(pageNum: number, pageSize: number): Observable<PageData> {
    let queryParams = new HttpParams();
    queryParams = queryParams.append('page', pageNum);
    queryParams = queryParams.append('size', pageSize);
    return this.http.get<PageData>(`${baseUrl}` + '/list/all', {
      params: queryParams,
    });
  }

  getDogBreed(breed: string): Observable<DogBreed> {
    return this.http.get<DogBreed>(`${baseUrl}` + '/detail/' + `${breed}`);
  }

  createLabelTextForBreedListItem(breedName: string) {
    if (!isNil(breedName)) {
      return breedName
        .trim()
        .toLowerCase()
        .split('-')
        .map((subStr) => subStr.charAt(0).toUpperCase() + subStr.slice(1))
        .join(' ');
    }
    return '';
  }
}
