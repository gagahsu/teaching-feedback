import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Course, CalendarDay } from '../models/course.model';

@Injectable({ providedIn: 'root' })
export class CourseService {
  private readonly url = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getCalendar(year: number, month: number): Observable<Record<string, CalendarDay>> {
    return this.http.get<Record<string, CalendarDay>>(`${this.url}/calendar`, {
      params: { year: year.toString(), month: month.toString() }
    });
  }

  getCourse(date: string): Observable<Course> {
    return this.http.get<Course>(`${this.url}/courses/${date}`);
  }

  saveCourse(date: string, title: string, content: string): Observable<Course> {
    return this.http.put<Course>(`${this.url}/courses/${date}`, { title, content });
  }
}
