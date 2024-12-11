import {Recipe} from "../Model/Recipe.ts";
import "./Recipe.css"
import {useState} from "react";
import {useNavigate} from "react-router-dom";

type RecipeCardProps = {
    Recipe: Recipe
    onDelete: (id: string) => void
    showDeleteButton?: boolean
    onDetails?:(id:string)=>void
    onToggleWishlist: (id: string) => void
}
export default function RecipeCard(props: Readonly<RecipeCardProps>) {
    const [showPopup, setShowPopup] = useState(false);
    const navigate= useNavigate()
    const handleViewDetails = (id: string) => {
        navigate(`/details/${id}`);
    }
    const handleDeleteClick = () => {
        setShowPopup(true);
    };

    const handleConfirmDelete = () => {
        props.onDelete(props.Recipe.id);
        setShowPopup(false);

    };

    const handleCancel = () => {
        setShowPopup(false);

    };

    return (
        <div className="recipe-card">
            <div className="recipe-card-buttons">
                    <button className="add-button">+</button>
                            <button id="" onClick={() => props.onToggleWishlist(props.Recipe.id)}
                                    className={props.Recipe.status === "FAVORITE" ? "red" : "black"}
                            >♥
                            </button>
            </div>

            <div >
                <button
                    onClick={() => handleViewDetails(props.Recipe.id)}
                    aria-label={`View details of ${props.Recipe.name}`}
                    className="recipe_card_image_button"
                >
                    <img
                        src={props.Recipe.imageUrl}
                        className="recipe-card-image"
                        alt={`Image of recipe ${props.Recipe.name}`}
                    />
                </button>
            </div>

            <div className="recipe-card-text">
                <h2 className="recipe-name">{props.Recipe.name}</h2>
                <p className="recipe-description">{props.Recipe.description}</p>
                <p className="recipe-time">{props.Recipe.time}min</p>
            </div>


            <div className="recipe-actions">
                {props.showDeleteButton && (<button onClick={handleDeleteClick}
                                                    disabled={!props.onDelete}>Delete</button>)}
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