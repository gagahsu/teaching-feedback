import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';

interface AuthResponse { token: string; name: string; role: string; }

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly url = `${environment.apiUrl}/auth`;
  private _token = new BehaviorSubject<string | null>(localStorage.getItem('tfb_token'));
  private _name  = new BehaviorSubject<string | null>(localStorage.getItem('tfb_name'));
  private _role  = new BehaviorSubject<string | null>(localStorage.getItem('tfb_role'));

  token$ = this._token.asObservable();

  get isLoggedIn() { return !!this._token.value; }
  get isTeacher()  { return this._role.value === 'TEACHER'; }
  get token()      { return this._token.value; }
  get name()       { return this._name.value; }
  get role()       { return this._role.value; }

  constructor(private http: HttpClient) {}

  login(name: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.url}/login`, { name, password })
      .pipe(tap(r => this.store(r)));
  }

  register(name: string, password: string, role: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.url}/register`, { name, password, role })
      .pipe(tap(r => this.store(r)));
  }

  logout() {
    ['tfb_token','tfb_name','tfb_role'].forEach(k => localStorage.removeItem(k));
    this._token.next(null); this._name.next(null); this._role.next(null);
  }

  private store(r: AuthResponse) {
    localStorage.setItem('tfb_token', r.token);
    localStorage.setItem('tfb_name',  r.name);
    localStorage.setItem('tfb_role',  r.role);
    this._token.next(r.token); this._name.next(r.name); this._role.next(r.role);
  }
}
