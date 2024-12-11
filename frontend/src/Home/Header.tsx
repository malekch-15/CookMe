import "./Header.css"
import {Link} from "react-router-dom";
import axios from "axios";
import {useEffect, useState} from "react";
export default function Header() {
    const [user,setUser]=useState<string|null>( null)

    const login = () => {
        const host = window.location.host === 'localhost:5173' ? 'http://localhost:8080' : window.location.origin;
        window.open(`${host}/oauth2/authorization/github`, '_self');
    };

    const logout = () => {
        const host = window.location.host === 'localhost:5173' ? 'http://localhost:8080' : window.location.origin;
        window.open(`${host}/logout`, '_self');
    };

        const loadCurrentUser = () => {
            axios.get("http://localhost:8080api/users/me")
                .then((response) => {
                        console.log(response.data)
                        setUser(response.data)
                    }
                )
        }
    useEffect(() => {
        loadCurrentUser()
    }, []);
    return (
        <div>
            <div className="auth-buttons">

                <button onClick={login}>Login</button>
                <>
                    <p>Welcome, {user}</p>
                    <button onClick={logout}>Logout</button>
                </>

            </div>
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