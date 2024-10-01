import { Routes } from '@angular/router';
import { HomeComponent } from "./components/home/home.component";
import { CargarMaterialComponent } from './components/cargar-material/cargar-material.component';
import {RegistroRecoleccionComponent} from "./components/registro-recoleccion/registro-recoleccion.component";

export const routes: Routes = [

  { path: 'home', component: HomeComponent },
  { path: 'cargar-material', component: CargarMaterialComponent },
  {path: 'registro-recoleccion', component: RegistroRecoleccionComponent},




];


