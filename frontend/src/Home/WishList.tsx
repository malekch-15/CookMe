import {Recipe} from "../Model/Recipe.ts";
import RecipeCard from "../Recipe/RecipeCard.tsx";
import "../App.css"
import {AppUser} from "../Model/AppUser.ts";

type WishListProps={
    recipe:Recipe[]
    onDelete:(id:string)=>void
    onToggleWishlist: (id: string) => void
    user:AppUser|undefined
}
export default function WishList(props:WishListProps){
if(props.user===undefined){
    console.log(props.user)
    return <h2>Please log in to view your favorites.</h2>;
}


    const favoriteRecipes = props.user.favorites

    return(
            <div className="wishlist-card">
                {favoriteRecipes.length === 0 ? (
                    <p>No favorite recipes found.</p>
                ) : (
                    favoriteRecipes.map((r) => (
                        <RecipeCard
                            key={r.id}
                            Recipe={r}
                            onDelete={props.onDelete}
                            onToggleWishlist={props.onToggleWishlist}
                        />
                    ))
                )}
            </div>
        );

}