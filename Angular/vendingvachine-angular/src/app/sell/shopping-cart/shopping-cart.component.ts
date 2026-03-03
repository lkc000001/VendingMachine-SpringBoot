import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

import { SellService } from '../../services/sell.service'
import { MemberOrder } from '../../models/memberOrder';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.css']
})
export class ShoppingCartComponent {

  shoppingCartList$: Observable<MemberOrder[]> = this.sellService.shoppingCartList$.asObservable();

  imageUrl: string = environment.backendUrl + '/shopping/images/';

  constructor(
    private sellService: SellService,
    private router: Router
  ){
    this.sellService.getShoppingCart().subscribe((data) => {
      if (data.length === 0) {
          this.router.navigate(['/']);
      }
    });
  }
}
