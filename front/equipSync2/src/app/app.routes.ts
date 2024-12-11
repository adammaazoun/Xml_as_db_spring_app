// app-routing.module.ts
import { Routes } from '@angular/router';
import { HrComponent } from './hr/hr.component';
import { EmComponent } from './em/em.component';
import { LoginComponent } from './login/login.component';
import { HrEquipmentComponent } from './hr-equipment/hr-equipment.component';
export const routes: Routes = [
  {
    path: 'hr',
    component: HrComponent
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'hr_equipment',
    component: HrEquipmentComponent
  },
  {
    path: 'em',
    component: EmComponent
  },

  {
    path: '**',
    redirectTo: 'login'  // Redirects unknown paths to HRComponent
  }
];