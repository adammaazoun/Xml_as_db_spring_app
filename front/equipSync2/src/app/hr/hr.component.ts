import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HrService } from './services/hr.service';
import { Router,ActivatedRoute } from '@angular/router';



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
interface req1 {
  userId: string;
  equipmentId: string;
}

@Component({
    selector: 'app-hr',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './hr.component.html',
    styleUrls: ['./hr.component.css']
})
export class HrComponent implements OnInit {
  employees: Employee[] = [];

  employee: Employee = {
    userId: '',
    username: '',
    firstname: '',
    lastname: '',
    password: '',
    email: '',
    role: 'EMPLOYEE',
    skills: []
  };
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
  reqt:req1={
    userId:'',
    equipmentId:''
  };
  formEditing = false;
  formInserting = false;
  formaffectation = false;
  constructor(private hrService: HrService, private router: Router) {}
  ngOnInit() {
    this.loadEmployees();
    this.hrService.me().subscribe(
      (userData) => {
        // Store the user data in the component
        this.user = userData;
      },
      (error) => {
        console.error('Error fetching user data:', error);
      });
  }

  loadEmployees() {
    this.hrService.getAll().subscribe({
      next: (resp: Employee[]) => {
        this.employees = resp;
      },
      error: (err) => {
        console.error('Error loading employees', err);
      }
    });
  }

  openEditModal(employee: Employee) {
    this.formEditing = true;
    this.formInserting = false;

    // Ensure proper initialization of the employee object
    this.employee = {
      ...employee,
      skills: employee.skills && employee.skills.length > 0
        ? [...employee.skills]
        : [{ name: ''}] // Default skill if empty
    };
  }

  openInsertModal() {
    this.resetEmployeeForm();
    this.formInserting = true;
    this.formEditing = false;
    this.formaffectation = false;
  }
  openAffectModal() {
    this.resetEmployeeForm();
    this.formInserting = false;
    this.formEditing = false;
    this.formaffectation = true;
  }

  resetEmployeeForm() {
    this.employee = {
      userId: '',
      username: '',
      firstname: '',
      lastname: '',
      password: '',
      email: '',
      role: 'EMPLOYEE',
      skills: []
    };
  }

  addSkill() {
    this.employee.skills.push({ name: ''});
  }
  goToEquipment(){
    this.router.navigate(['/hr_equipment']);
  }
refrech(){
  window.location.reload();
}
search(){}
  removeSkill(index: number) {
    this.employee.skills.splice(index, 1);

    // Ensure at least one skill exists
    if (this.employee.skills.length === 0) {
      this.addSkill();
    }
  }

  logout() {
    localStorage.removeItem('role');
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  update() {
    const employeeToUpdate = {
      ...this.employee,
      skills: this.employee.skills.map(skill => skill.name)
    };

    this.hrService.update(employeeToUpdate).subscribe({
      next: () => {
        this.loadEmployees();
        this.formEditing = false;
      },
      error: (err) => {
        console.error('Update failed', err);
      }
    });
  }

  insert() {
    const newEmployee = {
      ...this.employee,
      skills: this.employee.skills.map(skill => skill.name)
    };

    this.hrService.insert(newEmployee).subscribe({
      next: () => {
        this.loadEmployees();
        this.formInserting = false;
      },
      error: (err) => {
        console.error('Insert failed', err);
      }
    });
  }
  affect(){
    const req = this.reqt;
  

    this.hrService.affect(req).subscribe({
      next: () => {
        this.formaffectation = false;
      },
      error: (err) => {
        console.error('Insert failed', err);
      }
    });
  }

  delete(userId: string) {
    this.hrService.delete(userId).subscribe({
      next: () => {
        this.employees = this.employees.filter(emp => emp.userId !== userId);
      },
      error: (err) => {
        console.error('Delete failed', err);
      }
    });
  }

  cancelModal() {
    this.formEditing = false;
    this.formInserting = false;
    this.formaffectation = false;
    this.resetEmployeeForm();
  }
}