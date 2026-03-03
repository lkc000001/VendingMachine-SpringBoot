import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Subject } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../environments/environment';
import { Product } from '../models/product'

let API_URL = environment.backendUrl;
@Injectable({
  providedIn: 'root'
})

export class ProductService {

  productList: Product[] = [];
  productList$ = new Subject<Product[]>();
  maxPage = 0;
  constructor(
      private http: HttpClient
    )
    {
      this.productList$ = new BehaviorSubject<Product[]>([]);
      if (this.productList.length > 0) {
          this.productList$.next(this.productList);
      }
    }

  queryProduct(param: any) {
    return this.http.post<any>(`${API_URL}/frontend/product/queryProduct`, param).pipe(map(data => {
      this.productList = data.data;
      this.productList$.next(data.data);
      this.maxPage = data.maxPage;
      return data.data;
    }));
  }
}
