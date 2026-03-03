import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { SellService } from '../../services/sell.service'
import { LoginService } from '../../services/login.service';
import { ShowMessageComponent } from './../show-message/show-message.component';
import { environment } from '../../environments/environment';
import { Member } from '../../models/member';

let API_URL = environment.backendUrl;

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  member: Member = new Member();
  shoppingCartList$ = this.sellService.shoppingCartList$.asObservable();
  isLogin: boolean = false;

  constructor(
    private sellService: SellService,
    private loginService: LoginService,
    private modalService: NgbModal
  ){
    this.sellService.getShoppingCart().subscribe(() => {});
  }

  ngOnInit() {
    /*this.loginService.currentUser.subscribe(
      (user) => (this.currentUser = user)
    );
    */
    this.loginService.currentMember
    this.member = this.loginService.getMember();

    this.checkLogin();
  }

  login() {
    const modalRef = this.modalService.open(ShowMessageComponent);
    modalRef.componentInstance.model = 'login';
    modalRef.result.then((result) => {
      this.checkLogin();
    });
  }

  logout() {
    this.loginService.logout();
    this.isLogin = false;
  }

  async register() {

  }

  async checkLogin() {
    this.isLogin = await this.loginService.checkIsLogin();
    console.log("this.isLogin:", this.isLogin)
  }
}
