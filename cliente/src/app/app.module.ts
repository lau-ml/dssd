import { NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser"; // Asegúrate de incluir esto
import { HttpClientModule, provideHttpClient, withInterceptorsFromDi } from "@angular/common/http";
import { FormsModule } from "@angular/forms";
import { RouterModule } from "@angular/router"; // Cambiado a RouterModule
import { AppComponent } from "./app.component";
import { NavComponent } from "./components/nav/nav.component";
import { HomeComponent } from "./components/home/home.component";
import { Recoleccion_cargarComponent } from "../recoleccion_cargar/recoleccion_cargar.component";
import { TopnavComponent } from "./components/topnav/topnav.component";
import { FooterComponent } from "./components/footer/footer.component";
import { CargarMaterialComponent } from "./components/cargar-material/cargar-material.component";
import { RegistroRecoleccionComponent } from "./components/registro-recoleccion/registro-recoleccion.component";
import {routes} from "./app.routes";

@NgModule({
  declarations: [
    AppComponent,
    NavComponent,
    HomeComponent,
    Recoleccion_cargarComponent,
    TopnavComponent,
    FooterComponent,
    CargarMaterialComponent,
    RegistroRecoleccionComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    RouterModule.forRoot(routes),
  ],
  exports: [
    NavComponent,
    TopnavComponent,
    FooterComponent,
    HomeComponent,
  ],
  providers: [
    provideHttpClient(withInterceptorsFromDi())
  ],
  bootstrap: [AppComponent] // Agrega el bootstrap aquí
})
export class AppModule {}
