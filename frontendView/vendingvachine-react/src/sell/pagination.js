import React, { Component } from 'react';
import 'bootstrap/dist/css/bootstrap.css';
import '../CSS/sell.css';

class pagination extends Component {

    oldPage = 1;

    pageOnClick = (e) => {
        let page = e.target.innerText;
        switch (page) {
            case "<":
                page = parseInt(this.oldPage) - 1;
                break;
            case ">":
                page = parseInt(this.oldPage) + 1;
                break;
            case "<<":
                page = 1;
                break;
            case ">>":
                page = this.props.maxPage.length;
                break;
        }
        this.oldPage = page;
        this.props.queryProduct(page);
        console.log(page);

    }

    render() {
        return (
            <nav aria-label="Page navigation example">
                <ul className="pagination">
                    <div className={ this.oldPage == 1 ? "beforePageDisplay" : "" }>
                        <li className="page-item cursorPointer displayInline">
                            <a className="page-link">
                                <span onClick={ this.pageOnClick }>{'<<'}</span>
                            </a>
                        </li>
                        <li className="page-item cursorPointer displayInline">
                            <a className="page-link">
                                <span onClick={ this.pageOnClick }>{'<'}</span>
                            </a>
                        </li>
                    </div>
                    { 
                        this.props.maxPage.map( ( page, index) => 
                            index == (this.props.pageIndex - 1) ? <li key={ index } className="page-item active"><a className="page-link ">{ index + 1 }</a></li>
                                : <li key={ index } className="page-item cursorPointer"><a className="page-link" onClick={ this.pageOnClick }>{ index + 1 }</a></li>
                        )
                    }
                    <div className={ this.oldPage == this.props.maxPage.length ? "afterPageDisplay" : "" }>
                        <li className="page-item displayInline">
                        <a className="page-link" href="#" aria-label="Next">
                            <span onClick={ this.pageOnClick }>{'>'}</span>
                        </a>
                        </li>
                        <li className="page-item displayInline">
                        <a className="page-link" href="#" aria-label="Next">
                            <span onClick={ this.pageOnClick }>{'>>'}</span>
                        </a>
                        </li>
                    </div>
                </ul>
            </nav>
        );
    }
}

export default pagination;