import "./Header.css"
import {Link} from "react-router-dom";
export default function Header() {
    return (
        <div>
            <nav className="navbar">
                <img src="logo.png" alt="logo" />
                <div className="navbar-links">
                    <Link to={"/"}>Home</Link>
                    <a >Meal Plan</a>
                    <a >Grocery</a>
                    <Link to={"/Favorite"}>Favorite</Link>
                    <Link to={"/New_Recipe"}>Add Recipe</Link>
                </div>
            </nav>
        </div>
    );
}