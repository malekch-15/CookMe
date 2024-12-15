
import { useEffect, useState } from "react";
import axios from "axios";

import {AppUser} from "./Model/AppUser.ts";

import {MealBasic} from "./Model/MealBasic.ts";
import MealPlanCard from "./Recipe/MealPlanCard.tsx";
type PropsMealPlan={
    user:AppUser|undefined
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
        <div className="meal-plan-page">
            <h1>Meal Plan</h1>
            {user &&(
                <div className="meal-plan-list">
                    {mealPlan.length > 0 ? (
                        <div>
                    {mealPlan.map((recipe) => (
                        <MealPlanCard mealPlan={recipe}/>
                    ))}
                        </div>
            ) : (
                <p>No meal plan available. Please add ingredients to your pantry!</p>
                    )}</div>
                   )}

        </div>
    );
};

