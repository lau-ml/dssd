import { NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser"; // Asegúrate de incluir esto
import { HttpClientModule, provideHttpClient, withInterceptorsFromDi } from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { RouterModule } from "@angular/router"; // Cambiado a RouterModule
import { AppComponent } from "./app.component";
import { NavComponent } from "./components/nav/nav.component";
import { HomeComponent } from "./components/home/home.component";
import { TopnavComponent } from "./components/topnav/topnav.component";
import { FooterComponent } from "./components/footer/footer.component";
import { CargarMaterialesComponent } from "./components/cargar-materiales/cargar-materiales.component";
import { RegistroRecoleccionComponent } from "./components/registro-recoleccion/registro-recoleccion.component";
import {routes} from "./app.routes";
import {NgOptimizedImage} from "@angular/common";
import {NgOptionComponent, NgSelectComponent} from "@ng-select/ng-select";

@NgModule({
  declarations: [
    AppComponent,
    NavComponent,
    HomeComponent,
    TopnavComponent,
    FooterComponent,
    CargarMaterialesComponent,
    RegistroRecoleccionComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    RouterModule.forRoot(routes),
    NgOptimizedImage,
    ReactiveFormsModule,
    NgSelectComponent,
    NgOptionComponent,
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
