import {Recipe} from "../Model/Recipe.ts";
import "./Recipe.css"
import {useState} from "react";
type RecipeCardProps={
    Recipe:Recipe
    onDelete:(id:string)=>void
    showDeleteButton?:boolean
}
export default function RecipeCard(props:RecipeCardProps){
    const [showPopup, setShowPopup] = useState(false);

    const handleDeleteClick = () => {
        setShowPopup(true); // Open popup
    };

    const handleConfirmDelete = () => {
        props.onDelete(props.Recipe.id); // Perform delete
        setShowPopup(false); // Close popup
    };

    const handleCancel = () => {
        setShowPopup(false); // Close popup
    };


    return(
        <div className="recipe-card">
            <button className="add-button">+</button>
            <div className="recipe-header" >

                <img className="recipe-image" src={props.Recipe.imageUrl}/>
            </div>
            <h2 className="recipe-name">{props.Recipe.name}</h2>
            <p className="recipe-description">{props.Recipe.description}</p>
            <p className="recipe-time">{props.Recipe.time}</p>
            <div className="recipe-actions">
                {props.showDeleteButton &&(<button className="delete-button" onClick={handleDeleteClick}
                        disabled={!props.onDelete}>Delete</button>)}
                <button className="show-more-button">Show More</button>
            </div>
            {showPopup && (
                <div className="popup-overlay">
                    <div className="popup-content">
                        <h3>Confirm Deletion</h3>
                        <p>Are you sure you want to delete this recipe?</p>
                        <div className="popup-actions">
                            <button onClick={handleConfirmDelete} className="popup-confirm">
                                Yes, Delete
                            </button>
                            <button onClick={handleCancel} className="popup-cancel">
                                Cancel
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>

    )
}