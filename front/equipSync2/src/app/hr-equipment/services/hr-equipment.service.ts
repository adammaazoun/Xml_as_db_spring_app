import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
@Injectable({
  providedIn: 'root'
})
export class HrEquipmentService {

  private apiHr = "http://localhost:8081/api/v1/equipments";

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
    return this.http.get<any>(`${this.apiHr}/all`); // Corrected string interpolation
  }

  update(employee: any) {
    const body = employee;
    return this.http.post<any>(`${this.apiHr}/update`, body); // Corrected string interpolation
  }
  
}
