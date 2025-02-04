import {Recipe} from "../Model/Recipe.ts";
import React, {ChangeEvent, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {BaseIngredient} from "../Model/BaseIngredient.ts";

type EditProps = {
    recipe: Recipe
    updateRecipe: (newRecipe: Recipe, id: string) => void
    ingredient: BaseIngredient[]
}
export default function Edit(props: Readonly<EditProps>) {
    const {id} = useParams<{ id: string }>();
    const [newRecipe, setNewRecipe] = useState<Recipe>(props.recipe);
    const [message, setMessage] = useState<string>("");
    const [preparationRows, setPreparationRows] = useState<number>(4);
    const navigate = useNavigate();

    const HandelInputRecipe = (event: ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
        const {name, value} = event.target;
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
    const handleIngredientSelection = (index: number, event: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const selectedIngredientName = event.target.value;
        const selectedIngredient = props.ingredient.find(
            (ingredient) => ingredient.name === selectedIngredientName
        );
        console.log(selectedIngredient)

        const updatedIngredients = [...newRecipe.ingredients];
        updatedIngredients[index] = {
            ...updatedIngredients[index],
            ingredient: {
                id: selectedIngredient?.id ?? "",
                name: selectedIngredientName,
            },
        };
        setNewRecipe((prevRecipe) => ({
            ...prevRecipe!,
            ingredients: updatedIngredients,
        }));

    };

    const handleIngredientChange = (index: number, event: React.ChangeEvent<HTMLInputElement>, field: string) => {
        if (newRecipe) {
            const updatedIngredients = [...newRecipe.ingredients];

            if (field === 'quantity') {
                updatedIngredients[index] = {
                    ...updatedIngredients[index],
                    quantity: event.target.value,
                };
            }
            setNewRecipe((prevRecipe) => ({
                ...prevRecipe!,
                ingredients: updatedIngredients,
            }));
        }
    };

    const addIngredient = () => {

        const newIngredient: BaseIngredient = {
            id: "",
            name: ""
        }
        setNewRecipe((prevRecipe) => ({
            ...prevRecipe!,
            ingredients: [...prevRecipe!.ingredients, {quantity: "", ingredient: {id: "", name: newIngredient.name}}],
        }));

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
            props.updateRecipe(newRecipe, id)
            setMessage("Recipe updated successfully!");
            setTimeout(() => {
                navigate(`/details/${id}`);
            }, 1000); // Adjust the delay (in milliseconds) as needed

        }

    };

    return (
        <>
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

                        <div className="add-ingredient">
                            <label>Ingredients:</label>
                            <button type="button" onClick={addIngredient}>
                                Add Ingredient
                            </button>
                        </div>
                        {newRecipe.ingredients.map((ingredient, index) => (
                            <div key={index}>
                                <div className="ingredients-field">
                                    <select
                                        value={ingredient.ingredient?.name} // Select the current ingredient name
                                        onChange={(e) =>
                                            handleIngredientSelection(index, e)}
                                        required
                                    >
                                        <option value="">Select an ingredient</option>
                                        {props.ingredient.map((ing) => (
                                            <option key={ing.id} value={ing.name}>
                                                {ing.name}
                                            </option>
                                        ))}
                                    </select>
                                    <input
                                        type="number"
                                        placeholder="Quantity"
                                        value={ingredient.quantity}
                                        onChange={(e) => handleIngredientChange(index, e, 'quantity')}
                                    />
                                </div>
                                <div className="ingredients-button">
                                    <button type="button" onClick={() => removeIngredient(index)}>
                                        Remove
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                    <button type="submit">Update Recipe</button>
                    {message && <p>{message}</p>} {/* Feedback for user */}

                </form>
            )}
        </>
    );
}