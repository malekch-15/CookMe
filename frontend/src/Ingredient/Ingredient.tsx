import {BaseIngredient} from "../Model/BaseIngredient.ts";
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
            <h1> Add New Ingredient</h1>
            <div className="data-ingredient">
                <input
                    type="text"
                    placeholder="add ingredient"
                    onChange={HandelInputIngredient}
                />
                <button type="submit" onClick={handleAddIngredient}>Add</button>
            </div>
            <div className="remove-ingredient">
                <h3>Our Ingredient</h3>
                <ul>
                    {props.ingredient.map((i, index) => (
                        <li key={i.id} className="item-name">
                            {i.name}
                            <button className="delete-button" onClick={() => removeIngredient(index)}>Delete</button>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );

}