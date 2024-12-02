import {Recipe} from "../Model/Recipe.ts";
import React, {ChangeEvent, useState} from "react";
import axios from "axios";
import {useParams} from "react-router-dom";
type EditProps={
    recipe:Recipe
    setRecipes: React.Dispatch<React.SetStateAction<Recipe[]>>
}
export default function Edit(props:EditProps){
    const { id } = useParams<{ id: string }>();
    const [newRecipe, setNewRecipe] = useState<Recipe >(props.recipe);
    const [message, setMessage] = useState<string>("");
    const [preparationRows, setPreparationRows] = useState<number>(4);

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

    const handleIngredientChange = (index: number, value: string) => {
        if (newRecipe) {
            const updatedIngredients = [...newRecipe.ingredients];
            updatedIngredients[index] = value;
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
                ingredients: [...prevRecipe!.ingredients, ""],
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

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();

        if (id && newRecipe) {
            axios
                .put(`/api/cookMe/update/${id}`, newRecipe)
                .then((response) => {
                    props.setRecipes((prev)=> prev. map((p)=>p.id==id ? response.data: p
                    ))
                    setMessage("Recipe updated successfully!");
                })
                .catch((error) => {
                    console.log("Error updating recipe:", error);
                });
        }
    };

    return (
        <div>
            <h1>Edit Recipe</h1>
            {newRecipe && (
                <form className="addRecipeForm" onSubmit={handleSubmit}>
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
                    </div>
                    <div>
                        <label>Ingredients:</label>
                        {newRecipe.ingredients.map((ingredient, index) => (
                            <div key={index} style={{ display: "flex", alignItems: "center" }}>
                                <input
                                    type="text"
                                    placeholder={`Ingredient ${index + 1}`}
                                    value={ingredient}
                                    onChange={(e) => handleIngredientChange(index, e.target.value)}
                                    required
                                />
                                <div className="ingredients_button">
                                    <button type="button" onClick={() => removeIngredient(index)}>
                                        Remove
                                    </button>
                                    <button type="button" onClick={addIngredient}>
                                        Add Ingredient
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                    <button type="submit">Update Recipe</button>
                    {message && <p>{message}</p>} {/* Feedback for user */}
                </form>
            )}
        </div>
    );
}