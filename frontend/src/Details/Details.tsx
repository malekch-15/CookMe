import {Recipe} from "../Model/Recipe.ts";
import {useNavigate} from "react-router-dom";
import "./Details.css"

type DetailsProps = {
    recipe: Recipe
}

export default function Details({recipe}: Readonly<DetailsProps>) {
    console.log(recipe)
    const navigate = useNavigate()

    const handleEdit = (id: string) => {
        navigate(`/details/${id}/edit`);
    }
    const preparationSteps = recipe.preparation.split(/[.]\s/);
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
                    <ul>{recipe.ingredients.map((ingredient) => (
                        <li key={ingredient.ingredient.id}>
                            {ingredient.quantity} {ingredient.ingredient.name}
                        </li>
                    ))}

                    </ul>

                    <div className="details-preparation">
                        <h3>Preparation:</h3>
                        <ol>
                            {preparationSteps.map((step, index) => (
                                <li key={index}>{step}</li>
                            ))}
                        </ol>


                </div>

                <button className="show-more-button" onClick={() => handleEdit(recipe?.id)}>Edit
                </button>
            </div>
        </div>
</div>
)
    ;
}