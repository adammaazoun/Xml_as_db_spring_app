import { HttpClient, HttpHeaders  } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private apiurl = "http://localhost:8081/api/v1/auth/login";
  private apiMe="http://localhost:8081/api/v1/users/me";
  constructor(private http: HttpClient) {
    localStorage.setItem('token',"");
  }

  login(username: string, password: string) {
    const body = { username, password };
    console.log("Request body:", body); // Log request body
    return this.http.post<any>(this.apiurl, body).pipe(
      tap(response => {
        console.log("Login successful, response:", response);
        if (response.token) {
          localStorage.setItem("token", response.token);
          localStorage.setItem("loggedIn", "true");
        }
      }),
      catchError((error) => {
        console.error("Login failed:", error);
        return throwError(() => new Error("Login failed"));
      })
    );
  }
  isLoggedIn(): boolean {
    return this.getToken();
  }
  getToken(): any {
    return localStorage.getItem('token');
  }

  getRole(): Observable<any> {
    const token = this.getToken();
    if (token) {
      const headers = new HttpHeaders({
        'Authorization': `Bearer ${token}`
      });
  
      return this.http.get<any>(this.apiMe, { headers }).pipe(
        tap(response => {
          // Log the entire response object for debugging
          console.log("API Response:", response); // This logs the full response object
  
          // Assuming the role is inside the response object
          const role = response?.role; // Adjust this if the role is nested differently in the response
          console.log("User role:", role); // Log the extracted role
        }),
        catchError((error) => {
          console.error("Failed to get role:", error);
          return throwError(() => new Error("Failed to get role"));
        })
      );
    } else {
      console.error("No token found.");
      return throwError(() => new Error("No token found"));
    }
  }

}
