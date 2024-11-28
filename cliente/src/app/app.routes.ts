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
import { PuntosRecoleccionComponent } from './components/usuario-recolector/puntos-recoleccion/puntos-recoleccion.component';
import { SolicitudPuntoRecoleccionComponent } from './components/usuario-recolector/solicitud-punto-recoleccion/solicitud-punto-recoleccion.component';
import { ListPuntosRecoleccionComponent } from './components/usuario-admin/puntos-recoleccion/list-puntos-recoleccion/list-puntos-recoleccion.component';
import { RecolectoresPuntoComponent } from './components/usuario-admin/puntos-recoleccion/recolectores-punto/recolectores-punto.component';
import { RecolectoresNoAsociadosPuntoComponent } from './components/usuario-admin/puntos-recoleccion/recolectores-no-asociados-punto/recolectores-no-asociados-punto.component';
import { ListRecolectoresComponent } from './components/usuario-admin/recolectores/list-recolectores/list-recolectores.component';
import { MaterialListComponent } from './components/usuario-admin/material/material-list/material-list.component';
import { MaterialEditComponent } from './components/usuario-admin/material/material-edit/material-edit.component';
import { MaterialCreateComponent } from './components/usuario-admin/material/material-create/material-create.component';
import { PuntoRecoleccionEditComponent } from './components/usuario-admin/puntos-recoleccion/punto-recoleccion-edit/punto-recoleccion-edit.component';
import { PuntoCreateComponent } from './components/usuario-admin/puntos-recoleccion/punto-create/punto-create.component';
import { AdministrarRecolectorComponent } from './components/usuario-admin/recolectores/administrar-recolector/administrar-recolector.component';
import { ListarOrdenesComponent } from './components/usuario-empleado/listar-ordenes/listar-ordenes.component';
import { ListaPagosRecolectorComponent } from './components/usuario-admin/recolectores/lista-pagos-recolector/lista-pagos-recolector.component';
import { ListaPuntosRecoleccionComponent } from './components/usuario-admin/recolectores/lista-puntos-recoleccion/lista-puntos-recoleccion.component';
import { RecolectoresPuntoAsociarComponent } from './components/usuario-admin/recolectores/recolectores-punto-asociar/recolectores-punto-asociar.component';
import { PagosRecolectoresComponent } from './components/usuario-empleado/pagos-recolectores/pagos-recolectores.component';
import { ListaPagosRecolectorComponent as MisPagos } from './components/usuario-recolector/lista-pagos-recolector/lista-pagos-recolector.component';
import { RegistroRecoleccionDetalleComponent } from './components/usuario-recolector/registro-recoleccion-detalle/registro-recoleccion-detalle.component';
import { RegistroComponent } from './components/usuario-admin/recolectores/registro/registro.component';

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
  { path: 'registro-recoleccion/:id', component: RegistroRecoleccionDetalleComponent, canActivate: [AuthGuard] },
  { path: 'puntos-recoleccion', component: PuntosRecoleccionComponent, canActivate: [AuthGuard] },
  { path: 'solicitar-puntos-recoleccion', component: SolicitudPuntoRecoleccionComponent, canActivate: [AuthGuard] },
  { path: 'lista-puntos-recoleccion', component: ListPuntosRecoleccionComponent, canActivate: [AuthGuard] },
  { path: 'puntos-recoleccion/edit/:id', component: PuntoRecoleccionEditComponent, canActivate: [AuthGuard] },
  { path: 'puntos-recoleccion/create', component: PuntoCreateComponent, canActivate: [AuthGuard] },
  { path: 'puntos-recoleccion/:id/recolectores', component: RecolectoresPuntoComponent, canActivate: [AuthGuard] },
  { path: 'puntos-recoleccion/:id/recolectores-no-asociados', component: RecolectoresNoAsociadosPuntoComponent, canActivate: [AuthGuard] },
  { path: 'list-recolectores', component: ListRecolectoresComponent, canActivate: [AuthGuard] },
  { path: 'recolector/:id/administrar', component: AdministrarRecolectorComponent, canActivate: [AuthGuard] },
  { path: 'recolector/:id/pagos', component: ListaPagosRecolectorComponent, canActivate: [AuthGuard] },
  { path: 'recolector/:id/puntos-de-recoleccion', component: ListaPuntosRecoleccionComponent, canActivate: [AuthGuard] },
  { path: 'recolector/:id/puntos-de-recoleccion/asociar', component: RecolectoresPuntoAsociarComponent, canActivate: [AuthGuard] },
  { path: 'recolector/:id/registro/:idRegistro', component: RegistroComponent, canActivate: [AuthGuard] },
  { path: 'pagos-recolectores', component: PagosRecolectoresComponent, canActivate: [AuthGuard] },
  { path: 'mis-pagos', component: MisPagos, canActivate: [AuthGuard] },
  { path: 'ordenes', component: ListarOrdenesComponent, canActivate: [AuthGuard] },
  { path: '**', redirectTo: "home" }


];


export const routing = RouterModule.forRoot(routes);
