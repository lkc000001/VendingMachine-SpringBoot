import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Subject } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../environments/environment';
import { Product } from '../models/product'
import { MemberOrder } from '../models/memberOrder'

let API_URL = environment.backendUrl;
@Injectable({
  providedIn: 'root'
})

export class SellService {

  shoppingCartList: MemberOrder[] = [];
  shoppingCartList$ = new BehaviorSubject<MemberOrder[]>([]);

  constructor(
      private http: HttpClient
    )
    {
      if (this.shoppingCartList.length > 0) {
          this.shoppingCartList$.next(this.shoppingCartList);
      }
    }

  CORSoption = {
    withCredentials: true
  };

  getShoppingCart() {
    return this.http.get<any>(`${API_URL}/shopping/getShoppingCart`, this.CORSoption).pipe(map(data => {
      this.shoppingCartList = data.data;
      this.shoppingCartList$.next(data.data);
      return data.data;
    }));
  }

  addShoppingCart(data: any) {
    return this.http.post<any>(`${API_URL}/shopping/addShoppingCart`, data, this.CORSoption).pipe(map(data => {
        return data.data;
    }));
  }
}
