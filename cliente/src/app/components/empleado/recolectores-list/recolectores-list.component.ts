import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UsuarioService } from '../../../services/usuario.service';
import { RecolectorDTO } from '../../../models/recolector.dto';

@Component({
  selector: 'app-recolectores-list',
  templateUrl: './recolectores-list.component.html',
  styleUrls: ['./recolectores-list.component.css']
})
export class RecolectoresListComponent implements OnInit {
  recolectores: RecolectorDTO[] = [];

  constructor(private usuarioService: UsuarioService, private router: Router) { }

  ngOnInit(): void {
    this.cargarRecolectores();
  }

  cargarRecolectores(): void {
    this.usuarioService.getRecolectores().subscribe((data: RecolectorDTO[]) => {
      this.recolectores = data;
    });
  }

  generarFormulario(recolector: RecolectorDTO): void {
    this.usuarioService.setRecolector(recolector);
    this.router.navigate([`formulario-material`]);
  }
}
