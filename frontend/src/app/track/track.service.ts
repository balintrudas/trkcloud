import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable, Subject} from 'rxjs';
import {Page} from '../core/model/page.model';

@Injectable({
  providedIn: 'root'
})
export class TrackService {

  constructor(private http: HttpClient) {
  }

  getTracks(page: Page): Observable<any> {
    return this.http.post('/api/search/tracks', page);
  }

  getPath(trackId: string): Observable<any> {
    return this.http.get(`/api/track/path/${trackId}`);
  }
}
