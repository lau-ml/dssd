import { NgModule } from "@angular/core";
import { NavComponent } from "./components/nav/nav.component";
import { HomeComponent } from "./components/home/home.component";
import { Recoleccion_cargarComponent } from "../recoleccion_cargar/recoleccion_cargar.component";
import { TopnavComponent } from "./components/topnav/topnav.component";
import { FooterComponent } from "./components/footer/footer.component";
import { HttpClientModule, provideHttpClient, withInterceptorsFromDi } from "@angular/common/http";
import {CargarMaterialComponent} from "./components/cargar-material/cargar-material.component";

@NgModule({
  imports: [HomeComponent],
  declarations: [
    NavComponent,
    Recoleccion_cargarComponent,
    TopnavComponent,
    FooterComponent,
    CargarMaterialComponent
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
})

export class AppModule {
}
