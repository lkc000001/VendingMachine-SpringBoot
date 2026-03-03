import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './utils/header/header.component';
import { SellComponent } from './sell/sell/sell.component';
import { PaginationComponent } from './utils/pagination/pagination.component';
import { LoginComponent } from './login/login/login.component';
import { ShowMessageComponent } from './utils/show-message/show-message.component';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AddMemberComponent } from './member/add-member/add-member.component';
import { ShoppingCartComponent } from './sell/shopping-cart/shopping-cart.component';
import { HomeComponent } from './home/home.component';
import { MemberComponent } from './member/member/member.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    SellComponent,
    PaginationComponent,
    LoginComponent,
    ShowMessageComponent,
    AddMemberComponent,
    ShoppingCartComponent,
    HomeComponent,
    MemberComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule,
    NgbModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
