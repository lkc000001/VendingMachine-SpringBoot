import React, { useState, useEffect, useRef, useCallback } from 'react';
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
    //const [ searchVar, setSearchVar] = useState(0);
    const [ currentEtag, setCurrentEtag] = useState('');

    const headerRef = useRef(null);

    //依分頁取得商品資料
    const queryProduct = useCallback(async (pageIndex) => {
        const apiUrl  = 'http://localhost:8086/VendingMachine/frontend/product/queryProduct';
        const requestData = {
            pageIndex: pageIndex,
            pageSize: 6,
            enabled: 1,
            productName: searchString,
            classify: searchClassify
        };

        /*const { data } = await axios.post(apiUrl, requestData, {timeout: 3000})
        .then(rs => rs)
        .catch(error => { console.log(error); });*/
        console.log("currentEtag1:", currentEtag);
        const response = await axios.post(
            apiUrl,
            requestData,
            {
                timeout: 3000,
                headers: {
                'If-None-Match': currentEtag   // 如果要送舊的 ETag
                },
                validateStatus: function (status) {
                    return status === 200 || status === 304;
                }
            }
            );

        if (response.status === 304) {
            console.log("資料沒變，不更新");
            return;
        }
        const data = response.data;
        //currentEtag = response.headers['etag'];
        setCurrentEtag(response.headers['etag']);
        console.log("currentEtag2:", currentEtag);

        setState( state => ({
            products: data.data,
            maxPage: Array.from({ length: data.maxPage }),
            pageIndex: pageIndex
        }));
    }, [searchString, searchClassify, currentEtag]);

    //一進入頁面顯示產品資料
    useEffect(
        () => {
            queryProduct(1);
        },[ queryProduct ]
    );
    
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
       queryProduct(1);
    }

    const onChangeClassify = (value) => {
        setSearchClassify(value);
        setSearchString('');
    }

    const queryProduct1 = () => {
        queryProduct(1);
    }

    //傳至SearchBar參數
    const searchBarParameter = {
        searchString,
        setSearchString,
        searchProduct,
        searchClassify,
        setSearchClassify,
        onChangeClassify,
        queryProduct1
    }
    
    return (
        <div className="main-body">
                <div className="content">
                    <Header getMember = { getMember } 
                            ref={ headerRef } />
                    <div className="content-wrapper">
                        <div className="main-content">
                        <SearchBar searchBarParameter = { searchBarParameter } />
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