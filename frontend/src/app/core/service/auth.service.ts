import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {User} from '../model/user.model';
import {catchError, map, tap} from 'rxjs/operators';
import {BehaviorSubject, Observable, pipe, Subject, throwError, empty} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  readonly GRANT_TYPE_PASSWORD: string = 'password';
  readonly GRANT_TYPE_REFRESH: string = 'refresh_token';
  readonly AUTHORIZATION: string = 'Basic Y2xpZW50OnNlY3JldA==';
  readonly LOCAL_TOKEN_NAME: string = 'OAAT';
  readonly AUTHORIZE_URL: string = '/api/uaa/oauth/token';
  readonly REVOKE_URL: string = '/api/uaa/oauth/revoke';

  constructor(private http: HttpClient) {
  }

  login(user: User): Observable<void> {
    if (!this.isLogged()) {
      return this.obtainToken(user).pipe(
        tap((value: any) => {
          if (value) {
            this.storeToken({
              token: value.access_token,
              refreshToken: value.refresh_token,
              expires: new Date().getTime() + (1000 * value.expires_in)
            });
          } else {
            throw new Error('Login failed');
          }
        }),
        catchError(err => {
          throw new Error(err.error.error_description === 'Bad credentials' ? 'Username or password is incorrect' : 'Login failed');
        })
      );
    } else {
      return new BehaviorSubject<any>(this.getToken()).pipe(tap((value: any) => {
        if (!value) {
          throw new Error('Login failed');
        }
      }));
    }
  }

  logout(): void {
    this.revokeToken().subscribe();
    this.removeToken();
  }

  storeToken(token: any) {
    localStorage.setItem(this.LOCAL_TOKEN_NAME, JSON.stringify(token));
  }

  getToken(): any {
    return JSON.parse(localStorage.getItem(this.LOCAL_TOKEN_NAME));
  }

  removeToken() {
    localStorage.removeItem(this.LOCAL_TOKEN_NAME);
  }

  refreshToken(): Observable<any> {
    const token = this.getToken();
    this.removeToken();
    if (token && token.refreshToken) {
      return this.updateToken(token.refreshToken).pipe(tap((value: any) => {
        if (value) {
          this.storeToken({
            token: value.access_token,
            refreshToken: value.refresh_token,
            expires: new Date().getTime() + (1000 * value.expires_in)
          });
        } else {
          throw new Error('Refresh token failed.');
        }
      }));
    }
    return throwError('Can\'t find refresh token.');
  }

  isLogged(): boolean {
    return this.getToken() != null && new Date(this.getToken().expires) > new Date();
  }

  obtainToken(user: User) {
    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded',
      'Authorization': this.AUTHORIZATION
    });
    const params = new HttpParams()
      .set('username', user.username)
      .set('password', user.password)
      .set('grant_type', this.GRANT_TYPE_PASSWORD);

    const options = {
      headers,
      withCredentials: true
    };
    return this.http.post(this.AUTHORIZE_URL, params, options);
  }

  updateToken(refreshToken: string) {
    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded',
      'Authorization': this.AUTHORIZATION
    });
    const params = new HttpParams()
      .set('refresh_token', refreshToken)
      .set('grant_type', this.GRANT_TYPE_REFRESH);

    const options = {
      headers,
      withCredentials: true
    };
    return this.http.post(this.AUTHORIZE_URL, params, options);
  }

  revokeToken() {
    return this.http.delete(this.REVOKE_URL);
  }

}
