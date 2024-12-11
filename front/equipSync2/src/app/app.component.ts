import { Component, Inject, PLATFORM_ID, OnInit } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { LoginComponent } from './login/login.component';
import { HttpClientModule } from '@angular/common/http';
import { HrComponent } from './hr/hr.component';
import { Router } from '@angular/router';
import { RouterModule, RouterOutlet } from '@angular/router';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    imports: [CommonModule, HttpClientModule, RouterModule],
    styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'equipSync';
  loggedIn = '';
  role: string | null = null;

  constructor(@Inject(PLATFORM_ID) private platformId: any, private router: Router) {}

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      // Access localStorage only in the browser
      this.loggedIn = localStorage.getItem('loggedIn') || 'false';
      this.role = localStorage.getItem('role');
      console.log('Role:', this.role);
      console.log('LoggedIn:', this.loggedIn);
    }
  }

  // Logout function to clear localStorage
  public logout() {
    localStorage.removeItem('role');
    localStorage.removeItem('loggedIn');
    this.role = null;  // Update role to null to hide the HR component
    console.log('Logged out');
    this.router.navigate(['/login']);
  }
}
