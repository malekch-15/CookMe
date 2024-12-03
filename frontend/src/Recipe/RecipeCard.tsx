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
export default function RecipeCard(props: RecipeCardProps) {
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
            <button className="add-button">+</button>
            <button id="" onClick={() => props.onToggleWishlist(props.Recipe.id)}
                    className={props.Recipe.status === "FAVORITE" ? "red" : "black"}
            >♥
            </button>
            <div className="recipe-header">
                <img className="recipe-image" src={props.Recipe.imageUrl} alt={"image recipe"}/>
            </div>
            <h2 className="recipe-name">{props.Recipe.name}</h2>
            <p className="recipe-description">{props.Recipe.description}</p>
            <p className="recipe-time">{props.Recipe.time}min</p>
            <div className="recipe-actions">
                {props.showDeleteButton && (<button className="delete-button" onClick={handleDeleteClick}
                                                    disabled={!props.onDelete}>Delete</button>)}
                <button className="show-more-button" onClick={() => handleViewDetails(props.Recipe.id)}>Show More
                </button>
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