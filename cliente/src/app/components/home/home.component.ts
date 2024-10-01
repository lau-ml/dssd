import { Component } from '@angular/core';
import { RegistroRecoleccionComponent } from '../registro-recoleccion/registro-recoleccion.component';

@Component({
  selector: 'app-home',
  standalone: true,
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
  imports: [RegistroRecoleccionComponent]
})
export class HomeComponent {

}
