import { Injectable } from '@angular/core';
import swal from "sweetalert2";
@Injectable({
  providedIn: 'root'
})
export class SweetalertService {

  constructor() { }

  showLoadingAlert(): void {
    swal.fire({
      title: 'Cargando...',
      allowOutsideClick: false,
      didOpen: () => {
        swal.showLoading();
      },
    });
  }

  showAlert(icon: 'success' | 'error', title: string, text: string): void {
    swal.close();

    swal.fire({
      icon,
      title,
      text,
    });
  }
}
