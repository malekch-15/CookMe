import {BaseIngredient} from "./BaseIngredient.ts";
import {Recipe} from "./Recipe.ts";

export type AppUser={
    id:string
    username:string
    avatarUrl:string,
    ingredient:BaseIngredient[],
    favorites:Recipe[]
}