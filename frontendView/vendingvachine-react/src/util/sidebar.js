import React, { Component } from 'react';
import '../CSS/sell.css';

class sidebar extends Component {
    render() {
        return (
            <div className="sidebar">
                <div > 
                    <a className="btn btn-success" onClick={ this.login } >登入</a>
                </div>
                
                <ul>
                    <li>項目1</li>
                    <li>項目2</li>
                    <li>項目3</li>
                </ul>
            </div>
        );
    }
}

export default sidebar;