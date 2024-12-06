import React, {ChangeEvent, useEffect, useState} from "react";
import {Recipe} from "./Model/Recipe";
import axios from "axios";
import "./AddRecipe.css"
import {Autocomplete, createFilterOptions, TextField} from "@mui/material";
import { styled } from "@mui/material/styles";
import {BaseIngredient} from "./Model/BaseIngredient.ts";
const filter = createFilterOptions<unknown>();

const CustomAutocomplete = styled(Autocomplete)(() => ({
    width: '300px',
    backgroundColor: '#f9f9f9',
    border: '1px solid #ccc',
    borderRadius: '4px',
    "& .MuiOutlinedInput-root": {
        padding: '5px',
    },
    "& .MuiAutocomplete-listbox": {
        backgroundColor: '#fff',
        border: '1px solid #ccc',
    },
}));
type addProps= {
    setRecipe: React.Dispatch<React.SetStateAction<Recipe[]>>
    ingredient :BaseIngredient[]
    newIngredient:BaseIngredient
    onAddIngredient:(name:string)=>void
}
export default function AddRecipe(props:addProps) {

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
            ...newIngredient[index],quantity:parseFloat(event.target.value)
        }
    }
    setNewRecipe((prev)=>({...prev,ingredients:newIngredient}))

}

    };
    const handleIngredientNameChange = async (index: number, value: unknown) => {
        if (!value) return;

        const newIngredients = [...newRecipe.ingredients];

        if (typeof value === "string") {
            // User entered a new ingredient name
            const addedIngredient = await props.onAddIngredient(value);
            if (addedIngredient) {
                newIngredients[index].ingredient = addedIngredient; // Use the added ingredient with its id
            }
        } else {
            // User selected an existing ingredient
            const selectedIngredient = props.ingredient.find((ing) => ing.name === value);
            if (selectedIngredient) {
                newIngredients[index].ingredient = selectedIngredient;
            }
        }

        setNewRecipe((prev) => ({ ...prev, ingredients: newIngredients }));
    };


    const addIngredient = () => {
        setNewRecipe((prevRecipe) => ({
            ...prevRecipe,
            ingredients: [
                ...prevRecipe.ingredients,
                { quantity: 0, ingredient: { id: "", name: "" } },
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
                <select
                    name="status"
                    value={newRecipe.status}
                    onChange={handleInputChange}
                    required
                >
                    <option value="NOT_FAVORITE">Not Favorite</option>
                    <option value="FAVORITE">Favorite</option>
                </select>
                <label>Ingredients:</label>
                <div>
                <button type="button" onClick={addIngredient}>
                    Add Ingredient
                </button>
                </div>
                {newRecipe.ingredients.map((ingredient, index) => (
                    <div key={index}>
                        <div className="ingredients-field">
                            <CustomAutocomplete
                                value={ingredient.ingredient.name || ""}
                                onChange={async (_event, newValue) => {
                                    await handleIngredientNameChange(index, newValue);
                                }}
                                freeSolo
                                filterOptions={(options, params) => {
                                    const filtered = filter(options, params);
                                    const { inputValue } = params;

                                    const isExisting = options.some(
                                        (option) => option.name.toLowerCase() === inputValue.toLowerCase()
                                    );

                                    if (inputValue !== "" && !isExisting) {
                                        filtered.push({ id: "", name: inputValue }); // Add a temporary ingredient
                                    }

                                    return filtered;
                                }}
                                options={props.ingredient}
                                getOptionLabel={(option) => (typeof option === "string" ? option : option.name)}
                                renderOption={(props, option) => (
                                    <li {...props}>{typeof option === "string" ? option : option.name}</li>
                                )}
                                renderInput={(params) => <TextField {...params} label="Ingredient Name" />}
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


            <button type="submit">Submit Recipe</button>
            {message && <p>{message}</p>} {/* Feedback for user */}
        </form>
        </div>
    );
}
