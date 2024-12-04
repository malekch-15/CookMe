import {Recipe} from "./Model/Recipe.ts";
import RecipeCard from "./Recipe/RecipeCard.tsx";
import "./App.css"

type WishListProps={
    recipe:Recipe[]
    onDelete:(id:string)=>void
    onToggleWishlist: (id: string) => void
}
export default function WishList(props:WishListProps){

    const favoriteRecipes = props.recipe.filter((r) => r.status === "FAVORITE");
    return(
        <div className="wishlist-card">
            { favoriteRecipes.map((r)=>(<RecipeCard key={r.id} Recipe={r} onDelete={props.onDelete} onToggleWishlist={props.onToggleWishlist}/>))}
                </div>
    )
}