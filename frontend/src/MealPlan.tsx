
import React, { useEffect, useState } from "react";
import axios from "axios";
import {AppUser} from "./Model/AppUser.ts";
import {MealBasic} from "./Model/MealBasic.ts";
import MealPlanCard from "./Recipe/MealPlanCard.tsx";
import {Recipe} from "./Model/Recipe.ts";


type PropsMealPlan={
    user:AppUser|undefined
    setRecipes: React.Dispatch<React.SetStateAction<Recipe[]>>

}
export default function MealPlan ({user,setRecipes}:PropsMealPlan) {
    const [mealPlan, setMealPlan] = useState<MealBasic[]>([]);
    const [error, setError] = useState<string>("");
    const [selectedMealName, setSelectedMealName] = useState<string | null>(null);
    const fetchMealPlan = async () => {
        if (!user?.id) {
            setError("User is not logged in.");
            return;
        }

        try {
            const response = await axios.get(`/api/cookMe/user/${user.id}/mealPlan`);
            setMealPlan(response.data);
        } catch (err) {
            console.error("Error fetching meal plan:", err);
            setError("Failed to fetch the meal plan. Please try again later."+error);
        }
    };
    useEffect(() => {
        fetchMealPlan();
    }, [user]);

    const handleMealClick = (mealName: string) => {
        setSelectedMealName(mealName);
    };



    return (
        <div>
            <h1>Meal Plan</h1>
            {user && (
                <div>
                    {mealPlan.length > 0 ? (
                        <div className="card-gallery">
                            {mealPlan.map((mealPlan) => (
                                <MealPlanCard
                                    key={mealPlan.strMeal}
                                    mealPlan={mealPlan}
                                    setRecipes={setRecipes}
                                    onMealClick={handleMealClick} // Pass the click handler
                                />
                            ))}
                        </div>
                    ) : (
                        <p>No meal plan available. Please add ingredients to your pantry!</p>
                    )}
                </div>
            )}
            {selectedMealName && (
                <div>
                    <h2>Selected Meal: {selectedMealName}</h2>
                    {/* You can add more details or content here related to the selected meal */}
                </div>
            )}
            {error && <p className="error-text">{error}</p>}
        </div>
    );
};

