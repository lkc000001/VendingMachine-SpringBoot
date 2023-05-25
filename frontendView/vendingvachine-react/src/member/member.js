import React, { useState } from 'react';
import Header from '../util/header';
import MmberDetails from './memberDetails';
import MemberNav from './memberNav';

const Member = () => {

    const [ memberDetails , setMemberDetails ] = useState(null);

    const getMember = (loginMemberDetails) => {
        setMemberDetails(loginMemberDetails);
    }

    return (
        <div className="main-body">
            <div className="content">
                <Header getMember = { getMember } />
                <main >
                    <div>
                        <MemberNav />
                        <MmberDetails memberDetails = { memberDetails } />
                    </div>
                </main>
            </div>
        </div>
    );

}

export default Member;