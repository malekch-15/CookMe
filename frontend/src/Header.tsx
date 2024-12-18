import "./css/Header.css"
import {Link} from "react-router-dom";
import {AppUser} from "./Model/AppUser.ts";
import {useEffect, useState} from "react";

type PropsHeader={
    user:AppUser|undefined
}
export default function Header({user}:PropsHeader) {
    const [showWelcomeMessage, setShowWelcomeMessage] = useState(true);
    const handleLogin = () => {
        const host = window.location.host === 'localhost:5173' ? 'http://localhost:8080' : window.location.origin;
        window.open(`${host}/oauth2/authorization/github`, '_self');
    };

    const handleLogout = () => {
        const host = window.location.host === 'localhost:5173' ? 'http://localhost:8080' : window.location.origin;
        window.open(`${host}/logout`, '_self');
    }
    useEffect(() => {
        if (user) {
            const timer = setTimeout(() => {
                setShowWelcomeMessage(false);
            }, 1000);

            return () => clearTimeout(timer); // Cleanup timer
        }
    }, [user]);
    return (
        <>
            <nav className="navbar">

                <Link to="/" className="logo">
                    <img src="/Logo.png" alt="Company Logo"/>
                </Link>
                    <Link to={"/"}>Home</Link>
                {user && (
                    <>
                <Link to={"/mealPlan"}>Meal Plan</Link>
                    <Link to={"/Ingredient"}>Ingredients</Link>
                    <Link to={"/UserIngredient"}>Ingredients</Link>
                    <Link to={"/WishList"}>Favorite</Link>
                    <Link to={"/New_Recipe"}>Add Recipe</Link>
                    </>)}
                    {user=== undefined? (
                        <button className="auth-button-login-button" onClick={handleLogin} aria-label="Login">
                            Login
                        </button>
                    ) : (
                        <>
                            {showWelcomeMessage && (
                                <p className="welcome-message">Welcome, {user.username}</p>
                            )}
                            <button className="login-button" onClick={handleLogout} aria-label="Logout">
                                Logout
                            </button>
                        </>
                    )}
            </nav>
        </>
    );
}