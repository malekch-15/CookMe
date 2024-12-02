import './App.css'
import {useEffect, useState} from "react";
import axios from "axios";
import {Recipe} from "./Model/Recipe.ts";
import Home from "./Home/Home.tsx";
import {Route, Routes} from "react-router-dom";
import Header from "./Home/Header.tsx";
import Footer from "./Home/Footer.tsx";
import AddRecipe from "./AddRecipe.tsx";
import DetailsPage from "./Details/DetailsPage.tsx";


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
                    <Route path={"/details/:id/*"} element={<DetailsPage setRecipes={setRecipe}/>}/>
                    <Route path={"/savaRecipe"} element={<AddRecipe setRecipe={setRecipe}/>}/>

                </Routes>
                <Footer/>
            </div>
        </>
    )
}

export default App
