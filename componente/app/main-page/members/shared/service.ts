import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Member} from "./model";

@Injectable()
export class MemberService {

  constructor(private httpClient: HttpClient) {
  }

  getMembers(): Observable<Member[]> {
    return this.httpClient.get<Array<Member>>("http://localhost:8080/api/members");
  }

}
