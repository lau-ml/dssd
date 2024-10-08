import { Component } from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {AuthenticationService} from "../../services/authentication.service";

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrl: './nav.component.css'
})
export class NavComponent {
  userLoginOn:boolean=false;

  constructor(private loginService:AuthenticationService, private router:Router) { }
  ngOnInit(): void {
    this.loginService.isLoggedValue.subscribe(
      {
        next:(userLoginOn) => {
          this.userLoginOn=userLoginOn;
        }
      }
    )
  }
}
