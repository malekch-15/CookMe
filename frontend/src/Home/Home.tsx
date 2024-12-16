import {Recipe} from "../Model/Recipe.ts";
import RecipeCard from "../Recipe/RecipeCard.tsx";
import {useState} from "react";
import Searchbar from "./Searchbar.tsx";
import {AppUser} from "../Model/AppUser.ts";


type HomeProps={
    recipe:Recipe[]
    onDelete:(id:string)=>void
    onToggleWishlist: (id: string) => void
    user:AppUser|undefined
}
export default function Home(props:Readonly<HomeProps>){
    const [searchQuery, setSearchQuery] = useState("");
    const filteredRecipes = props.recipe.filter((recipe) =>
        recipe.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
        recipe.ingredients.some((ingredient) =>
            ingredient.ingredient.name.toLowerCase().includes(searchQuery.toLowerCase())
        )
    );

    const handleSearch = (query: string) => {
        setSearchQuery(query);
    };

    return(
        <>

            <div className="home-title">
                <h1>Recipes</h1>
                <p className="home-description"> CookMe is your source for thousands of delicious,<br/> easy and quick recipes, healthy meals,
                    dinner ideas, recipes for kids, holiday menus, and more</p>
            </div>
            <Searchbar onSearch={handleSearch}/>
            <div className="card-gallery">

                {filteredRecipes.length === 0 ? (
                    <p>No recipes found for your search.</p>
                ) : (
                    filteredRecipes.map((recipe) => (
                        <RecipeCard
                            key={recipe.id}
                            Recipe={recipe}
                            onDelete={props.onDelete}
                            showDeleteButton={true}
                            onToggleWishlist={props.onToggleWishlist}
                            isFavorite={props.user?.favorites.some(fav => fav.id === recipe.id)}
                        />
                    ))
                )}
            </div>

        </>
    )
}