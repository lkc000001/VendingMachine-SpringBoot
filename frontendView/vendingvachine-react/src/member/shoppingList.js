import React, { useState, useEffect, useRef, Fragment  } from 'react'
import axios from "axios";
import Table from 'react-bootstrap/Table';
import { Button, ButtonGroup } from 'react-bootstrap';
import Header from '../util/header';
import MemberNav from './memberNav';
import Pagination from '../util/pagination';

const queryMemberOrderGroupUrl  = 'http://localhost:8086/VendingMachine/memberorder/queryMemberOrderGroup';

const ShoppingList = () => {

  const [ { memberOrders, maxPage, pageIndex }, setState ] = useState({
    memberOrders: [],
    maxPage: [],
    pageIndex: 1
  });

  const [ memberId , setMemberId ] = useState(null);

  const [ showCount , setShowCount ] = useState(10);

  //一進入頁面顯示清單
  useEffect(
    () => {
      if(memberId != null) {
        queryMemberOrder(1);
      }
    },[ memberId,  showCount]
  );
  
  const getMember = (loginMemberDetails) => {
    setMemberId(loginMemberDetails.memberId);
  }


  const queryMemberOrder = async (pageIndex) => {
    const requestData = {
        pageIndex: pageIndex,
        pageSize: showCount,
        memberId: memberId
    };
    const { data } = await axios.post(queryMemberOrderGroupUrl, requestData, {timeout: 3000})
    .then(rs => rs)
    .catch(error => { console.log(error); });

    setState( state => ({
      memberOrders: data.data,
      itemsCount: data.itemsCount,
      maxPage: Array.from({ length: data.maxPage }),
      pageIndex: pageIndex
    }));
    
  }

  const changeShowCount = (e) => {
    const count = e.target.value;
    setShowCount(count);
  }

  return (
    <div >
      <div className="content">
        <Header getMember = { getMember }/>
        <div>
            <MemberNav />
            <div className="search-block-line-title mt-3 ">
              <div className="showright mb-2">
                顯示筆數： 
                <select value={ showCount } onChange={ changeShowCount }>
                  <option value="10">10</option>
                  <option value="25">25</option>
                  <option value="50">50</option>
                </select>
              </div>
            </div>
            <Table striped bordered hover >
              <thead>
                <tr>
                    <th width="100" >訂單編號</th>
                    <th width="80" >產品編號</th>
                    <th width="200">產品名稱</th>
                    <th width="60">單價</th>
                    <th width="60">購買數量</th>
                    <th width="80">金額</th>
                </tr>
              </thead>
              <tbody>
              {
                memberOrders.map((orderMap, index) => (
                  <Fragment key={ index }>
                    {
                      Object.keys(orderMap).map((orderNo) => (
                      <Fragment key={ orderNo } >
                      {
                        orderMap[orderNo].map((mo, index) => (
                        <tr key={ mo.productId } >
                          {
                            index == 0 && <td className='shoppingListTd' rowSpan={ orderMap[ orderNo ].length }>{ orderNo }</td>
                          }
                          <td>{ mo.productId } </td>
                          <td>{ mo.productName } </td>
                          <td>{ mo.productPrice } </td>
                          <td>{ mo.buyQuantity } </td>
                          <td>{ mo.total } </td>
                        </tr>
                        ))
                      }
                        
                      </Fragment>
                      ))
                    }
                  </Fragment>
                ))
              }
              </tbody>
          </Table>
          {
              maxPage.length !== 1 ?
                <Pagination query = { queryMemberOrder } pageIndex = { pageIndex }
                                    maxPage = { maxPage } /> :
                <div><br /></div>
          }
        </div>
      </div>
    </div>
    
  )
}

export default ShoppingList
