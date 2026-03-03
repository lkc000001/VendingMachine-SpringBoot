import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ShoppingCartComponent } from './sell/shopping-cart/shopping-cart.component';

import { AuthGuard } from './utils/auth.guard';
import { SellComponent } from './sell/sell/sell.component';
import { Member } from './models/member';



const routes: Routes = [
  { path: '', component: SellComponent },
  { path: 'shoppingCart', component: ShoppingCartComponent, canActivate: [AuthGuard]  },
  { path: 'member', component: Member, canActivate: [AuthGuard]  },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
