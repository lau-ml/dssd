import { Routes } from '@angular/router';
import { HomeComponent } from "./components/home/home.component";
import { CargarMaterialesComponent } from './components/cargar-materiales/cargar-materiales.component';
import {RegistroRecoleccionComponent} from "./components/registro-recoleccion/registro-recoleccion.component";

export const routes: Routes = [

  { path: 'home', component: HomeComponent },
  { path: 'cargar-materiales', component: CargarMaterialesComponent },
  {path: 'registro-recoleccion', component: RegistroRecoleccionComponent},




];


