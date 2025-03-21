import React, {ChangeEvent, useEffect, useState} from "react";
import {Recipe} from "../Model/Recipe.ts";
import axios from "axios";
import "../css/AddRecipe.css"
import {Autocomplete, createFilterOptions, TextField} from "@mui/material";

import {BaseIngredient} from "../Model/BaseIngredient.ts";
const filter = createFilterOptions<BaseIngredient>();

type addProps= {
    setRecipe: React.Dispatch<React.SetStateAction<Recipe[]>>
    ingredient :BaseIngredient[]
    newIngredient:BaseIngredient
    onAddIngredient:(name:string)=>Promise<BaseIngredient>
}
export default function AddRecipe(props:Readonly<addProps>) {

    const [newRecipe, setNewRecipe] = useState<Recipe>({
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
        setNewRecipe((prevRecipe) => ({
            ...prevRecipe,
            [name]: value,
        }));
        if (name === "preparation") {
            const lines = value.split("\n").length; // Count the lines based on newlines
            setPreparationRows(Math.max(4, lines)); // Set at least 4 rows
        }
    };

    const handleIngredientChange = (index: number, event:React.ChangeEvent<HTMLInputElement> , field: string) => {
if(newRecipe){
    const newIngredient=[...newRecipe.ingredients]
    if(field==='quantity'){
        newIngredient[index]={
            ...newIngredient[index],quantity:event.target.value
        }
    }
    setNewRecipe((prev)=>({...prev,ingredients:newIngredient}))

}

    };
    const handleIngredientNameChange = async (index: number, value: BaseIngredient | string |null
    ) => {
        const newIngredients = [...newRecipe.ingredients];
        const ingredientExist=props.ingredient.some((i)=>i.name.toLowerCase()===value)
        if (!ingredientExist) {
            // Create a new ingredient
            const newIngredient = await props.onAddIngredient((value as BaseIngredient).name);
            newIngredients[index].ingredient = newIngredient; // Assign generated ingredient
        } else  {
            // User selected an existing ingredient
            newIngredients[index].ingredient = value as BaseIngredient;
        }
        setNewRecipe((prev) => ({ ...prev, ingredients: newIngredients }));
    };


    const addIngredient = () => {
        setNewRecipe((prevRecipe) => ({
            ...prevRecipe,
            ingredients: [
                ...prevRecipe.ingredients,
                { quantity: "0", ingredient: { id: "", name: "" } },
            ],
        }));
    };

    useEffect(() => {
        console.log("Updated newRecipe:", newRecipe);
    }, [newRecipe]);

    const removeIngredient = (index: number) => {
        const updatedIngredients = newRecipe.ingredients.filter((_, i) => i !== index);
        setNewRecipe((prevRecipe) => ({
            ...prevRecipe,
            ingredients: updatedIngredients,
        }));
    };


    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();

            axios.post(`/api/cookMe/add`, newRecipe).then(response=>{
                setNewRecipe(response.data)
                setMessage("Recipe added success" +
                    "fully!");
                props.setRecipe(prevState => [...prevState,response.data])
            }).catch (error=> { console.log("recipe not added",error)})
        }

    return (
      <>
            <h1>
                Let's add some  new Recipe
            </h1>
        <form className="addRecipeForm" onSubmit={handleSubmit}>
            <div className="addRecipe">
                <input
                    type="text"
                    name="name"
                    placeholder="Recipe Name"
                    value={newRecipe.name}
                    onChange={handleInputChange}
                    required
                />
                <textarea
                    className="description-input"
                    name="description"
                    placeholder="Description"
                    value={newRecipe.description}
                    onChange={handleInputChange}
                    rows={2}
                    required
                />
                <textarea
                    className="textarea"
                    name="preparation"
                    placeholder="Preparation"
                    value={newRecipe.preparation}
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
                    value={newRecipe.imageUrl}
                    onChange={handleInputChange}
                />

                <div className="add-ingredient">
                    <label>Ingredients:</label>
                    <button   type="button" onClick={addIngredient}>
                        Add Ingredient
                    </button>
                </div>
                {newRecipe.ingredients.map((ingredient, index) => (
                    <div key={index}>
                        <div className="ingredients-field">

                            <Autocomplete
                                value={newRecipe.ingredients[index]?.ingredient}
                                onChange={(_event, newValue) => handleIngredientNameChange(index, newValue)}
                                filterOptions={(options, params) => {
                                    const filtered = filter(options, params);
                                    const { inputValue } = params;
                                    const isExisting = options.some((option) => inputValue === option.name);

                                    if (inputValue !== '' && !isExisting) {
                                        filtered.push({ id: '', name: inputValue});
                                    }
                                    return filtered;
                                }}
                                selectOnFocus
                                clearOnBlur
                                handleHomeEndKeys
                                options={props.ingredient}
                                getOptionLabel={(option) => (typeof option === 'string' ? option : option.name)}
                                renderOption={(props, option) => (
                                    <li {...props}>{option.name}</li>
                                )}
                                freeSolo
                                renderInput={(params) => (
                                    <TextField {...params} label="Ingredient Name" />
                                )}
                            />

                            <input
                            type="number"
                            placeholder={`Ingredient ${index + 1}`}
                            value={ingredient.quantity}
                            onChange={(e) => handleIngredientChange(index, e,'quantity')}
                            required
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
            <button type="submit"> Add Recipe</button>
            {message && <p>{message}</p>} {/* Feedback for user */}
        </form>
        </>
    );
}
