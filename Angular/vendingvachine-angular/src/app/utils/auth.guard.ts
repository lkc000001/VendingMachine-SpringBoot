import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, ActivatedRoute, UrlTree } from '@angular/router';
import { Observable , Subscription } from 'rxjs';
import { map } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';

import { SellService } from '../services/sell.service'
import { environment } from '../environments/environment';

let API_URL = environment.backendUrl;

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {

  shoppingCartList$ = this.sellService.shoppingCartList$.asObservable();
  istrue: boolean = false;

  constructor(
    private http: HttpClient,
    private sellService: SellService
  ) {
    this.sellService.getShoppingCart().subscribe(() => { });
  }

  CORSoption = {
    withCredentials: true
  };

  private subscription: Subscription = new Subscription();

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    let urlFilter = ['/', '/login'];
    if (urlFilter.filter(data => { return data === state.url }).length == 0) {
      const userData = localStorage.getItem('currentUser');
      console.log("userData:", userData);
      if(userData === null) {
        return false;
      }
    }

    return true;
  }
}
