
import { useEffect, useState } from "react";
import axios from "axios";
import {Recipe} from "./Model/Recipe.ts";
import {AppUser} from "./Model/AppUser.ts";
import RecipeCard from "./Recipe/RecipeCard.tsx";
type PropsMealPlan={
    user:AppUser|undefined
    onDelete:(id:string)=>void
    onToggleWishlist: (id: string) => void
}
export default function MealPlan ({user,onToggleWishlist,onDelete}:PropsMealPlan) {
    const [mealPlan, setMealPlan] = useState<Recipe[]>([]);
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
            {mealPlan.length > 0 ? (
                <div className="meal-plan-list">
                    {mealPlan.map((recipe) => (
                        <RecipeCard key={recipe.id} Recipe={recipe}
                                    onDelete={onDelete}
                                    onToggleWishlist={onToggleWishlist}/>
                    ))}
                </div>
            ) : (
                <p>No meal plan available. Please add ingredients to your pantry!</p>
            )}
        </div>
    );
};

