import { NgModule } from "@angular/core";
import { NavComponent } from "../nav/nav.component";
import { HomeComponent } from "../home/home.component";
import { Recoleccion_cargarComponent } from "../recoleccion_cargar/recoleccion_cargar.component";
import { TopnavComponent } from "../topnav/topnav.component";
import { FooterComponent } from "../footer/footer.component";
import { HttpClientModule, provideHttpClient, withInterceptorsFromDi } from "@angular/common/http";

@NgModule({
  imports: [HomeComponent],
  declarations: [
    NavComponent,
    Recoleccion_cargarComponent,
    TopnavComponent,
    FooterComponent,
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
