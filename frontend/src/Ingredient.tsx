import {BaseIngredient} from "./Model/BaseIngredient.ts";
import React, {ChangeEvent, useState} from "react";


type IngredientProps = {
    ingredient: BaseIngredient[]
    onAddIngredient: (name: string) => Promise<BaseIngredient>
    setIngredient: React.Dispatch<React.SetStateAction<BaseIngredient[]>>
}
export default function Ingredient(props: Readonly<IngredientProps>) {
    const [newIngredient, setNewIngredient] = useState<string>("")

    const HandelInputIngredient = (event: ChangeEvent<HTMLInputElement>) => {
        const name = event.target.value;
        setNewIngredient(name);

    }
    const handleAddIngredient = () => {
        if (!newIngredient.trim()) {
            alert("the name of the ingredient can not be empty")
        }
        const ingredientExist = props.ingredient.some((i) => i.name.toLowerCase() === newIngredient.toLowerCase())
        if (ingredientExist) {
            alert("ingredient exsit")
        } else {
            props.onAddIngredient(newIngredient)
            setNewIngredient("")
            alert("ingredient added")
        }
    }
    const removeIngredient = (index: number) => {
        const updatedIngredients = props.ingredient.filter((_, i) => i !== index);
        props.setIngredient(updatedIngredients);
    };

    return (
        <div>
            <div>
                <input
                    type="text"
                    placeholder="add ingredient"
                    onChange={HandelInputIngredient}
                />
                <button onClick={handleAddIngredient}>Add</button>
            </div>
            <div>
                <h3>Current Ingredients:</h3>
                <ul>
                    {props.ingredient.map((i, index) => (
                        <li key={i.id}>
                            {i.name}
                            <button onClick={() => removeIngredient(index)}>Delete</button>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );

}