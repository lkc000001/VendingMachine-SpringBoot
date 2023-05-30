import React, { useState } from 'react';
import axios from "axios";
import { useLocation, useNavigate } from "react-router-dom";


const LoginPage = (props) => {

    //取得當前頁面URL
    const location = useLocation()['pathname'] ;
    const navigate = useNavigate();

    const [inputMemberId, setInputMemberId] = useState('');
    const [inputPassword, setInputPassword] = useState('');
    const [showErrorMsg, setShowErrorMsg] = useState('');

    const handleSubmit = async (event) => {
        event.preventDefault();
        //取得form表單資料
        const data = new FormData(event.target);
        const formValues = Object.fromEntries(data.entries());
        
        //登入驗証
        const LoginResp = await axios.post("http://localhost:8086/VendingMachine/frontend/memberLogin", formValues, { withCredentials: true })
                                                .then(rs => rs.data)
                                                .catch(error => { console.log(error); });
        if(LoginResp.state !== 200) {
            showMessage(LoginResp.data);
            return null;
        }

        if(location === '/') {
            props.handleLoginPage();
            return null;
        }
        navigate("/");
    }
    
    const clear = () => {
        setInputMemberId('');
        setInputPassword('');
        setShowErrorMsg('');
    }

    const showMessage = (msg) => {
        setShowErrorMsg(msg);
    }

    return (
        <div className="whitebg">
            <div className="logo">
                <img src="" alt=""/>
            </div>
            <div className="s-title">
                <span className="s-txt">登入系統</span>
            </div>
            <form onSubmit={ handleSubmit } >
                <div id="inputdiv">
                    <div className="inputblock">
                        <label>使用者名稱：</label>
                        <input type="text" name="memberId" value={ inputMemberId } onChange={(e) => setInputMemberId(e.target.value)}/>
                    </div>
                    <div className="inputblock">
                        <label>使用者密碼：</label>
                        <input type="password" name="memberPassword" value={ inputPassword } onChange={(e) => setInputPassword(e.target.value)} />
                    </div>
                </div>
                <span className="error" >
                    { showErrorMsg == '' ? <br /> :  showErrorMsg}
                </span>
                <div className="loginbtn">
                    <button type='button' className="btn lg newbtn-gray" onClick={ clear }>清除</button>
                    &nbsp;
                    <button type="submit" className="btn lg newbtn-yellow">登入</button>
                </div>
            </form>
        </div>
    );

}

export default LoginPage;