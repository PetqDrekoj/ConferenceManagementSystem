import {Component, Input, OnInit} from '@angular/core';
import {RegisterService} from "./shared/service";
import {Observable} from "rxjs";
import {Message} from "../login-page/shared/model";
import {Router} from "@angular/router";

@Component({
  selector: 'app-register-page',
  templateUrl: './register-page.component.html',
  styleUrls: ['./register-page.component.css']
})
export class RegisterPageComponent implements OnInit {

  constructor(private serviceRegister: RegisterService, private router: Router) { }

  ngOnInit(): void {
  }

  @Input() error:string;

  register(username, password, verifyPassword, email, fullName, affiliation, userWebsite) : void{
    this.serviceRegister.register({username:username,password:password,verifyPassword:verifyPassword,email:email,
      fullName:fullName,affiliation:affiliation, userWebsite:userWebsite}).subscribe(t=>{
      if (t.entity != null){
        alert("Success");
        setTimeout(()=>{this.router.navigate(['']);},2000);
      }
      else{
        alert(t.error);
      }
    });
  }


  back() :void{
    this.router.navigate(['']);
}
mainpage():void{
  this.router.navigate(['']);
}

}
