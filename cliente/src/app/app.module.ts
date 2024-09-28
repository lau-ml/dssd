import {NgModule} from "@angular/core";
import {NavComponent} from "../nav/nav.component";
import {HomeComponent} from "../home/home.component";
import {Recoleccion_cargarComponent} from "../recoleccion_cargar/recoleccion_cargar.component";
import {TopnavComponent} from "../topnav/topnav.component";
import {FooterComponent} from "../footer/footer.component";

@NgModule({
  imports: [],
  declarations: [
    HomeComponent,
    NavComponent,
    Recoleccion_cargarComponent,
    TopnavComponent,
    FooterComponent
  ],
  exports: [
    NavComponent,
    TopnavComponent,
    FooterComponent
  ]
})

export class AppModule {
}
