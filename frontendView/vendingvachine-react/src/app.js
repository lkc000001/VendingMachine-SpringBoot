import React, { useState, useContext } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Sell from './sell/sell';
import Login from './login/login';
import EditMember from './member/editMember';
import Member from './member/member';
import Wallet from './member/wallet';
import ShoppingList from './member/shoppingList';
import UserContext, { context } from './context/userContext';
import AddMember from './member/addMember';

function App() {

  /*const [state, dispatch] = useState({
                                        type: 'init',
                                        user: {memberId:'', memberName: "遊客"},
                                        shoppingCartCount: 5
                                    });     */         
    return (
      //<UserContext.Provider value={[state, dispatch]}>
        <BrowserRouter>
          <Routes>
              <Route path="/" element={<Sell />} />
              <Route path="/login" element={<Login />} />
              <Route path="/addMember" element={<AddMember />} />
              <Route path="/editMember" element={<EditMember />} />
              <Route path="/member" element={<Member />} />
              <Route path="/wallet" element={<Wallet />} />
              <Route path="/shoppingList" element={<ShoppingList />} />
          </Routes>
        </BrowserRouter>
      //</UserContext.Provider>
    );
}

export default App;