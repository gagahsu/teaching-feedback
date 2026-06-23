import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CourseService } from '../../services/course.service';
import { AuthService } from '../../services/auth.service';
import { CalendarDay } from '../../models/course.model';

interface CalCell {
  date: string; dayNum: number; inMonth: boolean; isToday: boolean; course?: CalendarDay;
}

@Component({ selector: 'app-calendar', templateUrl: './calendar.component.html' })
export class CalendarComponent implements OnInit {
  year  = new Date().getFullYear();
  month = new Date().getMonth() + 1;
  today = this.fmt(new Date());
  weeks: CalCell[][] = [];
  calData: Record<string, CalendarDay> = {};
  helpDays: { date: string; label: string }[] = [];
  totalHelp = 0;

  constructor(public auth: AuthService, private courseService: CourseService, public router: Router) {}

  ngOnInit() { this.load(); }

  load() {
    this.courseService.getCalendar(this.year, this.month).subscribe({
      next: d => { this.calData = d; this.build(); },
      error: () => this.build()
    });
  }

  build() {
    const firstDow = new Date(this.year, this.month - 1, 1).getDay();
    let cur = new Date(this.year, this.month - 1, 1 - firstDow);
    this.weeks = []; this.helpDays = []; this.totalHelp = 0;
    for (let w = 0; w < 6; w++) {
      const week: CalCell[] = [];
      for (let i = 0; i < 7; i++) {
        const dateStr = this.fmt(cur);
        const inMonth = cur.getMonth() === this.month - 1;
        const cd = this.calData[dateStr];
        if (inMonth && cd?.helpCount) {
          this.totalHelp += cd.helpCount;
          this.helpDays.push({ date: dateStr, label: `${cur.getMonth()+1}/${cur.getDate()} · ${cd.helpCount}` });
        }
        week.push({ date: dateStr, dayNum: cur.getDate(), inMonth, isToday: dateStr === this.today, course: cd });
        cur = new Date(cur.getFullYear(), cur.getMonth(), cur.getDate() + 1);
      }
      this.weeks.push(week);
    }
    if (this.weeks.length === 6 && this.weeks[5].every(d => !d.inMonth)) this.weeks.pop();
  }

  prev() { if (this.month === 1) { this.month = 12; this.year--; } else this.month--; this.load(); }
  next() { if (this.month === 12) { this.month = 1; this.year++; } else this.month++; this.load(); }
  goToday() { const n = new Date(); this.year = n.getFullYear(); this.month = n.getMonth()+1; this.load(); }
  openDay(date: string) { this.router.navigate(['/day', date]); }
  logout() { this.auth.logout(); this.router.navigate(['/login']); }

  get monthLabel() { return `${this.year} 年 ${this.month} 月`; }
  private fmt(d: Date) { return `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')}`; }
}
