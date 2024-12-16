
import {Recipe} from "./Recipe.ts";
import {RecipeIngredient} from "./RecipeIngredient.ts";

export type AppUser={
    id:string
    username:string
    avatarUrl:string,
    ingredient:RecipeIngredient[],
    favorites:Recipe[]
}