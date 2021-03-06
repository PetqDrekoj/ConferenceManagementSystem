export class MyDate {
  day: number;
  month: number;
  year: number;
}

export class ChangeDeadlineModel {
  conference: string;
  abstract_deadline: MyDate;
  paper_deadline: MyDate;
  bidding_deadline: MyDate;
  review_deadline: MyDate;
  ending_date: MyDate;
  reEval_date: MyDate;
  submissionDate: MyDate;

}


export class ChangeDeadlineMessage{
  entity: ChangeDeadlineModel;
  error: string;
}
