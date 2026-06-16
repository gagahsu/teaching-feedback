import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({ selector: 'app-login', templateUrl: './login.component.html' })
export class LoginComponent {
  mode: 'login' | 'register' = 'login';
  name = ''; password = ''; role = 'STUDENT';
  error = ''; loading = false;

  constructor(private auth: AuthService, private router: Router) {
    if (auth.isLoggedIn) router.navigate(['/calendar']);
  }

  submit() {
    if (!this.name || !this.password) return;
    this.loading = true; this.error = '';
    const obs = this.mode === 'login'
      ? this.auth.login(this.name, this.password)
      : this.auth.register(this.name, this.password, this.role);
    obs.subscribe({
      next: () => this.router.navigate(['/calendar']),
      error: (e: { error?: { message?: string } }) => {
        this.error = e.error?.message || '登入失敗，請再試';
        this.loading = false;
      }
    });
  }
}
