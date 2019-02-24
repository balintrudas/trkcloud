import {Component, OnInit} from '@angular/core';
import {BreakpointObserver, Breakpoints} from '@angular/cdk/layout';
import {map} from 'rxjs/operators';
import {Observable} from 'rxjs';
import {NavigationEnd, Router} from '@angular/router';
import {AuthService} from '../core/service/auth.service';
import {MatSidenav} from '@angular/material';

@Component({
  selector: 'layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss']
})
export class LayoutComponent implements OnInit {

  pageTitle: string;

  breakPointHandler: Observable<boolean> =
    this.breakpointObserver.observe([Breakpoints.Small, Breakpoints.XSmall]).pipe(
      map(result => result.matches),
    );

  constructor(private breakpointObserver: BreakpointObserver, private router: Router, private authService: AuthService) {
    this.onTitleChanges();
  }

  ngOnInit() {
  }

  toggle(drawer: MatSidenav): void {
    if (drawer.mode === 'over') {
      drawer.toggle();
    }
  }

  onTitleChanges() {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        if (this.router.routerState.root) {
          const title = this.getTitle(this.router.routerState, this.router.routerState.root);
          this.pageTitle = title[title.length - 1];
        }
      }
    });
  }

  getTitle(state, parent) {
    const data = [];
    if (parent && parent.snapshot.data && parent.snapshot.data.title) {
      data.push(parent.snapshot.data.title);
    }

    if (state && parent) {
      data.push(... this.getTitle(state, state.firstChild(parent)));
    }
    return data;
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['login']);
  }

}
