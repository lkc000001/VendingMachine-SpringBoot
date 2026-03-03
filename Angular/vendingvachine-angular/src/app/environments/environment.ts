const service = 0;
let backendUrl = '';


switch (service) {
  case 0:
    backendUrl = 'http://localhost:8086/VendingMachine';
    break;
}

//設定每頁顯示產品的數量
const pageSize = 6;
//讀取圖片URL
const imageUrl = backendUrl + '/frontend/product/images/';

export const environment = {
  backendUrl,
  pageSize,
  imageUrl
};

