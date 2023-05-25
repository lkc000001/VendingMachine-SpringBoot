import React, { Component, useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { Button } from 'react-bootstrap';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'

const MemberDetails = ({ memberDetails }) => {
//class memberDetails extends Component {
    const location = useLocation()['pathname'] ;
    //const [ memberDetails , setMemberDetails ] = useState();

    const [memberId, setMemberId] = useState('');
    const [memberName, setMemberName] = useState('');
    const [memberAddress, setMemberAddress] = useState('');
    const [memberBirthday, setMemberBirthday] = useState('');
    const [memberPhone, setMemberPhone] = useState('');

    useEffect(() => {
        if(location === '/member' || location === '/editMember') {
            if( memberDetails != null) {
                setMemberId(memberDetails.memberId);
                setMemberName(memberDetails.memberName);
                setMemberAddress(memberDetails.memberAddress);
                setMemberBirthday(memberDetails.memberBirthday);
                setMemberPhone(memberDetails.memberPhone);
            }
        }   
    }, [ memberDetails ]);

    //const { memberId } = props.details;
    /*state = {
        memberId: '',
        memberName: '',
        memberAddress: '',
        memberBirthday: '',
        memberPhone: ''
    };
   
    componentDidUpdate( prevProps, prevState ) {
        const { memberId, memberName, memberAddress, memberBirthday, memberPhone } = prevProps.memberDetails;
        if( memberId != prevState.memberId ) {
            this.setState({
                memberId: memberId,
                memberName: memberName,
                memberAddress: memberAddress,
                memberBirthday: memberBirthday,
                memberPhone: memberPhone
            });
        }
        
    }*/

    //render() {
    
        return (
            <div className="search-block mt-3">
                <Container fluid="sm">
                {
                    location === '/addMember' &&
                    <Row>
                        <Col className="search-block-line-title">註冊會員</Col>
                    </Row>
                }
                    <Row>
                        <Col sm="6" className="search-block-line-title memberTextR" >會員編號：</Col>
                        <Col sm="6" className="search-block-line">
                            <input type="text" name="memberId" 
                                            value={ memberId } 
                                            onChange={(e) => setMemberId( e.target.value )} 
                                            readOnly={ location === '/member' ? true : false } />

                        </Col>
                    </Row>
                {
                    location === '/addMember' && 
                    <Row>
                        <Col sm="6" className="search-block-line-title memberTextR" >密　　碼：</Col>
                        <Col sm="6" className="search-block-line">
                            <input type="password" name="memberPassword" />
                        </Col>
                    </Row>
                }
                    <Row>
                        <Col sm="6" className="search-block-line-title memberTextR" >姓　　名：</Col>
                        <Col sm="6" className="search-block-line">
                            <input type="text" name="memberName" 
                                            value={ memberName !== null ?  memberName : '' } 
                                            onChange={(e) => setMemberName( e.target.value )} 
                                            readOnly={ location === '/member' ? true : false } />

                        </Col>
                    </Row>
                    <Row>
                        <Col sm="6" className="search-block-line-title memberTextR" >住　　址：</Col>
                        <Col sm="6" className="search-block-line">
                            <input type="text" name="memberAddress" 
                                            value={ memberAddress !== null ? memberAddress : '' } 
                                            onChange={(e) => setMemberAddress( e.target.value )} 
                                            readOnly={ location === '/member' ? true : false } />

                        </Col>
                    </Row>
                    <Row>
                        <Col sm="6" className="search-block-line-title memberTextR" >生　　日：</Col>
                        <Col sm="6" className="search-block-line">
                            <input type="date" name="memberBirthday" 
                                    value={ memberBirthday !== null ? memberBirthday : '' } 
                                    onChange={(e) => setMemberBirthday( e.target.value )} 
                                    readOnly={ location === '/member' ? true : false } 
                                    pattern="\d{4}-\d{2}-\d{2}" />

                        </Col>
                    </Row>
                    <Row>
                        <Col sm="6" className="search-block-line-title memberTextR" >電　　話：</Col>
                        <Col sm="6" className="search-block-line">
                            <input type="text" name="memberPhone" 
                                    value={ memberPhone !== null ? memberPhone : '' } 
                                    onChange={(e) => setMemberPhone( e.target.value )} 
                                    readOnly={ location === '/member' ? true : false } />

                        </Col>
                    </Row>
                </Container>
                {
                    location === '/addMember' && 
                    <div className="search-block-line justifyContentCenter">
                        <Button variant="primary" type="submit" >註冊</Button>
                    </div>
                }
                {
                    location === '/editMember' && 
                    <div className="search-block-line justifyContentCenter">
                        <Button variant="primary" type="submit" >修改</Button>
                    </div>
                }
            </div>
        );
    //};
}

export default MemberDetails;