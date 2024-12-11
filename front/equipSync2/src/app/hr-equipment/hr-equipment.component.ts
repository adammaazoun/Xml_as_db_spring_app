import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HrEquipmentService } from './services/hr-equipment.service';
import { Router,ActivatedRoute } from '@angular/router';
interface Equipment {
  equipmentId: string;
  category: string;
  tasks: task[];
  details: string;
  status: string;
  photo: string;  
}
interface task {
  task: string;
}
interface Skill {
  name: string;
}

interface Employee {
  userId: string;
  username: string;
  firstname: string;
  lastname: string;
  password: string;
  email: string;
  role: string;
  skills: Skill[];
}
@Component({
  selector: 'app-hr-equipment',
  imports: [CommonModule, FormsModule],
  templateUrl: './hr-equipment.component.html',
  styleUrl: './hr-equipment.component.css'
})

export class HrEquipmentComponent {
  equipment: Equipment[]=[];
  user: Employee = {
    userId: '',
    username: '',
    firstname: '',
    lastname: '',
    password: '',
    email: '',
    role: 'HR',
    skills: []
  };
  constructor(private hrequipmentService: HrEquipmentService, private router: Router) {}
  ngOnInit() {
    this.loadequipment();
    this.hrequipmentService.me().subscribe(
      (userData) => {
        // Store the user data in the component
        this.user = userData;
      },
      (error) => {
        console.error('Error fetching user data:', error);
      });
  }
  loadequipment() {
    this.hrequipmentService.getAll().subscribe({
      next: (resp: Equipment[]) => {
        this.equipment = resp;
      },
      error: (err) => {
        console.error('Error loading equipments', err);
      }
    });
  }
  goToEmployees(){
    this.router.navigate(['/hr']);
  }
refrech(){
  window.location.reload();
}
logout() {
  localStorage.removeItem('role');
  localStorage.removeItem('token');
  this.router.navigate(['/login']);
}
search(){}

}
