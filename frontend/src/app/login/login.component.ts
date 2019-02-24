import {Component, OnInit, ViewChild} from '@angular/core';
import {User} from '../core/model/user.model';
import {AuthService} from '../core/service/auth.service';
import {MatSnackBar} from '@angular/material';
import {Router} from '@angular/router';
import {Form, NgForm} from '@angular/forms';

@Component({
  selector: 'login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  user: User = new User('', '');
  loginErrorMessage: string;

  @ViewChild('lform')
  form: NgForm;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit() {
    this.onFormValueChanges();
  }

  onSubmit() {
    this.authService.login(this.user).subscribe(value => {
      this.router.navigate(['']);
    }, err => {
      this.loginErrorMessage = err.message;
    });
  }

  onFormValueChanges() {
    this.form.valueChanges.subscribe(value => {
      this.loginErrorMessage = null;
    });
  }

}
