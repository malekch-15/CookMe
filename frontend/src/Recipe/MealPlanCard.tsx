
import "../css/Recipe.css"
import {MealBasic} from "../Model/MealBasic.ts";
import {useNavigate} from "react-router-dom";

type MealPlanProps = {
    mealPlan:MealBasic

}
export default function MealPlanCard(props: Readonly<MealPlanProps>) {
    const navigate= useNavigate()
    const handleViewDetails = (mealName: string) => {
        navigate(`/meal/${encodeURIComponent(mealName)}`)
    }
    return (
        <div className="recipe-card">


            <div className="recipe-image-container">


            <button
                onClick={() => handleViewDetails(props.mealPlan?.strMeal)}
                aria-label={`View details of ${props.mealPlan.strMeal}`}
                className="recipe_card_image_button"
            >
                <img
                    src={props.mealPlan?.strMealThumb}
                    className="recipe-card-image"
                    alt={`Image of recipe ${props.mealPlan?.strMeal}`}
                />
            </button>
            </div>
            <div className="recipe-card-text">
                <h2 className="recipe-name">{props.mealPlan?.strMeal}</h2>
            </div>

        </div>

    )
}