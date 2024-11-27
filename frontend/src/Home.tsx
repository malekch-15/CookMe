import {Recipe} from "./Model/Recipe.ts";
import RecipeCard from "./Recipe/RecipeCard.tsx";
import "./Home.css"

type HomeProps={
    recipe:Recipe[]
    onDelete:(id:string)=>void
}
export default function Home(props:HomeProps){


    return(
       <div className="gallery-container">
           {props.recipe.map((r)=>(<RecipeCard key={r.id} Recipe={r}
                                               onDelete={props.onDelete}  showDeleteButton={true}/>))}
       </div>
    )
}