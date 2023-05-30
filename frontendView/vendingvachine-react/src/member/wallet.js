import React, { useState, useEffect, useRef  } from 'react'
import axios from "axios";
import Header from '../util/header'
import MemberNav from './memberNav'
import '../CSS/wallet.css';
import { Button } from 'react-bootstrap';

const getWalletUrl  = 'http://localhost:8086/VendingMachine/wallet/findByMemberId/';
const addWalletUrl  = 'http://localhost:8086/VendingMachine/wallet/addWallet/';

const Wallet = () => {

  const [ { memberId, memberName } , setMemberDetails ] = useState({ 
        memberId: '',  
        memberName: ''
  });

  const [ wallet , setWallet ] = useState([]);
  const [ balance , setBalance ] = useState(0);
  const [ amount , setAmount ] = useState(0);

  const headerRef = useRef(null);

    const showMessage = (title, message) => {
        headerRef.current.showMessage(title, message);
    }

  //一進入頁面取得餘額
  useEffect(
    () => {
        if(memberId != '') {
          getWallet();
          setAmount(0);
        }
    },[ memberId, balance ]
  );

  const getMember = (loginMemberDetails) => {
      setMemberDetails(loginMemberDetails);
  }

  const getWallet = async () => {
    const requestData = {
      pageIndex: 1,
      pageSize: 10,
      memberId: memberId
    };
    const { data } = await axios.post(getWalletUrl, requestData, {timeout: 3000})
    .then(rs => rs)
    .catch(error => { console.log(error); });

    setWallet(data.data)
    setBalance(data.data[0].balance);
  }

  const saveWallet = async () => {
    const requestData = {
      memberId: memberId,
      amount: amount
    };
    const { data } = await axios.post(addWalletUrl, requestData, {timeout: 3000})
    .then(rs => rs)
    .catch(error => { console.log(error); });

    if(data.state === 4101) {
      showMessage("失敗", data.data);
      return null;
    }
    setBalance(data.data);
    showMessage("","儲值成功");
  }

  return (
    <div className="main-body">
      <div className="content">
        <Header getMember = { getMember } ref={ headerRef } />
        <main >
          <div>
            <MemberNav />
            <div className="search-block mt-3">
              <div className="search-block-line-title mb-1">
                  <span className='' >會員明細</span>
              </div>
              <div className="search-block-line">
                <div>
                  <label>
                    <span>會員編號：</span>
                    <input type="text" value={ memberId !== null ?  memberId : '' } readOnly={true} />
                  </label>
                  &nbsp;&nbsp;&nbsp;
                  <label>
                    <span>會員姓名：</span>
                    <input type="text" value={ memberName !== null ?  memberName : '' } readOnly={true} />
                  </label>
                </div>
              </div>
              <div className="search-block-line">
                <div>
                  <label>
                    <span>餘　　額：</span>
                    <input type="text" value={ balance } readOnly={true} />
                  </label>
                </div>
              </div>
            </div>

            <div className="search-block mt-3">
              <div className="search-block-line-title mb-1">
                  <span className='' >儲　　值</span>
              </div>
              <div className="search-block-line">
                <div>
                  <label>
                    <span>儲值金額：</span>
                    <input type="number" value={ amount } onChange={(e) => setAmount( e.target.value )} />
                  </label>
                  &nbsp;&nbsp;&nbsp;
                  <Button variant="primary" onClick={ saveWallet }>儲值</Button>
                </div>
              </div>
            </div>
            
            <div className="search-block mt-3">
              <div className="search-block-line-title mb-1">
                  <span className='' >儲值記錄</span>
              </div>
              <div className="search-block-line">
              <table border="1">
                <thead>
                  <tr>
                    <th width="150">儲值日期</th>
                    <th width="100">儲值金額</th>
                  </tr>
                </thead>
                <tbody>
                  {
                    wallet.map((w, index) =>
                    <tr key={ w.walletId }>
                      <td>{ w.createTime}</td>
                      <td>{ w.amount}</td>
                    </tr>
                    )
                  }
                </tbody>
              </table>      
              </div>
            </div>
          </div>
        </main>
      </div>
    </div>
  )
}

export default Wallet
