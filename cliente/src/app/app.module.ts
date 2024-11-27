import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NgSelectModule } from '@ng-select/ng-select';
import { NgOptimizedImage } from '@angular/common';

// Your components
import { AppComponent } from './app.component';
import { NavComponent } from './components/nav/nav.component';
import { TopnavComponent } from './components/topnav/topnav.component';
import { FooterComponent } from './components/footer/footer.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login';
import { RegisterComponent } from './components/register/register.component';
import { VerificarComponent } from './components/verificar/verificar.component';
import { RecuperarContraComponent } from './components/recuperar-contra/recuperar-contra.component';
import { CargarMaterialesComponent } from './components/usuario-recolector/cargar-materiales/cargar-materiales.component';
import { RegistroRecoleccionComponent } from './components/usuario-recolector/registro-recoleccion/registro-recoleccion.component';
import { FormularioMaterialComponent } from './components/usuario-empleado/formulario-material/formulario-material.component';
import { routing } from './app.routes';
// Your services and guards
import { JwtInterceptor } from './_helpers'; // Correct path
import { ErrorInterceptor } from './_helpers'; // Correct path
import { AuthGuard } from "./_guards";
import { NoAuthGuard } from "./_guards/noAuth.guard";
import { RecolectoresListComponent } from './components/usuario-empleado/recolectores-list/recolectores-list.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { PuntosRecoleccionComponent } from './components/usuario-recolector/puntos-recoleccion/puntos-recoleccion.component';
import { SolicitudPuntoRecoleccionComponent } from './components/usuario-recolector/solicitud-punto-recoleccion/solicitud-punto-recoleccion.component';
import { ListPuntosRecoleccionComponent } from './components/usuario-admin/puntos-recoleccion/list-puntos-recoleccion/list-puntos-recoleccion.component';
import { RecolectoresPuntoComponent } from './components/usuario-admin/puntos-recoleccion/recolectores-punto/recolectores-punto.component';
import { RecolectoresNoAsociadosPuntoComponent } from './components/usuario-admin/puntos-recoleccion/recolectores-no-asociados-punto/recolectores-no-asociados-punto.component';
import { PuntoCreateComponent } from './components/usuario-admin/puntos-recoleccion/punto-create/punto-create.component';
import { PuntoRecoleccionEditComponent } from './components/usuario-admin/puntos-recoleccion/punto-recoleccion-edit/punto-recoleccion-edit.component';
import { MaterialListComponent } from './components/usuario-admin/material/material-list/material-list.component';
import { MaterialEditComponent } from './components/usuario-admin/material/material-edit/material-edit.component';
import { MaterialCreateComponent } from './components/usuario-admin/material/material-create/material-create.component';
import { ListRecolectoresComponent } from './components/usuario-admin/recolectores/list-recolectores/list-recolectores.component';
import { AdministrarRecolectorComponent } from './components/usuario-admin/recolectores/administrar-recolector/administrar-recolector.component';
import { ListarOrdenesComponent } from './components/usuario-empleado/listar-ordenes/listar-ordenes.component';
import { PadLeftPipe } from './pipes/pad-left.pipe';
import { ListaPagosRecolectorComponent } from './components/usuario-admin/recolectores/lista-pagos-recolector/lista-pagos-recolector.component';
import { ListaPagosRecolectorComponent as MisPagos } from './components/usuario-recolector/lista-pagos-recolector/lista-pagos-recolector.component';
import { PaginationComponent } from './components/pagination/pagination.component';
import { BreadcrumbComponent } from './components/breadcrumb/breadcrumb.component';
import { ListaPuntosRecoleccionComponent } from './components/usuario-admin/recolectores/lista-puntos-recoleccion/lista-puntos-recoleccion.component';
import { RecolectoresPuntoAsociarComponent } from './components/usuario-admin/recolectores/recolectores-punto-asociar/recolectores-punto-asociar.component';
import { PagosRecolectoresComponent } from './components/usuario-empleado/pagos-recolectores/pagos-recolectores.component';
import { RegistroRecoleccionDetalleComponent } from './components/usuario-recolector/registro-recoleccion-detalle/registro-recoleccion-detalle.component';
@NgModule({
  declarations: [
    AppComponent,
    NavComponent,
    TopnavComponent,
    FooterComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    VerificarComponent,
    RecuperarContraComponent,
    CargarMaterialesComponent,
    RegistroRecoleccionComponent,
    FormularioMaterialComponent,
    RecolectoresListComponent,
    MaterialListComponent,
    MaterialEditComponent,
    MaterialCreateComponent,
    PuntosRecoleccionComponent,
    SolicitudPuntoRecoleccionComponent,
    ListPuntosRecoleccionComponent,
    PuntoRecoleccionEditComponent,
    PuntoCreateComponent,
    RecolectoresPuntoComponent,
    RecolectoresNoAsociadosPuntoComponent,
    ListRecolectoresComponent,
    AdministrarRecolectorComponent,
    ListarOrdenesComponent,
    PadLeftPipe,
    ListaPagosRecolectorComponent,
    PaginationComponent,
    BreadcrumbComponent,
    ListaPuntosRecoleccionComponent,
    RecolectoresPuntoAsociarComponent,
    PagosRecolectoresComponent,
    MisPagos,
    RegistroRecoleccionDetalleComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    NgSelectModule,
    routing,
    NgOptimizedImage,
    NgbModule
  ],
  exports: [
    NavComponent,
    TopnavComponent,
    FooterComponent,
    HomeComponent,
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
    { provide: AuthGuard, useClass: AuthGuard },
    { provide: NoAuthGuard, useClass: NoAuthGuard },
    provideAnimationsAsync()

  ],
  bootstrap: [AppComponent],
})
export class AppModule { }
