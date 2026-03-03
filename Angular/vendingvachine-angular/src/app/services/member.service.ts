import { Injectable } from '@angular/core';

import { Member } from '../models/member';

@Injectable({
  providedIn: 'root'
})
export class MemberService {

  private member: Member = new Member;

  constructor() {  }

  /*private getUser() {
    const user = localStorage.getItem("currentUser");
    if(user != null) {
      this.member = JSON.parse(user);
    } else {
      this.member = new Member;
    }
  }

  getMemberId() {
    this.getUser();
    return this.member.memberId;
  }

  checkLogin() {
    this.getUser();
    if (this.member.memberId === '') {
      return true;
    }
    return false;
  }*/

}
