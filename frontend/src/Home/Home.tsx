import {Recipe} from "../Model/Recipe.ts";
import RecipeCard from "../Recipe/RecipeCard.tsx";
import {useState} from "react";
import Searchbar from "./Searchbar.tsx";

type HomeProps={
    recipe:Recipe[]
    onDelete:(id:string)=>void
    onToggleWishlist: (id: string) => void
}
export default function Home(props:HomeProps){
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

        <div>
            <div>
                <Searchbar onSearch={handleSearch}/>
                <h1>Recipes</h1>
                <p> CookMe is your source for thousands of delicious, easy and quick recipes, healthy meals,
                    dinner ideas, recipes for kids, holiday menus, and more</p>
            </div>

            <div className="gallery-container">

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
                        />
                    ))
                )}
            </div>
        </div>
    )
}