import {AfterViewInit, Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {MatPaginator, MatSort} from '@angular/material';
import {GridDataSource} from './grid-datasource';
import {TrackService} from '../track.service';

@Component({
  selector: 'grid',
  templateUrl: './grid.component.html',
  styleUrls: ['./grid.component.scss']
})
export class GridComponent implements AfterViewInit, OnInit {

  @ViewChild(MatPaginator)
  paginator: MatPaginator;

  @ViewChild(MatSort)
  sort: MatSort;

  @Output()
  rowClick = new EventEmitter();

  @Input() selectedRow: any;
  @Output() selectedRowChange = new EventEmitter();

  dataSource: GridDataSource;
  selectedRowIndex: number = -1;

  constructor(private trackService: TrackService) {
  }

  displayedColumns = ['status', 'username', 'firstName', 'lastName', 'vehicleName', 'averageSpeed', 'distance', 'duration'];

  ngOnInit() {
    this.dataSource = new GridDataSource(this.paginator, this.sort, this.trackService);
  }

  ngAfterViewInit(): void {
    this.dataSource.connectPaginator();
    this.dataSource.getData();
  }

  searchTextChanged(event: any) {
    this.dataSource.getData(event.target.value);
  }

  rowClickHandler(row: any): void {
    this.selectedRow = row;
    this.selectedRowIndex = row.trackId;
    this.selectedRowChange.emit(row);
    this.rowClick.emit(row);
  }
}
