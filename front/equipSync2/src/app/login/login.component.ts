import { Component } from '@angular/core';
import { LoginService } from './services/login.service';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [FormsModule, CommonModule, HttpClientModule]
})
export class LoginComponent {
  public role = '';
  user: { username: string; password: string } = {
    username: '',
    password: ''
  };

  constructor(public loginService: LoginService, private router: Router) {
    localStorage.setItem('loggedIn', 'false');
  }
  ngOnInit(){
    localStorage.removeItem('role');
  localStorage.removeItem('token');
  }
  onSubmit() {
    this.loginService.login(this.user.username, this.user.password).subscribe(
      (response: any) => {
        console.log('Login response:', response);
        const token = response.token;  // assuming the token is in the response
        localStorage.setItem('token', token);
        this.loginService.getRole().subscribe(
          (resp: any) => {
            this.role = resp.role;
            localStorage.setItem('role', this.role);
            localStorage.setItem('loggedIn', 'true');  // Update loggedIn status
            console.log('Role:', this.role);
            if (this.role==="HR") {
              this.router.navigate(['/hr']);
            }
            else if (this.role==="EMPLOYEE") {
              this.router.navigate(['/employee']);
            }
            else if (this.role==="EM") {
              this.router.navigate(['/em']);
            }
            
          }
        );
      },
      (error) => {
        console.error('Login failed:', error);
      }
    );
  }
}
