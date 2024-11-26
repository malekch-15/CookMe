import {Recipe} from "./Model/Recipe.ts";
import RecipeCard from "./Recipe/RecipeCard.tsx";

type HomeProps={
    recipe:Recipe[]
}
export default function Home(props:HomeProps){
    return(
       <div>
           {props.recipe.map((r)=>(<RecipeCard key={r.id} Recipe={r}/>))}
       </div>
    )
}