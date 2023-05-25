import React,  { useState, Fragment }  from 'react';
import Pagination from 'react-bootstrap/Pagination';
import 'bootstrap/dist/css/bootstrap.css';
import '../CSS/sell.css';


const PaginationUtil = ({ query, pageIndex, maxPage}) => {

    const [oldPage, setOldPage] = useState(1);

    const pageOnClick = (e) => {
        let page = e.target.innerText;
        
        switch (page.substring(0,1)) {
            case "‹":
                page = parseInt(oldPage) - 1;
                break;
            case "›":
                page = parseInt(oldPage) + 1;
                break;
            case "«":
                page = 1;
                break;
            case "»":
                page = maxPage.length;
                break;
            default:
                page = parseInt(page);
        }
        setOldPage(page)
        query(page);

    }
        return (
                <Pagination>
                    <Pagination.First onClick={ pageOnClick } disabled={ pageIndex === 1 ? true : false }/>
                    <Pagination.Prev onClick={ pageOnClick } disabled={ pageIndex === 1 ? true : false }/>
                    { maxPage.map( ( page, index) => 
                        <Fragment key={ index } >
                            {
                                pageIndex - 1  === index ? <Pagination.Item className='customLink' active>{ index + 1}</Pagination.Item> :
                                                    <Pagination.Item onClick={ pageOnClick }>{ index + 1}</Pagination.Item>
                            }
                        </Fragment>
                    )}
                    <Pagination.Next onClick={ pageOnClick } disabled={ pageIndex === maxPage.length ? true : false }/>
                    <Pagination.Last onClick={ pageOnClick } disabled={ pageIndex === maxPage.length ? true : false }/>
                </Pagination>
        );

}

export default PaginationUtil;