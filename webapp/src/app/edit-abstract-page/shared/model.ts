export class CreateAbstractModel{
  username: string;
  conference_name:string;
  title:string;
  content:string;
  keywords:string;
  topics:string;
  additional_authors:string;
  url:string;
  id:number;
  constructor(username:string, conference_name:string, title:string,content:string,keywords:string, topics:string,additional_authors:string, url:string) {
    this.additional_authors = additional_authors;
    this.conference_name = conference_name;
    this.content = content;
    this.username = username;
    this.keywords = keywords;
    this.topics = topics;
    this.title = title;
    this.url = url;
  }
}
