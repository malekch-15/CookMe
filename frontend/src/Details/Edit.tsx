import {Recipe} from "../Model/Recipe.ts";
import React, {ChangeEvent, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
type EditProps={
    recipe:Recipe
    updateRecipe:(newRecipe:Recipe,id:string)=>void
}
export default function Edit(props:EditProps){
    const { id } = useParams<{ id: string }>();
    const [newRecipe, setNewRecipe] = useState<Recipe >(props.recipe);
    const [message, setMessage] = useState<string>("");
    const [preparationRows, setPreparationRows] = useState<number>(4);
    const navigate = useNavigate();

    const HandelInputRecipe = (event: ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
        const { name, value } = event.target;
        if (newRecipe) {
            setNewRecipe((prev) => ({
                ...prev!,
                [name]: value,
            }));
        }

        if (name === "preparation") {
            const lines = value.split("\n").length;
            setPreparationRows(Math.max(4, lines));
        }
    };

    const handleIngredientChange = (index: number, event: React.ChangeEvent<HTMLInputElement>, field: string) => {
        if (newRecipe) {

            const updatedIngredients = [...newRecipe.ingredients];

            if (field === 'name') {
                updatedIngredients[index] = {
                    ...updatedIngredients[index],
                    ingredient: {
                        ...updatedIngredients[index].ingredient,
                        name: event.target.value,
                    },
                };
            } else if (field === 'quantity') {
                updatedIngredients[index] = {
                    ...updatedIngredients[index],
                    quantity:parseFloat( event.target.value),
                };
            }
            setNewRecipe((prevRecipe) => ({
                ...prevRecipe!,
                ingredients: updatedIngredients,
            }));
        }
    };

    const addIngredient = () => {
        if (newRecipe) {
            setNewRecipe((prevRecipe) => ({
                ...prevRecipe!,
                ingredients: [...prevRecipe!.ingredients, {quantity:0,ingredient:{id:"",name:""}}],
            }));
        }
    };

    const removeIngredient = (index: number) => {
        if (newRecipe) {
            const updatedIngredients = newRecipe.ingredients.filter((_, i) => i !== index);
            setNewRecipe((prevRecipe) => ({
                ...prevRecipe!,
                ingredients: updatedIngredients,
            }));
        }
    };

    const handleSubmit = (e: React.FormEvent) => {
       e.preventDefault();
        if (id && newRecipe) {
            props.updateRecipe(newRecipe,id)
            setMessage("Recipe updated successfully!");
            setTimeout(() => {
                navigate(`/details/${id}`);
            }, 1000); // Adjust the delay (in milliseconds) as needed

        }

    };

    return (
        <div>
            <h1>Edit Recipe</h1>
            {newRecipe && (
                <form className="addRecipeForm" >
                    <div className="addRecipe">
                        <input
                            type="text"
                            name="name"
                            placeholder="Recipe Name"
                            value={newRecipe.name}
                            onChange={HandelInputRecipe}
                            required
                        />
                        <textarea
                            className="description_input"
                            name="description"
                            placeholder="Description"
                            value={newRecipe.description}
                            onChange={HandelInputRecipe}
                            rows={2}
                            required
                        />
                        <textarea
                            className="textarea"
                            name="preparation"
                            placeholder="Preparation"
                            value={newRecipe.preparation}
                            onChange={HandelInputRecipe}
                            required
                            rows={preparationRows}
                        />
                        <input
                            type="text"
                            name="time"
                            placeholder="Time of Cooking"
                            value={newRecipe.time}
                            onChange={HandelInputRecipe}
                            required
                        />
                        <input
                            type="text"
                            name="imageUrl"
                            placeholder="Image URL"
                            value={newRecipe.imageUrl}
                            onChange={HandelInputRecipe}
                        />
                        <select
                            name="status"
                            value={newRecipe.status}
                            onChange={HandelInputRecipe}
                            required
                        >
                            <option value="NOT_FAVORITE">Not Favorite</option>
                            <option value="FAVORITE">Favorite</option>
                        </select>
                        <label>Ingredients:</label>
                        <div className="ingredients_button">

                            <button type="button" onClick={addIngredient}>
                                Add Ingredient
                            </button>
                        </div>
                        {newRecipe.ingredients.map((ingredient, index) => (
                            <div key={index}>
                                <div className="ingredient-row">
                                    <input
                                        type="text"
                                        placeholder={`Ingredient ${index + 1}`}
                                        value={ingredient.ingredient.name}
                                        onChange={(e) => handleIngredientChange(index, e, 'name')}
                                        required
                                    />
                                    <input
                                        type="number"
                                        placeholder="Quantity"
                                        value={ingredient.quantity} // Display the current quantity
                                        onChange={(e) => handleIngredientChange(index, e, 'quantity')} // Handle quantity change
                                        required
                                    />

                                </div>
                                <button type="button" onClick={() => removeIngredient(index)}>
                                    Remove
                                </button>
                            </div>
                        ))}
                    </div>
                    <div>
                    </div>
                    <button type="submit" onSubmit={handleSubmit}>Update Recipe</button>
                    {message && <p>{message}</p>} {/* Feedback for user */}
                </form>
            )}
        </div>
    );
}