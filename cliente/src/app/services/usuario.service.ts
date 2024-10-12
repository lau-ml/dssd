import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject, map} from "rxjs";
import {tap} from "rxjs/operators";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  private nombre: BehaviorSubject<string> = new BehaviorSubject<string>("");
  private apellido: BehaviorSubject<string> = new BehaviorSubject<string>("");
  private email: BehaviorSubject<string> = new BehaviorSubject<string>("");
  private id: BehaviorSubject<number> = new BehaviorSubject<number>(0);
  private usuario: BehaviorSubject<any> = new BehaviorSubject<any>("")

  constructor(private http: HttpClient) {
  }

  getUsuario() {
    return this.http.get<any>(environment.urlHost + "user/").pipe(
      tap((usuarioData) => {
        this.nombre.next(usuarioData.nombre);
        this.apellido.next(usuarioData.apellido);
        this.email.next(usuarioData.email);
        this.usuario.next(usuarioData.usuario);
        this.id.next(usuarioData.id);
      }),
      map((usuarioData) => usuarioData)
    );
  }


  getNombre() {
    return this.nombre.asObservable();
  }

  getApellido() {
    return this.apellido.asObservable();
  }

  getEmail() {
    return this.email.asObservable();
  }

  getId() {
    return this.id.asObservable();
  }


  updateNombre(nombre: string) {
    this.nombre.next(nombre);
  }

}
