import React, {ChangeEvent, useState} from "react";
import {Recipe} from "./Model/Recipe";
import axios from "axios";
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
        ingredients: [""],
    });
    const [message, setMessage] = useState<string>("");

    const handleInputChange = (event: ChangeEvent<HTMLInputElement>) => {
        const {name, value} = event.target;
        setRecipe((prevRecipe) => ({
            ...prevRecipe,
            [name]: value,
        }));
    };


    const handleIngredientChange = (index: number, value: string) => {
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
            ingredients: [...prevRecipe.ingredients, ""],
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
        <form onSubmit={handleSubmit}>
            <div>
                <input
                    type="text"
                    name="name"
                    placeholder="Recipe Name"
                    value={recipe.name}
                    onChange={handleInputChange}
                    required
                />
                <input
                    type="text"
                    name="description"
                    placeholder="Description"
                    value={recipe.description}
                    onChange={handleInputChange}
                    required
                />
                <input
                    type="text"
                    name="preparation"
                    placeholder="Preparation"
                    value={recipe.preparation}
                    onChange={handleInputChange}
                    required
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
                <input
                    type="text"
                    name="status"
                    placeholder="Status"
                    onChange={handleInputChange}
                />
            </div>
            <div>
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
                        <button type="button" onClick={() => removeIngredient(index)}>
                            Remove
                        </button>
                    </div>
                ))}
                <button type="button" onClick={addIngredient}>
                    Add Ingredient
                </button>
            </div>
            <button type="submit">Submit Recipe</button>
            {message && <p>{message}</p>} {/* Feedback for user */}
        </form>
        </div>
    );
}
