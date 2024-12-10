import "./Header.css"
import {Link} from "react-router-dom";
export default function Header() {
    // const [userName,setUserName]=useState<string>("")


    return (
        <div>
            <nav className="navbar">
                <Link to="/" className="logo">
                    <img src="/Logo.png" alt="Company Logo"/>
                </Link>
                <div className="navbar-links">
                    <Link to={"/"}>Home</Link>
                    <a>Meal Plan</a>
                    <Link to={"/Ingredient"}>Ingredients</Link>
                    <Link to={"/WishList"}>Favorite</Link>
                    <Link to={"/New_Recipe"}>Add Recipe</Link>

                </div>
            </nav>
        </div>
    );
}