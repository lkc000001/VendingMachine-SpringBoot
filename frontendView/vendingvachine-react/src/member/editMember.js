import React, { useState, useRef }  from 'react';
import Header from '../util/header';
import '../CSS/member.css';
import axios from "axios";
import MemberDetails from './memberDetails';
import MemberNav from './memberNav';

const updataMemberUrl = "http://localhost:8086/VendingMachine/member/updataMember";

const EditMember = () => {


    const [ memberDetails , setMemberDetails ] = useState(null);
    
    const getMember = (loginMemberDetails) => {
        setMemberDetails(loginMemberDetails);
    }

    const headerRef = useRef(null);

    const showMessage = (title, message) => {
        headerRef.current.showMessage(title, message);
    }

    const handleSubmit = async (event) => {
        event.preventDefault();
        
        //取得form表單資料
        const data = new FormData(event.target);
        const formValues = Object.fromEntries(data.entries());
        //儲存會員資料
        const memberResp = await axios.put(updataMemberUrl, formValues, { withCredentials: true })
                                                .then(rs => rs.data)
                                                .catch(error => { console.log(error); });
        
        if(memberResp.state === 200) {
            showMessage("", memberResp.data);
            return null;
        }
    }

    return (
        <div className="main-body">
            <div className="content">
                 <Header getMember = { getMember } ref={ headerRef } />
                <main >
                    <div>
                        <MemberNav />
                        <form onSubmit={ handleSubmit } >
                            <MemberDetails memberDetails = { memberDetails } />
                        </form>
                    </div>
                </main>
            </div>
        </div>
    );
    
}

export default EditMember;