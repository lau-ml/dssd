import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {catchError, map, tap} from 'rxjs/operators';
import {environment} from '../../environments/environment';
import {LoginRequest} from "../_requests/loginRequest";
import {RegisterRequest} from "../_requests/registerRequest";
import {PasswordRequest} from "../_requests/passwordRequest";

@Injectable({providedIn: 'root'})
export class AuthenticationService {

  public currentUser: BehaviorSubject<String> = new BehaviorSubject<String>("");
  private isLogged: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  private isResendEmail: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  private isRecoverPass: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  constructor(private http: HttpClient) {
    this.isLogged = new BehaviorSubject<boolean>(sessionStorage.getItem("token") != null);
    this.currentUser = new BehaviorSubject<String>(sessionStorage.getItem("token") || "");
  }

  public get currentUserValue(): Observable<String> {
    return this.currentUser.asObservable();
  }

  public get isLoggedValue(): Observable<boolean> {
    return this.isLogged.asObservable();
  }

  public get isResendEmailValue(): Observable<boolean> {
    return this.isResendEmail.asObservable();
  }

  public get isRecoverPassValue(): Observable<boolean> {
    return this.isRecoverPass.asObservable();
  }

  login(credenciales: LoginRequest) {
    return this.http.post<any>(environment.urlHost + "auth/login", credenciales).pipe(
      tap((userData) => {
        sessionStorage.setItem("token", userData.token);
        this.currentUser.next(userData.token);
        this.isLogged.next(true);
      }),
      map((userData) => userData.token),

    );
  }

  resendEmail(email: String) {
    return this.http.post<any>(environment.urlHost + "auth/resend", {email: email}).pipe(
      tap((userData) => {
        this.isResendEmail.next(true);
      }),
      map((userData) => userData.message)
    );
  }

  recoverPassword(email: String) {
    return this.http.post<any>(environment.urlHost + "auth/recover", {email: email}).pipe(
      tap((userData) => {
        this.isRecoverPass.next(true);
      }),
      map((userData) => userData.message)
    );
  }

  verifyCode(code: String) {
    return this.http.patch<any>(environment.urlHost + "auth/verify", {"code": code}).pipe(
      map((userData) => userData.message)
    );
  }

  resetPassword(credentials: PasswordRequest) {
    return this.http.patch<any>(environment.urlHost + "auth/reset", credentials).pipe(
      map((userData) => userData.message)
    );
  }

  logout() {
    sessionStorage.removeItem('token');
    this.isLogged.next(false);
    this.currentUser.next("");

  }

  get userToken(): String {
    return this.currentUser.value;
  }
}
