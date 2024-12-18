import React, { useState} from 'react';
import axios from 'axios';
import {AppUser} from "../Model/AppUser.ts";
import {BaseIngredient} from "../Model/BaseIngredient.ts";
import {useNavigate} from "react-router-dom";

type PropsIngredient={
    user:AppUser|undefined
    ingredient:BaseIngredient[]
    onAddIngredient:(name:string)=>Promise<BaseIngredient>
    setUser: React.Dispatch<React.SetStateAction<AppUser|undefined>>
}
export default function UserIngredient(props:PropsIngredient){
    const [newIngredient, setNewIngredient] = useState<BaseIngredient>({ id: '', name: '' });
    const [quantity, setQuantity] = useState<string>("");
    const [isCustomInput, setIsCustomInput] = useState<boolean>(false);
const navigate=useNavigate();

    const handleAddIngredient = async () => {
        if (!newIngredient.name || quantity <= "0") {
            alert('Please enter a valid ingredient name and quantity.');
            return;
        }

        let ingredientToAdd = newIngredient;

        // Check if the ingredient exists
        const existingIngredient = props.ingredient.find(
            (ing) => ing.name.toLowerCase() === newIngredient.name.toLowerCase()
        );

        if (!existingIngredient) {
            // Add the new ingredient
            ingredientToAdd = await props.onAddIngredient(newIngredient.name);
        }

        const newRecipeIngredient = {quantity, ingredient: ingredientToAdd};

        // Optimistically update the user state
        props.setUser((prevUser) =>
            prevUser
                ? {
                    ...prevUser,
                    ingredient: [...prevUser.ingredient, newRecipeIngredient],
                }
                : prevUser
        );

        axios.post(`/api/cookMe/user/${props.user?.id}/ingredients`, ingredientToAdd, {
            params: {quantity},
        }).then(()=>navigate('/mealPlan')).catch((error) => {
            console.error("Error adding ingredient:", error);

            // Roll back the optimistic update if the request fails
            props.setUser((prevUser) =>
                prevUser
                    ? {
                        ...prevUser,
                        ingredient: prevUser.ingredient.filter(
                            (ing) => ing.ingredient?.id !== newIngredient.id
                        ),
                    }
                    : prevUser
            );
        });
    };
        // Handle removing an ingredient
    const handleDelete = async (id: string|undefined) => {
        if (!props.user) {
            alert("Please log in to manage your ingredients.");
            return;
        }
        try {
            // Call the backend to delete the ingredient
           axios.delete(`/api/cookMe/user/${props.user.id}/ingredients`,{
                headers: { "Content-Type": "text/plain" },
                data:id
           }).then(()=>{
               axios.get("/api/users/me")
                   .then((response) => {
                      props.setUser(response.data);
                   })
                   .catch((error) => {
                       console.error(error);
                      props.setUser(undefined);
                   });
           })
        } catch (error) {
            console.error("Error deleting ingredient:", error);

            // Roll back state update if the API request fails
            alert("Failed to delete ingredient. Please try again.");
        }
    };


        return (
            <div>
                <h1>Manage Your Ingredients</h1>
                {props.user ? (
                    <>
                        <div>
                            <h2>Current Ingredients</h2>
                            {props.user.ingredient.map((ingredient) => (
                                <div key={ingredient.ingredient?.id}>
                                <span>
                                    {ingredient.ingredient?.name}
                                </span>
                                    <button onClick={() =>{ console.log('Button clicked');handleDelete(ingredient.ingredient?.id)}}>
                                        Remove
                                    </button>
                                </div>
                            ))}
                        </div>
                        <div>
                            <h2>Add a New Ingredient</h2>
                            <div>
                                <label htmlFor="ingredientSelect">Choose an Ingredient:</label>
                                <select
                                    id="ingredientSelect"
                                    value={newIngredient.id}
                                    onChange={(e) => {
                                        const selectedId = e.target.value;
                                        if (selectedId === "custom") {
                                            setIsCustomInput(true);
                                            setNewIngredient({id: '', name: ''}); // Clear for custom input
                                        } else {
                                            setIsCustomInput(false);
                                            const selectedIngredient = props.ingredient.find(
                                                (ing) => ing.id === selectedId
                                            );
                                            if (selectedIngredient) {
                                                setNewIngredient(selectedIngredient);
                                            }
                                        }
                                    }}
                                >
                                    <option value="" disabled>
                                        Select an ingredient
                                    </option>
                                    {props.ingredient.map((ing) => (
                                        <option key={ing.id} value={ing.id}>
                                            {ing.name}
                                        </option>
                                    ))}
                                    <option value="custom">Add New Ingredient</option>
                                </select>
                            </div>
                            {isCustomInput && (
                                <input
                                    type="text"
                                    placeholder="New Ingredient Name"
                                    value={newIngredient.name}
                                    onChange={(e) =>
                                        setNewIngredient({id: `${Date.now()}`, name: e.target.value})
                                    }
                                />
                            )}
                            <input
                                type="number"
                                placeholder="Quantity"
                                value={quantity}
                                onChange={(e) => setQuantity(e.target.value)}
                            />
                            <button onClick={handleAddIngredient}>Add Ingredient</button>
                        </div>
                    </>
                ) : (
                    <p>Loading user data...</p>
                )}
            </div>
        );


};

