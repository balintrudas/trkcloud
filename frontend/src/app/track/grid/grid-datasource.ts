import {DataSource} from '@angular/cdk/collections';
import {MatPaginator, MatSort} from '@angular/material';
import {map, tap} from 'rxjs/operators';
import {Observable, BehaviorSubject} from 'rxjs';
import {TrackService} from '../track.service';
import {Page} from '../../core/model/page.model';

export interface GridItem {
  trackId: string;
  trackStatus: string;
}

export class GridDataSource extends DataSource<GridItem> {

  private dataSubject = new BehaviorSubject<GridItem[]>([]);

  constructor(private paginator: MatPaginator, private sort: MatSort, private trackService: TrackService) {
    super();
    this.paginator.length = 0;
  }

  connect(): Observable<GridItem[]> {
    return this.dataSubject.asObservable();
  }

  connectPaginator(): void {
    this.paginator.page.pipe(tap(() => this.getData())).subscribe();
  }

  disconnect() {
    this.dataSubject.complete();
  }

  getData(searchText = ''): void {
    this.trackService.getTracks(new Page(this.paginator.pageIndex, this.paginator.pageSize, searchText))
      .pipe(tap(x => this.paginator.length = x.totalElements), map(value => {
          return value.content;
        }
      )).subscribe(data => this.dataSubject.next(data));
  }

}
