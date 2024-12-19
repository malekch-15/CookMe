
import "../css/Recipe.css"
import {MealBasic} from "../Model/MealBasic.ts";
import {useNavigate} from "react-router-dom";
import {Recipe} from "../Model/Recipe.ts";
import axios from "axios";
import React, { useState} from "react";

type MealPlanProps = {
    mealPlan:MealBasic
    setRecipes: React.Dispatch<React.SetStateAction<Recipe[]>>
    onMealClick:(mealName: string) => void
}
    export default function MealPlanCard(props: Readonly<MealPlanProps>) {
        const navigate = useNavigate();
        const [error, setError] = useState<string>("");
        const [loading, setLoading] = useState<boolean>(false);

        const fetchAndSaveMeal = async (meal: MealBasic) => {
            try {
                // Fetch meal by name
                const response = await axios.get(`/api/cookMe/meal/${encodeURIComponent(meal.strMeal)}`);
                const fetchedMeal = response.data;

                // Save fetched meal to API
                const postResponse=  await axios.post(`/api/cookMe/mealApi`, fetchedMeal, {
                    headers: { "Content-Type": "application/json" },
                });

                return postResponse.data
                 // Return the saved recipe data
            } catch (err) {
                console.error("Error in fetchAndSaveMeal:", err);
                setError("Failed to fetch and save some meals. Please try again.");
                return null;
            }
        };

        const handleViewDetails = async (meal: MealBasic) => {
            setLoading(true);
            console.log(meal)
            props.onMealClick(meal.strMeal);
            try {
                const savedRecipe = await fetchAndSaveMeal(meal);
                console.log(savedRecipe)
                if (savedRecipe?.id) {
                    navigate(`/details/${savedRecipe.id}`);
                } else {
                    console.error("Failed to navigate: Recipe not saved.");
                }
            } finally {
                setLoading(false);
            }
        };

        return (
            <div className="meal-plan-list">
                <div className="recipe-card">
                    <div className="recipe-image-container">
                        <button
                            onClick={() => handleViewDetails(props.mealPlan)} // Directly pass the single meal object
                            aria-label={`View details of ${props.mealPlan.strMeal}`}
                            className="recipe_card_image_button"
                            disabled={loading} // Disable while loading
                        >
                            <img
                                src={props.mealPlan.strMealThumb}
                                className="recipe-card-image"
                                alt={`Image of recipe ${props.mealPlan.strMeal}`}
                            />
                        </button>
                    </div>
                    <div className="recipe-card-text">
                        <h2 className="recipe-name">{props.mealPlan.strMeal}</h2>
                    </div>
                </div>
                {error && <p className="error-text">{error}</p>}
            </div>
        );
    }
