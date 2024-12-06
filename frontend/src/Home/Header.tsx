import "./Header.css"
import {Link} from "react-router-dom";
export default function Header() {
    return (
        <div>
            <nav className="navbar">
                <Link to="/" className="logo">
                    <img src="/src/assets/logo.png" alt="Company Logo"/>
                </Link>
                <div className="navbar-links">
                    <Link to={"/"}>Home</Link>
                    <a>Meal Plan</a>
                    <a>Grocery</a>
                    <Link to={"/WishList"}>Favorite</Link>
                    <Link to={"/New_Recipe"}>Add Recipe</Link>
                </div>
            </nav>
        </div>
    );
}