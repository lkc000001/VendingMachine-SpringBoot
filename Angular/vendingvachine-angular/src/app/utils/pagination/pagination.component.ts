import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { ProductService } from '../../services/product.service'
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.css']
})
export class PaginationComponent {

  @Input() maxPage: number = 0;
  pagesArray: number[] = [];
  pageIndex: number = 1;

  constructor(
    private productService: ProductService
  ) {}


  ngOnChanges(changes: SimpleChanges) {
    if (changes['maxPage'] && !changes['maxPage'].firstChange) {
      this.createPagesArray();
    }
  }

  private createPagesArray() {
    this.pagesArray = Array(this.maxPage).fill(0).map((x, i) => i + 1);
  }

  changePage(page: any) {
    let ischange = true;
    if (page === "min") {
      page = 1;
    }
    if (page === "max") {
      page = this.maxPage;
    }
    ischange = page === this.pageIndex ? false : true;
    if(ischange) {
      let param = { "pageIndex": page, "pageSize": environment.pageSize };
      this.queryProduct(param);
      this.pageIndex = page;
    }

  }

  queryProduct(param: any) {
    this.productService.queryProduct(param).subscribe((data) => {
      this.maxPage = this.productService.maxPage;
    });
  }

}
