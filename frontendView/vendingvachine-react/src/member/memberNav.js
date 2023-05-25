import React from 'react';
import { Link,  useLocation} from "react-router-dom";
import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';

const MemberNav = () => {
    //取得目前URL
    const location = useLocation()['pathname'] ;
    
    return (
        <Navbar bg="light" expand="lg">    
                <Nav className="mx-auto navBtn">
                    <Nav.Link className={ location === '/member' && 'memberNavColor'} as={Link} to="/member">會員資料</Nav.Link>
                    <Nav.Link className={ location === '/wallet' && 'memberNavColor'} as={Link} to="/wallet">儲值</Nav.Link>
                    <Nav.Link className={ location === '/shoppingList' && 'memberNavColor'} as={Link} to="/shoppingList">購買明細</Nav.Link>
                    <Nav.Link className={ location === '/editMember' && 'memberNavColor'} as={Link} to="/editMember">修改會員資料</Nav.Link>
                </Nav>
        </Navbar>
    );
   
}

export default MemberNav;