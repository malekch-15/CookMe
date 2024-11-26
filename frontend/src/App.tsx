import './App.css'
import {useEffect, useState} from "react";
import axios from "axios";
import {Recipe} from "./Model/Recipe.ts";
import Home from "./Home.tsx";

function App() {
    const [recipe, setRecipe] = useState<Recipe[]>([]);

    const fetchRecipe = () => {
        axios.get("/api/cookMe")
            .then(response => {
                setRecipe(response.data);
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            })
    };
    useEffect(fetchRecipe, [])
    return (
        <>
            <div>
                <h1>Recipe</h1>
              <Home recipe={recipe}/>
            </div>
        </>
    )
}

export default App
