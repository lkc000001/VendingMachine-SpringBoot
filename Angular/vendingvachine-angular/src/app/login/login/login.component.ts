import { Component } from '@angular/core';
import { Member } from 'src/app/models/member';
import { LoginService } from 'src/app/services/login.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  error: String = '';
  memberId: string='';
  memberPassword: string='';
  member: Member = new Member;

  constructor(
    private loginService: LoginService,
    public activeModal: NgbActiveModal
  ){}


  login() {
    const memberId = this.member.memberId;
    const password = this.member.memberPassword;
    if(memberId === '' ) {
      this.error = "帳號不能為空白";
      return ;
    }
    if(password === '' ) {
      this.error = "密碼不能為空白";
      return ;
    }

    this.loginService.login(this.member).subscribe((data) => {
      if(data.state !== 200) {
        this.error = data.data;
      } else {
        this.activeModal.close();
      }
    });
  }
}
