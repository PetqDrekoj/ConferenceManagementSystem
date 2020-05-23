import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {createConferenceMessage, createConferenceModel, MyDate} from "./createConference.model";

@Injectable()
export class CreateConferenceService{

  constructor(private httpClient: HttpClient) {
  }

  add(conference_name :string,chair_username: string,co_chair_username:string ,starting_date: Date,ending_date: Date,abstract_deadline: Date,paper_deadline: Date,bidding_deadline : Date,review_deadline:Date): void{
    var c = new createConferenceModel();
    // month is between 0 and 11
    c.conference_name = conference_name;
    c.chair_username = chair_username;
    c.co_chair_username = co_chair_username;
    c.starting_date = new MyDate(starting_date.getDate(),starting_date.getMonth()+1,starting_date.getFullYear());
    c.ending_date = new MyDate(ending_date.getDate(),ending_date.getMonth()+1,ending_date.getFullYear());
    c.abstract_deadline = new MyDate(abstract_deadline.getDate(),abstract_deadline.getMonth()+1,abstract_deadline.getFullYear());
    c.paper_deadline = new MyDate(paper_deadline.getDate(),paper_deadline.getMonth()+1,paper_deadline.getFullYear());
    c.bidding_deadline = new MyDate(bidding_deadline.getDate(),bidding_deadline.getMonth()+1,bidding_deadline.getFullYear());
    c.review_deadline = new MyDate(review_deadline.getDate(),review_deadline.getMonth()+1,review_deadline.getFullYear());
    this.httpClient.post<createConferenceMessage>("http://localhost:8080/api/add_conference",c).subscribe(response=>
    {
      console.log("createConference: ", response.error);
      console.log("createConference: ", response.entity);
    });

  }

}
