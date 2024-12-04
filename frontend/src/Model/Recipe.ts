import {Status} from "./Status.ts";
import {RecipeIngredient} from "./RecipeIngredient.ts";

export type Recipe={
    id:string,
    name:string,
    description:string,
    time:number,
    imageUrl:string,
    preparation:string,
    status:Status,
    ingredients:RecipeIngredient[]
}