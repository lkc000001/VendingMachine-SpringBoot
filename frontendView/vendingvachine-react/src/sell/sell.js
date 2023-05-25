import React, { useState, useEffect, useRef } from 'react';
import 'bootstrap/dist/css/bootstrap.css';
import '../CSS/sell.css';
import Header from '../util/header';
import axios from "axios";
import Card from './card';
import Pagination from '../util/pagination';
import SearchBar from './searchBar';

const Sell = () => {

    const [ { products, maxPage, pageIndex }, setState ] = useState({
        products: [],
        maxPage: [],
        pageIndex: 1
    });
    
    const [ memberId , setMemberId ] = useState('');

    const [ searchString, setSearchString] = useState('');
    const [ searchClassify, setSearchClassify] = useState('');
    const [ searchVar, setSearchVar] = useState(0);

    //一進入頁面顯示產品資料
    useEffect(
        () => {
            queryProduct(1);
        },[ searchVar ]
    );

    const headerRef = useRef(null);

    //依分頁取得商品資料
    const queryProduct = async (pageIndex) => {
        const apiUrl  = 'http://localhost:8086/VendingMachine/frontend/product/queryProduct';
        const requestData = {
            pageIndex: pageIndex,
            pageSize: 6,
            enabled: 1,
            productName: searchString,
            classify: searchClassify
        };

        const { data } = await axios.post(apiUrl, requestData, {timeout: 3000})
        .then(rs => rs)
        .catch(error => { console.log(error); });

        setState( state => ({
            products: data.data,
            maxPage: Array.from({ length: data.maxPage }),
            pageIndex: pageIndex
        }));
    }

    const showMessage = (title, message) => {
        headerRef.current.showMessage(title, message);
    }

    const changeCountVer = () => {
        headerRef.current.changeCountVer();
    }

    const getMember = (loginMemberDetails) => {
        setMemberId(loginMemberDetails.memberId);
    }

    const searchProduct = () => {
        setSearchVar(searchVar + 1);
    }

    const onChangeClassify = () => {
        setSearchString('');
        setSearchVar(searchVar + 1);
    }

    //傳至SearchBar參數
    const searchBarParameter = {
        searchString,
        setSearchString,
        searchProduct,
        searchClassify,
        setSearchClassify,
        onChangeClassify
    }
    
    return (
        <div className="main-body">
                <div className="content">
                    <Header getMember = { getMember } 
                            ref={ headerRef } />
                    <div className="content-wrapper">
                        <div className="main-content">
                        <SearchBar searchBarParameter = { searchBarParameter }/>
                        { products.map( ( product, index) =>
                            <Card key={ product.productId } product = { product } 
                                                            showMessage = { showMessage }
                                                            memberId = { memberId }
                                                            changeCountVer = { changeCountVer } /> 
                        )}
                        {
                            maxPage.length !== 1 &&
                            <Pagination query = { queryProduct } pageIndex = { pageIndex }
                                    maxPage = { maxPage } />
                        }
                        </div>
                    </div>
                </div>
          </div>
    );
}

export default Sell;