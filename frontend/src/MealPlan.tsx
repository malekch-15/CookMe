
import React, { useEffect, useState } from "react";
import axios from "axios";

import {AppUser} from "./Model/AppUser.ts";

import {MealBasic} from "./Model/MealBasic.ts";
import MealPlanCard from "./Recipe/MealPlanCard.tsx";
import {Recipe} from "./Model/Recipe.ts";
type PropsMealPlan={
    user:AppUser|undefined
    setRecipe: React.Dispatch<React.SetStateAction<Recipe[]>>
}
export default function MealPlan ({user}:PropsMealPlan) {
    const [mealPlan, setMealPlan] = useState<MealBasic[]>([]);
    const [error, setError] = useState<string>("");
    const fetchMealPlan = async () => {

        axios.get(`/api/cookMe/user/${user?.id}/mealPlan`)
            .then((response)=>{setMealPlan(response.data)}
            ).catch( (err) =>{
            setError("no recipe"+err);

        })


    };
    useEffect(() => {
        fetchMealPlan();
    }, []);

    if (error) {
        return <div>Error: {error}</div>;
    }

    return (
        <div>
            <h1>Meal Plan</h1>
            {user &&(
                <div >
                {mealPlan.length > 0 ? (
                        <div className="card-gallery">
                    {mealPlan.map((mealPlan) => (
                        <MealPlanCard key={mealPlan.idMeal} mealPlan={mealPlan}/>
                    ))}
                        </div>
            ) : (
                <p>No meal plan available. Please add ingredients to your pantry!</p>
                    )}</div>
                   )}

        </div>
    );
};

