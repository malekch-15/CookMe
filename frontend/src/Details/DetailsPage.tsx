import Details from "./Details.tsx";
import {Route, Routes, useParams} from "react-router-dom";
import Edit from "./Edit.tsx";
import React, {useEffect, useState} from "react";
import {Recipe} from "../Model/Recipe.ts";
import axios from "axios";
import {BaseIngredient} from "../Model/BaseIngredient.ts";

type DetailsPagesProps = {
    setRecipes: React.Dispatch<React.SetStateAction<Recipe[]>>
    ingredient: BaseIngredient[]
}
export default function DetailsPage({setRecipes, ingredient}: Readonly<DetailsPagesProps>) {
    const { id, mealName } = useParams<{ id: string; mealName: string }>();
    const [recipe, setRecipe] = useState<Recipe | null>(null);
    const [error, setError] = useState<string>("");

    const fetchDetails = () => {
        if (id) {
            axios.get(`/api/cookMe/${id}`)
                .then((response) => setRecipe(response.data))
                .catch((error) => {
                    setError("Error fetching recipe: " + error.message);
                    console.error("Error fetching recipe by ID:", error);
                });
        } else if (mealName) {
            axios.get(`/api/cookMe/meal/${encodeURIComponent(mealName)}`)
                .then((response) =>{

                    setRecipe(response.data)
                })
                .catch((error) => {
                    setError("Error fetching recipe: " + error.message);
                    console.error("Error fetching recipe by name:", error);
                });
        } else {
            setError("Error: Either 'id' or 'name' must be provided.");
            console.log(error)
        }
    };

    const updateRecipe = (newRecipe: Recipe, id: string) => {
        axios
            .put(`/api/cookMe/update/${id}`, newRecipe)
            .then((response) => {
                setRecipes((prev) => prev.map((p) => p.id == id ? response.data : p
                ))
                setRecipe(response.data)
            })
            .catch((error) => {
                console.log("Error updating recipe:", error);
            });
    }
    useEffect(() => {
        fetchDetails();
    }, [id,mealName]);


    if (!recipe) {
        return <div className="loading">loading....</div>
    }
    return (

        <Routes>
            <Route index element={<Details recipe={recipe} />}/>
            <Route path={"edit"} element={<Edit recipe={recipe} updateRecipe={updateRecipe} ingredient={ingredient}/>}/>
        </Routes>
    )
}