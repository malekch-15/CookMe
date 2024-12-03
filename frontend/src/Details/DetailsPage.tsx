import Details from "./Details.tsx";
import {Route, Routes, useParams} from "react-router-dom";
import Edit from "./Edit.tsx";
import React, {useEffect, useState} from "react";
import {Recipe} from "../Model/Recipe.ts";
import axios from "axios";
type DetailsPagesProps={
    setRecipes: React.Dispatch<React.SetStateAction<Recipe[]>>
}
export default function DetailsPage({setRecipes}:DetailsPagesProps){
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
    return(

        <Routes>
            <Route index element={<Details recipe={recipe}/>}/>
            <Route path={"edit"} element={<Edit  recipe={recipe} setRecipes={setRecipes}/>}/>
        </Routes>
    )
}