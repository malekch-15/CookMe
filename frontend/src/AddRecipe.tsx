import React, {ChangeEvent, useState} from "react";
import {Recipe} from "./Model/Recipe";
import axios from "axios";
import "./AddRecipe.css"
import {RecipeIngredient} from "./Model/RecipeIngredient.ts";
type addProps= {
    setRecipe: React.Dispatch<React.SetStateAction<Recipe[]>>
}
export default function AddRecipe(props:addProps) {
    const [recipe, setRecipe] = useState<Recipe>({
        id: "",
        name: "",
        description: "",
        preparation: "",
        time: 0,
        imageUrl: "",
        status: "NOT_FAVORITE",
        ingredients: [],
    });
    const [message, setMessage] = useState<string>("");
    const [preparationRows, setPreparationRows] = useState<number>(4);



    const handleInputChange = (event: ChangeEvent<HTMLInputElement|HTMLTextAreaElement|HTMLSelectElement>) => {
        const {name, value} = event.target;
        setRecipe((prevRecipe) => ({
            ...prevRecipe,
            [name]: value,
        }));
        if (name === "preparation") {
            const lines = value.split("\n").length; // Count the lines based on newlines
            setPreparationRows(Math.max(4, lines)); // Set at least 4 rows
        }
    };
    const handleAddRecipeIngredient = (ingredientId: string, quantity: number) => {
        const recipeIngredient = { quantity, ingredient: { id: ingredientId } };

        axios
            .post("/api/recipeIngredient", recipeIngredient) // Adjust endpoint as needed
            .then((response) => {
                console.log("Recipe ingredient added successfully:", response.data);
            })
            .catch((error) => {
                console.error("Failed to add recipe ingredient", error);
            });
    };

    const handleIngredientChange = (index: number, value:RecipeIngredient) => {
        const updatedIngredients = [...recipe.ingredients];
        updatedIngredients[index] = value;
        setRecipe((prevRecipe) => ({
            ...prevRecipe,
            ingredients: updatedIngredients,
        }));
    };


    const addIngredient = () => {
        setRecipe((prevRecipe) => ({
            ...prevRecipe,
            ingredients: [...prevRecipe.ingredients,{}],
        }));
    };


    const removeIngredient = (index: number) => {
        const updatedIngredients = recipe.ingredients.filter((_, i) => i !== index);
        setRecipe((prevRecipe) => ({
            ...prevRecipe,
            ingredients: updatedIngredients,
        }));
    };


    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();

            axios.post(`/api/cookMe/add`, recipe).then(response=>{
                setRecipe(response.data)
                setMessage("Recipe added successfully!");
                props.setRecipe(prevState => [...prevState,response.data])
            }).catch (error=> { console.log("recipe not added",error)})
        }

    return (
        <div>
            <h1>
                Let's add some  new Recipe
            </h1>
        <form className="addRecipeForm" onSubmit={handleSubmit}>
            <div className="addRecipe">
                <input
                    type="text"
                    name="name"
                    placeholder="Recipe Name"
                    value={recipe.name}
                    onChange={handleInputChange}
                    required
                />
                <textarea
                    className="description-input"
                    name="description"
                    placeholder="Description"
                    value={recipe.description}
                    onChange={handleInputChange}
                    rows={2}
                    required
                />
                <textarea
                    className="textarea"
                    name="preparation"
                    placeholder="Preparation"
                    value={recipe.preparation}
                    onChange={handleInputChange}
                    required
                    rows={preparationRows}

                />
                <input
                    type="text"
                    name="time"
                    placeholder="Time of Cooking"
                    onChange={handleInputChange}
                    required
                />
                <input
                    type="text"
                    name="imageUrl"
                    placeholder="Image URL"
                    value={recipe.imageUrl}
                    onChange={handleInputChange}
                />
                <select
                    name="status"
                    value={recipe.status}
                    onChange={handleInputChange}
                    required
                >
                    <option value="NOT_FAVORITE">Not Favorite</option>
                    <option value="FAVORITE">Favorite</option>
                </select>
                <label>Ingredients:</label>
                {recipe.ingredients.map((ingredient, index) => (
                    <div key={index}>
                        <input
                            type="text"
                            placeholder={`Ingredient ${index + 1}`}
                            value={ingredient}
                            onChange={(e) => handleIngredientChange(index, e.target.value)}
                            required
                        />
                        <div className="ingredients-button">
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


            <button type="submit">Submit Recipe</button>
            {message && <p>{message}</p>} {/* Feedback for user */}
        </form>
        </div>
    );
}
