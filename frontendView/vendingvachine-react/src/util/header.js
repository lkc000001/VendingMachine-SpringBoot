import React, { useState, useEffect, useImperativeHandle, forwardRef } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import axios from "axios";
import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';
import Login from '../login/LoginPage';
import ShowMessage from '../util/showMessage';
import ShoppingCart from '../member/shoppingCart';

const Header = forwardRef((props, cRef) => {
    //取得當前頁面URL
    const location = useLocation()['pathname'] ;
    const navigate = useNavigate();

    //給父類呼叫顯示訊息頁面
    useImperativeHandle(cRef, () => ({
        showMessage, changeCountVer
    }));

    const [ { memberDetails, isLogin }, setMember ] = useState({
        memberDetails: {
            memberId: '',
            memberName: "遊客"
        },
        isLogin: false,
    });
    
    const [ { shoppingCart, shoppingCartCount }, setShoppingCart ] = useState({
        shoppingCart: [],
        shoppingCartCount: 0
    });
    
    const [ countVer, setCountVer ] = useState(0);

    const { memberId,  memberName} = memberDetails;

    const [{ modalTitle, modalMessage, show}, setShowMsg ] = useState( {
        modalTitle: '',
        modalMessage: '',
        show: false
    });

    //進頁面檢查是否已登入
    useEffect(() => {
        checkLogin();
    }, [ isLogin, countVer ]);

    //顯示login頁面
    const showLogin = () => {
        setShowMsg({
            modalMessage: <Login handleLoginPage = { handleLoginPage } />,
            show: true
        });
    }

    //關閉login頁面,設定已登入
    const handleLoginPage = () => {
        setShowMsg({ show: false });
        setMember(member => ({ ...member, isLogin:  true }));
    }

    //顯示訊息頁面
    const showMessage = (title, message) => {
        setShowMsg( {
            modalTitle: title,
            modalMessage: message,
            show: true
        });
    }

    //關閉訊息頁面
    const handleShow = () => setShowMsg({ show: false });
    
    //設定是否可以結帳
    const [ isCheckout, setIsCheckout ] = useState(false);
    const setCheckout = (value) => {
        setIsCheckout(value);
    }

    //顯示購物車頁面
    const showShoppingList = () => {
        setShowMsg({
            modalTitle: '購物車',
            modalMessage: <ShoppingCart shoppingCart = { shoppingCart } 
                                        handleShow = { handleShoppingList } 
                                        setCheckout = { setCheckout } 
                                        changeShoppingCart = { changeShoppingCart } 
                                        changeCountVer = { changeCountVer } 
                                        handleShoppingList = { handleShoppingList } />,
            show: true
        });
    }

    //關閉購物車頁面
    const handleShoppingList = () => {
        setShowMsg({ show: false });
    }
    
    //結帳
    const addMemberOrder = async () => {
        const addMemberOrderResp = await axios.post("http://localhost:8086/VendingMachine/memberorder/addMemberOrder", shoppingCart, { withCredentials: true })
        .then(rs => rs.data)
        .catch(error => { console.log(error); });

        window.location.reload();

         window.addEventListener("load", () => {
            setTimeout(() => {
                setShowMsg({
                    modalTitle: "結帳",
                    modalMessage: "結帳成功",
                    show: true,
                });
            }, 50);
        });
    }
    
    //登入驗証
    const checkLogin = async () => {
        const checkLoginResp = await axios.get("http://localhost:8086/VendingMachine/frontend/checkLogin", { withCredentials: true })
        .then(rs => rs.data)
        .catch(error => { console.log(error); });

        if(checkLoginResp.state === 200) {
           
            setMember(member => ({
                ...member,
                memberDetails: checkLoginResp.data,
                isLogin: true
            }));

            changeShoppingCart(checkLoginResp.data.shoppingCart);

            const urlList = ['/', '/member', '/shoppingList', "/wallet", '/editMember'];
            if (urlList.includes(location)) {
                props.getMember(checkLoginResp.data);
            }
        } else {
            if (location != "/addMember") {
                navigate("/");
            }
        }
    }
    //登出
    const logout = async () => {
        const checkLoginResp = await axios.get("http://localhost:8086/VendingMachine/frontend/logout", { withCredentials: true })
        .then(rs => rs.data)
        .catch(error => { console.log(error); });

        setMember({
            memberDetails: {
                memberId: '',
                memberName: "遊客"
            },
            isLogin: false
        });

        setShoppingCart({ 
            shoppingCart: [],
            shoppingCartCount: 0,
        });

        navigate("/");
    }
    //更新購物車
    const changeShoppingCart = (newCart) => {
        const shoppingCartCountLenth = newCart == null ? 0 : newCart.length;
        setShoppingCart(sc => ({ 
            shoppingCart: newCart,
            shoppingCartCount: shoppingCartCountLenth,
        }));
    }

    const changeCountVer = () => {
        setCountVer(countVer + 1);
    }

    //傳至showMessage參數
    const showMessageParameter = {
        show,
        modalTitle,
        modalMessage,
        handleShow,
        addMemberOrder,
        isCheckout,
        changeShoppingCart,
        changeCountVer
    }

    return (
        <Navbar bg="dark" variant={"dark"} expand="lg">
            <Nav className="mr-auto">
                <Nav.Link active className='customLink'>會員ID:{ memberId }</Nav.Link>
                <Nav.Link active className='customLink'>會員名稱: { memberName }</Nav.Link>
            </Nav>
            <Navbar.Brand className="mx-auto order-0">生活用品</Navbar.Brand>
            <Nav className="ml-auto">
                <Nav.Link as={Link} to="" active={ shoppingCartCount > 0 ? true : false } 
                                    className={ memberId === '' && 'customLink' } 
                                    onClick={ shoppingCartCount > 0  && showShoppingList } 
                                    >購物車({ shoppingCartCount })</Nav.Link>
                <Nav.Link as={Link} to="/" active>首頁</Nav.Link>
                {
                    memberId === '' ? 
                    <Nav.Link className='customLink'>會員中心</Nav.Link> :
                    <Nav.Link as={Link} to="/member" active>會員中心</Nav.Link>
                }
                { memberId === '' && <Nav.Link as={Link} to="/addMember" active>註冊</Nav.Link>}
                {
                    memberId === '' ? <Nav.Link onClick={ memberId === '' && showLogin } active>登入</Nav.Link> : 
                                     <Nav.Link onClick={ logout } active>登出</Nav.Link>
                }
            </Nav>
            <ShowMessage showMessageParameter = { showMessageParameter } />
        </Navbar>
        
    );
})

export default Header;