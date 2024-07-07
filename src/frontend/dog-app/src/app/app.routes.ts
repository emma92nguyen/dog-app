import { Routes } from '@angular/router';
import { BreedListComponent } from './components/breed-list/breed-list.component';
import { BreedDetailComponent } from './components/breed-detail/breed-detail.component';
import { AnalyticsComponent } from './components/analytics/analytics.component';

export const routes: Routes = [
  { path: '', redirectTo: '/list-all', pathMatch: 'full' },
  { path: 'list-all', component: BreedListComponent },
  { path: 'detail/:id', component: BreedDetailComponent },
  { path: 'analytics', component: AnalyticsComponent },
  { path: '**', component: BreedListComponent },
];
