import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class HrService {
  private apiHr = "http://localhost:8081/api/v1/users/hr";

  constructor(private http: HttpClient) {}
  me(){
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.get<any>(`http://localhost:8081/api/v1/users/me`,{ headers }); // Corrected string interpolation
  }

  insert(employee: any) {
    const body = employee;
    return this.http.post<any>(`${this.apiHr}/insert`, body); // Corrected string interpolation
  }

  delete(id: string) {
    return this.http.delete<any>(`${this.apiHr}/${id}`); // Corrected string interpolation
  }

  getEmployee(id: string) {
    return this.http.get<any>(`${this.apiHr}/${id}`); // Corrected string interpolation
  }

  getAll() {
    return this.http.get<any>(`${this.apiHr}/all-emloyes`); // Corrected string interpolation
  }

  update(employee: any) {
    const body = employee;
    return this.http.post<any>(`${this.apiHr}/update`, body); // Corrected string interpolation
  }
  affect(request: any) {
    const body = request;
    return this.http.post<any>(`${this.apiHr}/affect`, body); // Corrected string interpolation
  }
}
