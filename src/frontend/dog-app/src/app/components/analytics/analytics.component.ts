import { Component, OnInit } from '@angular/core';
import { DogAppAnalyticsService } from '../../services/dog-app-analytics/dog-app-analytics.service';
import { MatCard } from '@angular/material/card';
import { MatGridList, MatGridTile } from '@angular/material/grid-list';
import { FormsModule } from '@angular/forms';
import { MatIcon } from '@angular/material/icon';
import { MatIconButton } from '@angular/material/button';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow,
  MatRowDef,
  MatTable,
} from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { DogAppRequestLog } from '../../models/dog-app-request-log';

@Component({
  selector: 'app-analytics',
  standalone: true,
  imports: [
    MatCard,
    MatGridList,
    MatGridTile,
    FormsModule,
    MatIcon,
    MatIconButton,
    MatCell,
    MatCellDef,
    MatColumnDef,
    MatHeaderCell,
    MatHeaderRow,
    MatHeaderRowDef,
    MatPaginator,
    MatRow,
    MatRowDef,
    MatTable,
    MatHeaderCellDef,
  ],
  templateUrl: './analytics.component.html',
  styleUrl: './analytics.component.scss',
})
export class AnalyticsComponent implements OnInit {
  logData: DogAppRequestLog[] = [];
  displayedColumns: string[] = [
    'sessionId',
    'clientIp',
    'requestUrl',
    'logTime',
  ];

  constructor(private dogAppAnalyticsService: DogAppAnalyticsService) {}

  ngOnInit() {
    this.getLogData();
  }

  getLogData() {
    this.dogAppAnalyticsService.getAllRequestsLast7Days().subscribe({
      next: (res) => {
        if (res.length > 0) {
          this.logData = res;
        }
      },
      error: (e) => console.error(e),
    });
  }
}
