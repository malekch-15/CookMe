import {Recipe} from "../Model/Recipe.ts";
import RecipeCard from "../Recipe/RecipeCard.tsx";
import {AppUser} from "../Model/AppUser.ts";
import "../css/Recipe.css"
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

    return (
        <>
        <div className="wishList-title">
            <h2>Your Top Picks </h2>
            Among the countless recipes on CookMe, these are the true standouts <br/> the ones you keep coming back to,
            time after time.
        </div>
    <div className="card-gallery">
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
        </>
        );

}