import React, { useState, useEffect, Fragment } from 'react'
import axios from "axios";
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import FormControl from 'react-bootstrap/FormControl';
import Container from 'react-bootstrap/Container';
import Col from 'react-bootstrap/Col'
import Row from 'react-bootstrap/Row';

const getProductClassifyUrl  = 'http://localhost:8086/VendingMachine/frontend/product/getProductClassify';

const SearchBar = (props) => {

    const { searchString, setSearchString, searchProduct, searchClassify, setSearchClassify, onChangeClassify} = props.searchBarParameter;

    const [classifyList, setClassifyList] = useState([]);

    useEffect(
        () => {
            if(classifyList.length <=0) {
                classifyListresp();
            }
        },[]
    );

    //依分清單資料
    const classifyListresp = async () => {
        const { data } = await axios.get(getProductClassifyUrl, {timeout: 3000})
        .then(rs => rs)
        .catch(error => { console.log(error); });
        
        setClassifyList(data.data);
    }

    const changeClassify = (e) => {
        setSearchClassify( e.target.value );  
        onChangeClassify();
    }

    const changeSearchProduct = () => {
        setSearchClassify('');  
        searchProduct();
    }

    const clserSearchProduct = () => {
        setSearchClassify('');  
        setSearchString('');
        searchProduct();
    }

    return (
        <Container fluid="sm" className='mb-3'>
            <Row>
                <Col>
                    <Row>
                        <Col sm="3" className="search-block-line-title memberTextR" >分類：</Col>
                        <Col sm="6">
                            <Form.Control as="select" value={ searchClassify } onChange={ changeClassify } >
                                { classifyList.map( ( cl, index) =>
                                    <Fragment key={ index } >
                                    {
                                        index === 0 ? <option value=''>全部</option> :
                                                    <option value= { cl }>{ cl }</option>
                                    }
                                    </Fragment>
                                )}
                            </Form.Control>
                        </Col>
                        <Col sm="3"></Col>
                    </Row>
                </Col>
                <Col>
                    <Row>
                        <Col sm="3" className="search-block-line-title memberTextR">搜尋：</Col>
                        <Col sm="5">
                            <FormControl type="text" className="mr-sm-3" value={ searchString } onChange={ (e) => setSearchString( e.target.value ) }/>
                        </Col>
                        <Col sm="4">
                            <Button variant="success" onClick={ changeSearchProduct }>搜尋</Button>
                            <Button className='ml-2' variant="secondary"  onClick={ clserSearchProduct }>清除</Button>
                        </Col>
                    </Row>
                </Col>
            </Row> 
        </Container>
    )
}

export default SearchBar

