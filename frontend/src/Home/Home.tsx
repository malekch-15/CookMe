import {Recipe} from "../Model/Recipe.ts";
import RecipeCard from "../Recipe/RecipeCard.tsx";
import {useState} from "react";
import Searchbar from "./Searchbar.tsx";

type HomeProps={
    recipe:Recipe[]
    onDelete:(id:string)=>void
}
export default function Home(props:HomeProps){
    const [searchQuery, setSearchQuery] = useState("");
    const filteredRecipes = props.recipe.filter((recipe) =>
        recipe.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
        recipe.ingredients.some((ingredient) =>
            ingredient.toLowerCase().includes(searchQuery.toLowerCase())
        )
    );

    const handleSearch = (query: string) => {
        setSearchQuery(query);
    };
    return(

        <div>
            <div>
                <Searchbar onSearch={handleSearch}/>
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
                        />
                    ))
                )}
            </div>
        </div>
    )
}