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
import WishList from "./WishList.tsx";
import {Status} from "./Model/Status.ts";
import {BaseIngredient} from "./Model/BaseIngredient.ts";
import Ingredient from "./Ingredient.tsx";




function App() {
    const [recipes, setRecipe] = useState<Recipe[]>([]);
    const[ingredient,setIngredient]=useState<BaseIngredient[]>([])
    const[newingredient,setNewIngredient]=useState<BaseIngredient>({
        id:"",
        name:""
        }
    )


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
    const handelToggelWishList=(id:string)=>{
        const recipe= recipes.find((r)=>r.id===id);
        if(!recipe) return;
        const updatedStatus:Status=recipe.status==="FAVORITE"?"NOT_FAVORITE":"FAVORITE";
     const updateRecipe={...recipe,status: updatedStatus};
     axios.put(`/api/cookMe/update/${id}`,updateRecipe)
         .then((response)=>
         setRecipe((prev)=>prev.map((p)=>p.id===id?response.data:p))
         )
    }

const handelAddIngredient=(newIngredient:BaseIngredient)=>{
        axios.post(`/api/ingredient`, newIngredient)
            .then((response)=>{
                setNewIngredient(response.data)
                setIngredient(prev=>[...prev,response.data])
            }).catch((error)=>console.log("no ingredient saved"+error))
}
    const fetchIngredient=()=>{
        axios.get("/api/ingredient")
            .then((response)=>{
                setIngredient(response.data)
            }).catch((error)=>console.log("no ingredient"+error))
    }
    useEffect(fetchIngredient, [])
    return (
        <>
            <div>
                <Header/>
                <Routes>
                    <Route path={"/"} element={ <Home recipe={recipes} onDelete={handelDelete} onToggleWishlist={handelToggelWishList}/>}/>
                    <Route path={"/details/:id/*"} element={<DetailsPage setRecipes={setRecipe} ingredient={ingredient}/>}/>
                    <Route path={"/WishList"} element={<WishList recipe={recipes} onToggleWishlist={handelToggelWishList} onDelete={handelDelete}/>}/>
                    <Route path={"/New_Recipe"} element={<AddRecipe setRecipe={setRecipe} ingredient={ingredient} newIngredient={newingredient}
                                                                    onAddIngredient={handelAddIngredient}/>}/>
                    <Route path={"/Ingredient"} element={<Ingredient ingredient={ingredient}/>}/>

                </Routes>
                <Footer/>
            </div>
        </>
    )
}

export default App
