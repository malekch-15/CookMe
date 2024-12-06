import {Status} from "./Status.ts";

export type Recipe={
    id:string,
    name:string,
    description:string,
    time:number,
    imageUrl:string,
    preparation:string,
    status:Status,
    ingredients:string[]
}