import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NgSelectModule } from '@ng-select/ng-select'; // Import the module, not components
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
import { CargarMaterialesComponent } from './components/cargar-materiales/cargar-materiales.component';
import { RegistroRecoleccionComponent } from './components/registro-recoleccion/registro-recoleccion.component';
import {routing} from './app.routes';
// Your services and guards
import { JwtInterceptor } from './_helpers'; // Correct path
import { ErrorInterceptor } from './_helpers'; // Correct path
import {AuthGuard} from "./_guards";
import {NoAuthGuard} from "./_guards/noAuth.guard";
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
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    NgSelectModule, // Import the module here
    routing,
    NgOptimizedImage, // Other modules
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
    { provide: NoAuthGuard, useClass: NoAuthGuard }

  ],
  bootstrap: [AppComponent],
})
export class AppModule { }
