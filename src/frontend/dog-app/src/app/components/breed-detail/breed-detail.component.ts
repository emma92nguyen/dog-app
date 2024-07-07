import { Component, OnInit } from '@angular/core';
import { MatCard, MatCardContent, MatCardImage } from '@angular/material/card';
import { MatGridList, MatGridTile } from '@angular/material/grid-list';
import { ActivatedRoute } from '@angular/router';
import { isNil } from 'lodash';
import { DogBreed } from '../../models/dog-breed';
import { NgIf, NgOptimizedImage } from '@angular/common';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { DogAppService } from '../../services/dog-app-service/dog-app.service';

@Component({
  selector: 'app-breed-detail',
  standalone: true,
  imports: [
    MatCardImage,
    MatCard,
    MatGridTile,
    MatCardContent,
    MatGridList,
    NgIf,
    NgOptimizedImage,
  ],
  templateUrl: './breed-detail.component.html',
  styleUrl: './breed-detail.component.scss',
})
export class BreedDetailComponent implements OnInit {
  breed: DogBreed | undefined = undefined;
  randomImg: SafeUrl = '';

  constructor(
    private route: ActivatedRoute,
    private dogAppService: DogAppService,
    private sanitizer: DomSanitizer,
  ) {}

  ngOnInit() {
    this.getBreedDetail();
  }

  getBreedDetail(): void {
    const breedName = this.route.snapshot.paramMap.get('id');
    if (!isNil(breedName)) {
      this.dogAppService.getDogBreed(breedName).subscribe({
        next: (res) => {
          this.breed = res;
          this.randomImg = this.getRandomImageForBreed();
        },
        error: (e) => console.error(e),
      });
    }
  }

  getRandomImageForBreed(): SafeUrl {
    if (!isNil(this.breed) && this.breed.imageUrls.length) {
      const img =
        this.breed.imageUrls[
          Math.floor(Math.random() * this.breed.imageUrls.length)
        ];
      return this.sanitizeImageUrl(img);
    }
    return '';
  }

  sanitizeImageUrl(imageUrl: string): SafeUrl {
    return this.sanitizer.bypassSecurityTrustUrl(imageUrl);
  }

  formatBreedName(): string {
    return this.dogAppService.createLabelTextForBreedListItem(
      this.breed!.breed,
    );
  }
}
