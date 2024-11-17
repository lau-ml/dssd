import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from "./components/home/home.component";
import { CargarMaterialesComponent } from './components/usuario-recolector/cargar-materiales/cargar-materiales.component';
import { RegistroRecoleccionComponent } from "./components/usuario-recolector/registro-recoleccion/registro-recoleccion.component";
import { AuthGuard } from "./_guards";
import { NoAuthGuard } from "./_guards/noAuth.guard";
import { RecuperarContraComponent } from "./components/recuperar-contra/recuperar-contra.component";
import { VerificarComponent } from "./components/verificar/verificar.component";
import { RegisterComponent } from "./components/register/register.component";
import { LoginComponent } from "./components/login";
import { FormularioMaterialComponent } from './components/usuario-empleado/formulario-material/formulario-material.component';
import { RecolectoresListComponent } from './components/usuario-empleado/recolectores-list/recolectores-list.component';
import { MaterialListComponent } from './components/usuario-admin/material-list/material-list.component';
import { MaterialEditComponent } from './components/usuario-admin/material-edit/material-edit.component';
import { MaterialCreateComponent } from './components/usuario-admin/material-create/material-create.component';
import { PuntosRecoleccionComponent } from './components/usuario-recolector/puntos-recoleccion/puntos-recoleccion.component';

export const routes: Routes = [

  { path: 'home', component: HomeComponent },
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [NoAuthGuard]
  }
  ,
  {
    path: 'register',
    component: RegisterComponent,
    canActivate: [NoAuthGuard]

  },
  {
    path: 'verify',
    component: VerificarComponent,
    canActivate: [NoAuthGuard]

  },
  {
    path: 'reset',
    component: RecuperarContraComponent,
    canActivate: [NoAuthGuard]
  },
  {
    path: 'cargar-materiales', component: CargarMaterialesComponent
    , canActivate: [AuthGuard]
  },
  {
    path: 'formulario-material', component: FormularioMaterialComponent
    , canActivate: [AuthGuard]
  },
  {
    path: 'recolectores', component: RecolectoresListComponent
    , canActivate: [AuthGuard]
  },
  {
    path: 'lista-materiales', component: MaterialListComponent
    , canActivate: [AuthGuard]
  },
  { path: 'material-edit/:id', component: MaterialEditComponent },
  { path: 'material-new', component: MaterialCreateComponent },
  { path: 'registro-recoleccion', component: RegistroRecoleccionComponent, canActivate: [AuthGuard] },
  { path: 'puntos-recoleccion', component: PuntosRecoleccionComponent, canActivate: [AuthGuard] },
  { path: '**', redirectTo: "home" }


];


export const routing = RouterModule.forRoot(routes);
