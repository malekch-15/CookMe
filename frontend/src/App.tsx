import './App.css'
import {useEffect, useState} from "react";
import axios from "axios";
import {Recipe} from "./Model/Recipe.ts";
import Home from "./Home.tsx";
import {Route, Routes} from "react-router-dom";
import Details from "./Details/Details.tsx";
import Header from "./Header.tsx";
import Footer from "./Footer.tsx";


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
    const handelDelete=(id:string)=>{
        axios.delete(`api/cookMe/${id}`)
            .then(()=>{
               setRecipe(prevState => prevState.filter(recipe=>recipe.id!==id))
            })
            .catch(error=>{
                console.error(error)
            })

    }

    return (
        <>
            <div>
                <Header/>
                <Routes>
                    <Route path={"/"} element={ <Home recipe={recipe} onDelete={handelDelete}/>}/>
                    <Route path={"/details/:id"} element={<Details/>}/>
                </Routes>
                <Footer/>
            </div>
        </>
    )
}

export default App
