import {Recipe} from "../Model/Recipe.ts";
import {useNavigate} from "react-router-dom";
import "./Details.css"
type DetailsProps={
    recipe:Recipe
}

export default function Details({recipe}:DetailsProps) {
console.log(recipe)
    const navigate= useNavigate()

    const handleEdit = (id: string) => {
        navigate(`/details/${id}/edit`);
    }
    return (
        <div className="details">
            {recipe.imageUrl ? (
                <img
                    src={recipe.imageUrl}
                    alt={recipe.name}
                    className="details-image"
                />
            ) : (
                <div className="no-image">Image not available</div>
            )}

            <div className="details-text">
            <h1 className="details-title">{recipe.name}</h1>
            <p className="details-info">Cooking Time: {recipe.time} minutes</p>
            <p className="details-description">{recipe.description}</p>
                <div className="details-ingredients">
                    <h3>Ingredients:</h3>
                    <ul>{recipe.ingredients.map((ingredient, index) => (
                        <li key={index}>
                            {ingredient.quantity} {ingredient.ingredient.name}
                        </li>
                    ))}

                    </ul>

                    <div className="details-preparation">
                        <h3>Preparation:</h3>
                        {recipe.preparation.split('\n').map((line, index) => (
                            <p key={index}>{line}</p>
                        ))}
                    </div>

                    <button className="show-more-button" onClick={() => handleEdit(recipe?.id)}>Edit
                    </button>
                </div>
            </div>
        </div>
            );
            }