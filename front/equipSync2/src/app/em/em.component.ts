import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EmService } from './services/em.service';
import { Router,ActivatedRoute } from '@angular/router';

interface Equipment {
  equipmentId: string;
  category: string;
  tasks: task[];
  details: string;
  status: string;
  photo: string;  
}
interface Equipment2 {
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
  selector: 'app-em',
  imports: [CommonModule, FormsModule],
  templateUrl: './em.component.html',
  styleUrl: './em.component.css'
})
export class EmComponent {
  equipment: Equipment2[]=[];
  user: Employee = {
    userId: '',
    username: '',
    firstname: '',
    lastname: '',
    password: '',
    email: '',
    role: 'Equipment',
    skills: []
  };
  equip: Equipment = {
    equipmentId: '',
  category: '',
  tasks: [],
  details: '',
  status: '',
  photo: '',
  };
  equip2: Equipment2 = {
    equipmentId: '',
  category: '',
  tasks: [],
  details: '',
  status: '',
  photo: '',
  };
  
  formEditing = false;
  formInserting = false;
  constructor(private hrequipmentService: EmService, private router: Router) {}
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
      next: (resp: Equipment2[]) => {
        this.equipment = resp;
        console.log(resp);
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

searchTerm = '';

search() {
  if (!this.searchTerm) {
    this.loadequipment();
    return;
  }

  this.equipment = this.equipment.filter(equip => 
    equip.category.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
    equip.details.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
    equip.status.toLowerCase().includes(this.searchTerm.toLowerCase())
  );
}
openEditModal(equip: Equipment2) {
  this.formEditing = true;
  this.formInserting = false;

  // Ensure proper initialization of the employee object
  this.equip2 = {
    ...equip,
    tasks: equip.tasks && equip.tasks.length > 0
      ? [...equip.tasks]
      : [{ task: ''}] // Default skill if empty
  };
}

openInsertModal() {
  this.resetEmployeeForm();
  this.formInserting = true;
  this.formEditing = false;
}
openAffectModal() {
  this.resetEmployeeForm();
  this.formInserting = false;
  this.formEditing = false;
  
}

resetEmployeeForm() {
  this.equip = {
    equipmentId: '',
  category: '',
  tasks: [],
  details: '',
  status: '',
  photo: "",
  };
}

addSkill() {
  this.equip.tasks.push({ task: ''});
}
removeSkill(index: number) {
  this.equip.tasks.splice(index, 1);

  // Ensure at least one skill exists
  if (this.equip.tasks.length === 0) {
    this.addSkill();
  }
}

logout() {
  localStorage.removeItem('role');
  localStorage.removeItem('token');
  this.router.navigate(['/login']);
}

update() {
  const equipToUpdate = {
    ...this.equip,
    tasks: this.equip.tasks.map(task => task.task),
    photo: this.equip.photo // Send the byte[] as-is
  };

  this.hrequipmentService.update(equipToUpdate).subscribe({
    next: () => {
      this.loadequipment();
      this.formEditing = false;
    },
    error: (err) => {
      console.error('Update failed', err);
    }
  });
}

insert() {
  const newEquip = {
    
    ...this.equip,
    tasks: this.equip.tasks.map(tasks => tasks.task),
    photo: this.equip.photo // Send the byte[] as-is
  };
  console.log(newEquip);
  this.hrequipmentService.insert(newEquip).subscribe({
    next: () => {
      this.loadequipment();
      this.formInserting = false;
    },
    error: (err) => {
      console.error('Insert failed', err);
    }
  });
}

delete(equipmentId: string) {
  this.hrequipmentService.delete(equipmentId).subscribe({
    next: () => {
      // Remove the deleted equipment from the list
      this.equipment = this.equipment.filter(e => e.equipmentId !== equipmentId);
    },
    error: (err) => {
      console.error('Delete failed', err);
    }
  });
}

cancelModal() {
  this.formEditing = false;
  this.formInserting = false;
  this.resetEmployeeForm();
}
// In your component
onFileSelected(event: Event): void {
  const input = event.target as HTMLInputElement;

  if (!input.files || input.files.length === 0) {
    alert('No file selected.');
    return;
  }

  const file: File = input.files[0];

  // Validate file type
  const allowedTypes = ['image/jpeg', 'image/png', 'image/gif'];
  if (!allowedTypes.includes(file.type)) {
    alert('Invalid file type. Please upload a JPEG, PNG, or GIF.');
    return;
  }

  // Validate file size
  const maxSizeInBytes = 5 * 1024 * 1024; // 5MB
  if (file.size > maxSizeInBytes) {
    alert('File is too large. Maximum size is 5MB.');
    return;
  }

  // Read the file as Data URL (Base64 string)
  const reader = new FileReader();
  reader.onloadend = () => {
    if (reader.result) {
      const base64String = reader.result as string;

      // Assign the Base64 string directly to the `equip.photo` property
      this.equip.photo = base64String.split(',')[1]; // Get the Base64 data without metadata
    } else {
      alert('Error reading the file.');
    }
  };

  reader.onerror = () => {
    alert('An error occurred while reading the file.');
  };

  reader.readAsDataURL(file); // Read the file as a Data URL (Base64 encoded string)
}


getImageSrc(photo: string | null): string {
  if (!photo || photo.length === 0) {
    return 'assets/default-image.jpg'; // Fallback for missing images
  }

  // Directly return the Base64 string if it's already in Base64 format
  return `data:image/jpeg;base64,${photo}`;
}




}
