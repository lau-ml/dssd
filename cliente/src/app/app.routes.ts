import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from "./components/home/home.component";
import { CargarMaterialesComponent } from './components/cargar-materiales/cargar-materiales.component';
import { RegistroRecoleccionComponent } from "./components/registro-recoleccion/registro-recoleccion.component";
import { AuthGuard } from "./_guards";
import { NoAuthGuard } from "./_guards/noAuth.guard";
import { RecuperarContraComponent } from "./components/recuperar-contra/recuperar-contra.component";
import { VerificarComponent } from "./components/verificar/verificar.component";
import { RegisterComponent } from "./components/register/register.component";
import { LoginComponent } from "./components/login";
import { FormularioMaterialComponent } from './components/formulario-material/formulario-material.component';

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
  { path: 'registro-recoleccion', component: RegistroRecoleccionComponent, canActivate: [AuthGuard] },
  { path: '**', redirectTo: "home" }


];


export const routing = RouterModule.forRoot(routes);
