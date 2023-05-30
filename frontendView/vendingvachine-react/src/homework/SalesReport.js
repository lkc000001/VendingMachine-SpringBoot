import React, { Component, createRef } from 'react';
import axios from "axios";
import '../CSS/homework.css';

const apiUrl = 'http://localhost:8086/VendingMachine/product/queryProduct';

class SalesReport extends Component {

    constructor(props){
        super(props);
        this.state = {
            pageIndex: 1,
            maxPage: [],
            products: []
        }; 
    }

    componentDidMount() {
        this.onClickSearch();
    }

    componentDidUpdate( prevProps, prevState ) {
        if(prevState.pageIndex !== this.state.pageIndex){
            this.onClickSearch();
        }
    }

    startDate = createRef();
    endDate = createRef();
    oldPage = 1; 

    onClickSearch = async () => {
        const startDate = this.startDate.current.value;
        const endDate = this.endDate.current.value;

        const requestData = {
            pageIndex: this.state.pageIndex,
            pageSize: 2,
            createTimeStart: startDate,
            createTimeEnd: endDate
        };
        const reportData = await axios.post(apiUrl,  requestData )
            .then(rs => rs.data)
            .catch(error => { console.log(error); });
        
        this.setState({
            products: reportData.data,
            maxPage: Array.from({ length: reportData.maxPage }),
        });
    };

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
                page = this.state.maxPage.length;
                break;
        }
        this.oldPage = page;
        this.setState({
            pageIndex: page
        });
    }


    render() {
        const { products, maxPage } = this.state;
        const maxPageLenth = maxPage.length;
        return (
            <div>
                <label>查詢日期起：</label> <input type='date' ref={ this.startDate } />
                <label style={{ marginLeft: '20px' }} />
                <label>查詢日期迄：</label> <input type='date' ref={ this.endDate } />
                <label style={{ marginLeft: '20px' }} />
                <button onClick={this.onClickSearch}>查詢</button>
                <hr />
                <table border={'2'}>
                    <thead>
                        <tr>
                            <th>商品編號</th>
                            <th>商品名稱</th>
                            <th>商品價格</th>
                            <th>商品成本</th>
                            <th>商品庫存</th>
                            <th>建立日期</th>
                        </tr>
                    </thead>
                    <tbody>
                        { products.map( ( product, index) =>
                            <tr key={ index }>
                            <td>{ product.productId }</td>
                            <td>{ product.productName }</td>
                            <td>{ product.price }</td>
                            <td>{ product.cost }</td>
                            <td>{ product.stock }</td>
                            <td>{ product.createTime }</td>
                        </tr>
                        )}
                    </tbody>
                </table>
                <hr/>                
                <div>   
                    <button onClick={ this.pageOnClick }  disabled={ this.oldPage == 1  ? true : false } >{'<<'}</button>
                    <button onClick={ this.pageOnClick }  disabled={ this.oldPage == 1  ? true : false } >{'<'}</button>
                    { maxPage.map( ( num, index) =>
                        <button onClick={ this.pageOnClick } key={ index } className={ this.oldPage == (index + 1)  && 'buttonActive'}  >{ index + 1 }</button>
                    )}
                    <button onClick={ this.pageOnClick }  disabled={ this.oldPage == maxPageLenth ? true : false } >{'>'}</button>
                    <button onClick={ this.pageOnClick }  disabled={ this.oldPage == maxPageLenth ? true : false } >{'>>'}</button>
                </div>
            </div>
        );
    }
}

export default SalesReport;