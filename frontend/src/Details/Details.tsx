import axios from "axios";
import {Recipe} from "../Model/Recipe.ts";
import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import "./Details.css"


export default function Details() {
    const para = useParams<{ id: string }>()
    const [recipe, setRecipe] = useState<Recipe | null>(null);
    const fetchDetails = () => {
        axios.get(`/api/cookMe/${para.id}`)
            .then((response => {
                setRecipe(response.data)
            })).catch((error) => console.log("no Recipe with this id", error));

    }
    useEffect(() => {
        fetchDetails();
    },[para.id]);
    if (!recipe) {
        return <div className="loading">loading....</div>
    }
    return (
        <div className="details-container">
            {recipe.imageUrl ? (
                <img
                    src={recipe.imageUrl}
                    alt={recipe.name}
                    className="details-image"
                />
            ) : (
                <div className="no-image">Image not available</div>
            )}
            <h1 className="details-title">{recipe.name}</h1>
            <p className="details-info">Cooking Time: {recipe.time} minutes</p>
            <p className="details-description">{recipe.description}</p>
            <div className="details-ingredients">
                <h3>Ingredients:</h3>
                <ul>
                    {recipe.ingredients.map((ingredient, index) => (
                        <li key={index}>{ingredient}</li>
                    ))}
                </ul>
            </div>
            <div className="details-preparation">
                <h3>Preparation:</h3>
                <p>{recipe.preparation}</p>
            </div>
            <div className="details-button-container">
                <button className="details-button">Edit</button>
            </div>
        </div>
    );
}