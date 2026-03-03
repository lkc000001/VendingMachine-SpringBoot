import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Subject, Observable, lastValueFrom } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { environment } from '../environments/environment';
import { Member } from '../models/member';

let API_URL = environment.backendUrl;

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private member: Member = new Member;
  private currentMemberSubject: BehaviorSubject<Member>;
  public currentMember: Observable<Member>;
  private isLogin: boolean = false;
  //private memberData = localStorage.getItem('currentUser');

  constructor(
    private http: HttpClient
  )
  {
    //const userData = localStorage.getItem('currentUser');
    //this.member = new Member;
    this.getMember();
    if (this.member.memberId === '') {
      this.currentMemberSubject = new BehaviorSubject<Member>(new Member());
    } else {
      //this.member = JSON.parse(this.membetJson);
      this.currentMemberSubject = new BehaviorSubject<Member>(this.member);
    }
    this.currentMember = this.currentMemberSubject.asObservable();
  }

  CORSoption = {
    withCredentials: true
  };

  login(data: any) {
    return this.http.post<any>(`${API_URL}/frontend/memberLogin`, data, this.CORSoption).pipe(map(data => {
      if(data.state === 200) {
        sessionStorage.setItem('currentUser', JSON.stringify(data.data));
        this.currentMemberSubject.next(data.data);
      }
      return data;
    }));
  }

  async checkLogin() {
    const result$ = this.http.get<any>(`${API_URL}/frontend/checkLogin`, this.CORSoption).pipe(
    map((resp) => {
        return resp;
    }));
    return await lastValueFrom(result$);
  }


  logout() {
    sessionStorage.clear();
    this.currentMemberSubject.next(new Member);
    this.member = new Member;
  }

  getMemberId() {
    this.getMember();
    return this.member.memberId;
  }

  getMember() {
    this.checkLogin();
    const memberData = sessionStorage.getItem("currentUser");
    console.log("memberData:", memberData)
    if(memberData != null) {
      this.member = JSON.parse(memberData);
    } else {
      this.member = new Member;
    }
    return this.member;
  }

  async checkIsLogin() {
    const res = await this.checkLogin();
    if(res.state === 200) {
      return true;
    } else {
      this.member = new Member;
      return false;
    }
  }
}
