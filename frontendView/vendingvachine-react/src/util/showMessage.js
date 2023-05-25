import React, { useState, useEffect } from 'react';
import { Button, Modal } from 'react-bootstrap';
import axios from "axios";
import Login from '../login/LoginPage';

const ShowMessage = (props) => {

    const { show, modalTitle, modalMessage, handleShow, addMemberOrder, isCheckout, changeShoppingCart, changeCountVer } = props.showMessageParameter;

    const clearShoppingCart = async () => {
        const apiUrl  = 'http://localhost:8086/VendingMachine/shopping/clearShoppingCart' ;
        const { data } = await axios.get(apiUrl, { withCredentials: true })
        .then(rs => rs)
        .catch(error => { console.log(error); });
        changeShoppingCart();
        changeCountVer();
        handleShow();
    }

    return (
        <div>
            <Modal show={ show } onHide={ handleShow } animation={false} centered size={ modalTitle == '購物車' && 'lg' }>
                <Modal.Header closeButton>
                    <Modal.Title >{ modalTitle }</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    { modalMessage }
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={ handleShow }>
                        關閉
                    </Button>
                    {
                        modalTitle == '購物車' && 
                            <Button variant={ isCheckout ? "primary" : "danger" }  onClick={ addMemberOrder } disabled={ !isCheckout }>
                                { isCheckout ? "結帳" : "餘額不足"} 
                            </Button>
                    }
                    {
                        modalTitle == '購物車' && 
                            <Button variant="warning" onClick={ clearShoppingCart } >
                                清空購物車
                            </Button>
                    }
                </Modal.Footer>
            </Modal> 
        </div>
    );
}

export default ShowMessage;