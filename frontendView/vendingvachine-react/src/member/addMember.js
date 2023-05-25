import React, { useState, useRef  }  from 'react';
import axios from "axios";
import Header from '../util/header';
import '../CSS/member.css';
import MemberDetails from './memberDetails';

const addMemberUrl = "http://localhost:8086/VendingMachine/member/addMember";

const AddMember = () => {
    //bootstrap modal設定
    const [{ modalTitle, modalMessage, show}, setShowMsg] = useState( {
        modalTitle: '',
        modalMessage: '',
        show: false
    });

    const [ memberDetails , setMemberDetails ] = useState({ memberId: '' });
    
    const headerRef = useRef(null);

    const showMessage = (title, message) => {
        headerRef.current.showMessage(title, message);
    }

    const handleSubmit = async (event) => {
        event.preventDefault();

        //取得form表單資料
        const data = new FormData(event.target);
        const formValues = Object.fromEntries(data.entries());
        //檢查欄位
        if(checkField(formValues)) {
            return null;
        }

        //儲存會員資料
        const memberResp = await axios.post(addMemberUrl, formValues, { withCredentials: true })
                                                .then(rs => rs.data)
                                                .catch(error => { console.log(error); });
        if(memberResp.state == 4001) {
            showMessage("新增帳號失敗",memberResp.data);
            return null;
        }
        showMessage("",memberResp.data);
    }
    
    const checkField = (value) => {
        if(value.memberId === '') {
            showMessage("欄位錯誤", '身分證不能為空白');
            return true;
        }
        if(value.memberPassword === '') {
            showMessage("欄位錯誤", '密碼不能為空白');
            return true;
        }
        if(value.memberPassword.length <= 5) {
            showMessage("欄位錯誤", '密碼不能小於5位數');
            return true;
        }
        if(value.memberName === '') {
            showMessage("欄位錯誤", '姓名不能為空白');
            return true;
        }
        return false;
    }

    return (
        <div className="main-body">
            <div className="content">
                 <Header ref={ headerRef } />
                <div >
                    <form onSubmit={ handleSubmit } >
                        <MemberDetails memberDetails = { memberDetails } />
                    </form>
                </div>
            </div>
        </div>
    );
    
}

export default AddMember;