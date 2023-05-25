import { createContext, useReducer } from 'react';
import UserContext from './userContext';

const initialState = {
    type: 'init',
    user: {memberId:'', memberName: "遊客"},
    shoppingCartCount: 3
};
  
  // 定義reducer函數
const reducer = (state, action) => {
switch (action.type) {
    case "init":
        return initialState;
    case "SetUser":
        return { ...state, user: action.user };
    default:
        return state;
}
};

// createContext並給予初始值
const context = createContext({
    type: 'init',
    user: {memberId:'', memberName: "遊客"}
});

export const UserProvider = ({ children }) => {
    const [state, dispatch] = useReducer(reducer, initialState);
    return (
        <UserContext.Provider value={[state, dispatch]}>
            {children}
        </UserContext.Provider>
    );
};

export default context;