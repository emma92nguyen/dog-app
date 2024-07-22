import { Component, OnInit } from '@angular/core';
import { MatGridList, MatGridTile } from '@angular/material/grid-list';
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
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatFormField } from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';
import { MatInput } from '@angular/material/input';
import { RouterLink } from '@angular/router';
import { DogBreedListItem } from '../../models/dog-breed-list-item';
import { DogAppService } from '../../services/dog-app-service/dog-app.service';
import { isNil } from 'lodash';

@Component({
  selector: 'app-breed-list',
  standalone: true,
  imports: [
    MatGridList,
    MatGridTile,
    MatTable,
    MatPaginator,
    MatFormField,
    FormsModule,
    MatInput,
    MatHeaderRow,
    MatRow,
    MatCell,
    MatHeaderCell,
    MatColumnDef,
    MatRowDef,
    MatHeaderRowDef,
    MatCellDef,
    MatHeaderCellDef,
    RouterLink,
  ],
  templateUrl: './breed-list.component.html',
  styleUrl: './breed-list.component.scss',
})
export class BreedListComponent implements OnInit {
  paginatedData: DogBreedListItem[] = [];

  displayedColumns: string[] = ['name'];

  length = 50;
  pageSize = 10;
  pageIndex = 0;
  pageSizeOptions = [5, 10];

  hidePageSize = false;
  showPageSizeOptions = true;
  showFirstLastButtons = true;
  disabled = false;

  pageEvent: PageEvent | undefined;

  constructor(private dogAppService: DogAppService) {}

  ngOnInit() {
    this.getPaginatedBreedData();
  }

  handlePageEvent(e: PageEvent) {
    this.pageEvent = e;
    this.length = e.length;
    this.pageSize = e.pageSize;
    this.pageIndex = e.pageIndex;
    this.getPaginatedBreedData();
  }

  getPaginatedBreedData() {
    this.dogAppService
      .getPaginatedData(this.pageIndex, this.pageSize)
      .subscribe({
        next: (res) => {
          if (!isNil(res.dogBreeds) && res.dogBreeds.length > 0) {
            this.length = res.totalItems;
            this.paginatedData = [];
            res.dogBreeds.map((dogBreed: string, index: number) => {
              const listItem = {
                number: index,
                breed: dogBreed,
                labelText:
                  this.dogAppService.createLabelTextForBreedListItem(dogBreed),
              };
              this.paginatedData.push(listItem);
            });
          }
          this.paginatedData.push();
        },
        error: (e) => console.error(e),
      });
  }
}
