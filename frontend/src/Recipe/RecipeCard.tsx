import {Recipe} from "../Model/Recipe.ts";
import "./Recipe.css"
type RecipeCardProps={
    Recipe:Recipe
}
export default function RecipeCard({Recipe}:RecipeCardProps){


    return(
        <div className="recipe-card">
            <button className="add-button">+</button>
            <div className="recipe-header">
                <img className="recipe-image" src={Recipe.imageUrl}/>
            </div>
            <h2 className="recipe-name">{Recipe.name}</h2>
            <p className="recipe-description">{Recipe.description}</p>
            <p className="recipe-time">{Recipe.time}</p>
            <div className="recipe-actions">
                <button className="delete-button">Delete</button>
                <button className="show-more-button">Show More</button>
            </div>
        </div>

    )
}