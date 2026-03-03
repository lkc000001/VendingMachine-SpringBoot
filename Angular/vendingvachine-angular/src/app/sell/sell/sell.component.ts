import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ProductService } from '../../services/product.service'
import { SellService } from '../../services/sell.service'
import { LoginService } from '../../services/login.service';
import { ShowMessageComponent } from '../../utils/show-message/show-message.component';
import { Product } from '../../models/product'
import { MemberOrder } from '../../models/memberOrder'
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-sell',
  templateUrl: './sell.component.html',
  styleUrls: ['./sell.component.css']
})

export class SellComponent implements OnInit {
  constructor(
    private productService: ProductService,
    private sellService: SellService,
    private loginService: LoginService,
    private modalService: NgbModal
  ) {}

  productList$ = this.productService.productList$.asObservable();

  imageUrl: string = environment.backendUrl + '/frontend/product/images/';
  productList: Product[] = [];
  memberOrder: MemberOrder = new MemberOrder();
  maxPage = 0;

  buyQuantityNameList = ["#buyQuantity1", "#buyQuantity2", "#buyQuantity3", "#buyQuantity4", "#buyQuantity5", "#buyQuantity6"];

  ngOnInit(): void {
    let param = { "pageIndex": 1, "pageSize": environment.pageSize };
    this.queryProduct(param);
  }

  queryProduct(param: any) {
    this.productService.queryProduct(param).subscribe((data) => {
      this.productList = data;
      this.maxPage = this.productService.maxPage;
    });
  }

  addShoppingCart(index: number) {
    if (!this.loginService.checkLogin()) {
      this.shoeMessage('錯誤', '登入後才可加入購物車');
      return ;
    } else {
      const buyQuantityInputValue = parseInt((<HTMLInputElement>document.getElementById(this.buyQuantityNameList[index])).value);
      const price = this.productList[index].price
      if(buyQuantityInputValue <= 0) {
        this.shoeMessage('數量錯誤', '購買數量需大於0');
        return ;
      }
      if(buyQuantityInputValue > price) {
        this.shoeMessage('數量錯誤', '購買數量不可大於庫存數量');
        return ;
      }

      this.memberOrder.memberId = this.loginService.getMemberId();
      this.memberOrder.productId = this.productList[index].productId;
      this.memberOrder.productCost = this.productList[index].cost;
      this.memberOrder.productPrice = price;
      this.memberOrder.buyQuantity = buyQuantityInputValue;
      this.memberOrder.total = buyQuantityInputValue * this.productList[index].price;

      this.sellService.addShoppingCart(this.memberOrder).subscribe((data) => {
        this.sellService.getShoppingCart().subscribe((data) => {});
      });
      this.shoeMessage('成功', '加入購物車成功');
    }
  }

  private shoeMessage(title: string, body: string) {
    const modalRef = this.modalService.open(ShowMessageComponent);
    modalRef.componentInstance.model = 'message';
    modalRef.componentInstance.title = title;
    modalRef.componentInstance.body = body;
    modalRef.result.then((result) => {

    });
  }
}
