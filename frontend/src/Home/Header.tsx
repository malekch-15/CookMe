import "./Header.css"
import {Link} from "react-router-dom";
import {AppUser} from "../Model/AppUser.ts";

type PropsHeader={
    user:AppUser|undefined
}
export default function Header({user}:PropsHeader) {
    const handleLogin = () => {
        const host = window.location.host === 'localhost:5173' ? 'http://localhost:8080' : window.location.origin;
        window.open(`${host}/oauth2/authorization/github`, '_self');
    };

    const handleLogout = () => {
        const host = window.location.host === 'localhost:5173' ? 'http://localhost:8080/api/' : window.location.origin;
        window.open(`${host}/logout`, '_self');
    }

    return (
        <>
            <nav className="navbar">

                <Link to="/" className="logo">
                    <img src="/Logo.png" alt="Company Logo"/>
                </Link>
                    <Link to={"/"}>Home</Link>
                    <a>Meal Plan</a>
                    <Link to={"/Ingredient"}>Ingredients</Link>
                    <Link to={"/WishList"}>Favorite</Link>
                    <Link to={"/New_Recipe"}>Add Recipe</Link>
                    {user=== undefined? (
                        <button className="auth-button login-button" onClick={handleLogin} aria-label="Login">
                            Login
                        </button>
                    ) : (
                        <>
                            < p className="welcome-message">Welcome, {user.username}</p>
                            <button className="auth-button logout-button" onClick={handleLogout} aria-label="Logout">
                                Logout
                            </button>
                        </>
                    )}
            </nav>
        </>
    );
}