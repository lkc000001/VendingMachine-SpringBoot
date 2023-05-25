import React, { useState, useEffect, useRef  } from 'react'
import axios from "axios";
import Table from 'react-bootstrap/Table';
import { Button, ButtonGroup } from 'react-bootstrap';

const ShoppingCart = ({ shoppingCart, setCheckout, changeCountVer, changeShoppingCart, handleShoppingList }) => {

    const [ shoppingCartList, setShoppingCartList] = useState(shoppingCart);
  
    const [ balance , setBalance ] = useState(0);

    const [ changeCount , setChangeCount] = useState(0);
  
    var alltotal = shoppingCart.reduce((acc, curr) => acc + curr.total, 0);

    useEffect(() => {
        const memberId = shoppingCartList[0].memberId;
        if(memberId != '') {
            getBalance(memberId);
        }
        if(alltotal > balance) {
            setCheckout(false);
        } else {
            setCheckout(true);
        }
    }, [ shoppingCart, balance ]);

    const updateQuantity = (index, newQuantity) => {
        const item = shoppingCartList[index];
        item.buyQuantity = newQuantity;
        item.total = item.productPrice * newQuantity;
        setChangeCount(changeCount + 1 )
    };
  
    const getBalance = async (memberId) => {
        const apiUrl  = 'http://localhost:8086/VendingMachine/wallet/getBalance/' + memberId ;
        const { data } = await axios.get(apiUrl, {timeout: 3000})
        .then(rs => rs)
        .catch(error => { console.log(error); });
        
        setBalance(data.data);
    }

    const removeShoppingCartList = async (productId) => {
        const apiUrl  = 'http://localhost:8086/VendingMachine/shopping/removeShoppingCart' ;
        const requestData = {
            productId: productId
        };
        const { data } = await axios.post(apiUrl, requestData, { withCredentials: true })
        .then(rs => rs)
        .catch(error => { console.log(error); });

        console.log('state:',data.state)
        if(data.state === 4202) {
            changeShoppingCart([])
            setShoppingCartList([])
            handleShoppingList()
            return null;
        }
       changeCountVer();

        alltotal = shoppingCart.reduce((acc, curr) => acc + curr.total, 0);
        //修改購物車清單
        setShoppingCartList(data.data)
        //修改父類購物車清單
        changeShoppingCart(data.data)

    }

    return (
        <Table striped bordered hover>
            <thead>
                <tr>
                    <th width="40">刪除</th>
                    <th width="50" >編號</th>
                    <th width="200">名稱</th>
                    <th width="60">單價</th>
                    <th width="80">購買數量</th>
                    <th width="70">金額</th>
                </tr>
            </thead>
            <tbody>
            { 
                shoppingCartList.map( (sc, index) =>
                    <tr key={ sc.productId }>
                        <td>
                            <Button variant="warning" className='btnX' onClick={ () => removeShoppingCartList(sc.productId) }>
                                X
                            </Button>
                        </td>
                        <td> { sc.productId } </td>
                        <td> { sc.productName } </td>
                        <td> { sc.productPrice } </td>
                        <td>
                            <ButtonGroup>
                                <Button variant="secondary"
                                onClick={ () => updateQuantity(index, Math.max(0, sc.buyQuantity - 1)) }
                                > - </Button>
                                <Button variant="light">{sc.buyQuantity}</Button>
                                <Button variant="secondary"
                                onClick={ () => updateQuantity(index, sc.buyQuantity + 1) }
                                > + </Button>
                            </ButtonGroup>
                        </td>
                        <td> { sc.total } </td>
                        
                    </tr>
                )
            }
                <tr>
                    <td colSpan="5" >總計</td>
                    <td className={ alltotal > balance ? 'insufficientBalance' : ''}>{ alltotal }</td>
                </tr>
                <tr>
                    <td colSpan="5">餘額</td>
                    <td>{ balance }</td>
                </tr>
            </tbody>
        </Table>
    )
}

export default ShoppingCart
