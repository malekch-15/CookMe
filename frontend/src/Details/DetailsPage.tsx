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
    const { id, name } = useParams<{ id: string; name: string }>();
    const [recipe, setRecipe] = useState<Recipe | null>(null);
    const [error, setError] = useState<string>("");

    const fetchDetails = async () => {
        try {
            const response = id
                ? await axios.get(`/api/cookMe/${id}`)
                : name
                    ? await axios.get(`/api/cookMe/meal/${name}`)
                    : null;

            if (response) {
                setRecipe(response.data);
            } else {
                setError("Invalid parameters. Cannot fetch recipe.");
            }
        } catch (err) {
            setError("Error fetching recipe: " + err+ error);
        }

    }
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

    }, [id,name]);
    if (!recipe) {
        return <div className="loading">loading....</div>
    }
    return (

        <Routes>
            <Route index element={<Details recipe={recipe}/>}/>
            <Route path={"edit"} element={<Edit recipe={recipe} updateRecipe={updateRecipe} ingredient={ingredient}/>}/>
        </Routes>
    )
}