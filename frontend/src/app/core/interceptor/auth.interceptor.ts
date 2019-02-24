import {Injectable} from '@angular/core';
import {HttpClient, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AuthService} from '../service/auth.service';
import {catchError, flatMap, map} from 'rxjs/operators';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService, private router: Router) {
  }

  private getRequestWithAuthentication(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const req = request.clone({
      headers: request.headers.set('Authorization', 'Bearer ' + this.authService.getToken().token)
    });
    return next.handle(req);
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    if (request.url === this.authService.AUTHORIZE_URL && request.method !== 'DELETE') {
      return next.handle(request);
    }

    if (this.authService.isLogged()) {
      return this.getRequestWithAuthentication(request, next);
    } else {
      return this.authService.refreshToken().pipe(flatMap((value: any) => {
          return this.getRequestWithAuthentication(request, next);
        }),
        catchError(err => {
          this.router.navigate(['login']);
          return next.handle(request);
        }));
    }
  }
}
