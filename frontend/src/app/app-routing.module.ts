import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { CalendarComponent } from './pages/calendar/calendar.component';
import { DayViewComponent } from './pages/day-view/day-view.component';
import { AuthGuard } from './guards/auth.guard';

const routes: Routes = [
  { path: '', redirectTo: 'calendar', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'calendar', component: CalendarComponent },
  { path: 'day/:date', component: DayViewComponent },
  { path: '**', redirectTo: 'calendar' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
