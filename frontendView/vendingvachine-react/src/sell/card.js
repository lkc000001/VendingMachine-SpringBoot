import React, { Component } from 'react';
import 'bootstrap/dist/css/bootstrap.css';
import '../CSS/sell.css';
import axios from "axios";

const checkLoginUrl  = "http://localhost:8086/VendingMachine/frontend/checkLogin";
const addShoppingCartUrl  = "http://localhost:8086/VendingMachine/shopping/addShoppingCart";
const productImagesUrl  = "http://localhost:8086/VendingMachine/frontend/product/images/";

class card extends Component {

    state = {
        inputBuyQuantity : 0
    };

    setInputBuyQuantity = (event) => {
        this.setState({
            inputBuyQuantity: event.target.value
        });

    };

    addShoppingCart = async () => {
        //檢查是否已登入
        const checkLoginResp = await axios.get(checkLoginUrl, { withCredentials: true })
        .then(rs => rs.data)
        .catch(error => { console.log(error); });
        console.log(checkLoginResp)
        if(checkLoginResp.state === 200) {
            const { productId, price, stock, cost } = this.props.product;
            const inputBuyQuantity = this.state.inputBuyQuantity;

            if ( inputBuyQuantity == '' || inputBuyQuantity <= 0) {
                this.props.showMessage('數量錯誤', '購買數量不可為0');
                return null;
            }
            if ( inputBuyQuantity > stock ) {
                this.props.showMessage('數量錯誤', '購買數量不可大於庫存數量');
                return null;
            }
            const productPrice = price;

            const shoppingCart = {
                memberId: this.props.memberId,
                productId: productId,
                productPrice: productPrice,
                productCost: cost,
                buyQuantity: inputBuyQuantity,
                total: inputBuyQuantity * productPrice
            };

            const productShoppingCarts = await axios.post(addShoppingCartUrl, shoppingCart, { withCredentials: true })
                                                    .then(rs => rs.data)
                                                    .catch(error => { this.props.showMessage('', '加入購物車失敗'); });
            
            this.props.changeCountVer();
            this.setState({
                inputBuyQuantity: 0
            });
            
            if(productShoppingCarts.state === 200) {
                this.props.showMessage('', '加入購物車成功');
            } else {
                this.props.showMessage('', '加入購物車失敗');
            }
            
        } else {
            this.props.showMessage('尚未登入', '請先登入後才可加入購物車');
        }
    }

    render() {
        const { productId, productName, image, price, stock, cost } = this.props.product;
        return (
            <div className="card border-primary mb-3 mr-3 cardWidth displayInline">
                <input type="hidden" id="cost" name="cost"/>

                <span className="cardTitle">{ productName }</span>
                {
                    image != null && <img src={ productImagesUrl + image } className="cardImage" alt="..."/>
                }
                <div className="card-body pb-1">
                    <span className="cardText displayInline mr-5" >價格: { price }</span>
                    <span className="cardText displayInline ml-5" >庫存: { stock }</span>
                </div>
                <div className="card-body pt-1">
                    <span className="cardText displayInline">購買數量：</span>
                    <input type="number" 
                            value= { this.state.inputBuyQuantity }
                            className="cardText displayInline width60" 
                            disabled={ stock  <= 0 ? true : false } 
                            onChange={ this.setInputBuyQuantity }/>
                </div>
                {
                    stock  <= 0 ? <div className="btn btn-danger cardBtn mb-2 customLink">庫存不足</div> : 
                                          <div className="btn btn-primary cardBtn mb-2" onClick={ this.addShoppingCart }>加入購物車</div>
                }
                
            </div>
        );
    }
}

export default card;