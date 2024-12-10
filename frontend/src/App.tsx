import './App.css'
import {useEffect, useState} from "react";
import axios from "axios";
import {Recipe} from "./Model/Recipe.ts";
import Home from "./Home/Home.tsx";
import { Route, Routes} from "react-router-dom";
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
    const [user, setUser] = useState<string>()
    const[ingredient,setIngredient]=useState<BaseIngredient[]>([])
    const[newIngredient,setNewIngredient]=useState<BaseIngredient>({
        id:"",
        name:""
        }
    )

    function login(){
        const host = window.location.host === 'localhost:5173' ? 'http://localhost:8080': window.location.origin

        window.open(host + '/oauth2/authorization/github', '_self')
    }
    function logout(){
        const host = window.location.host === 'localhost:5173' ? 'http://localhost:8080' : window.location.origin

        window.open(host + '/logout', '_self')
    }
    const loadUser = () => {
        axios.get("/api/users/me")
            .then(response => {
                console.log("User data:", response.data);
                setUser(response.data)
            })
            .catch(error => {
                console.error("Error fetching user data:", error);
                alert("Failed to load user. Please ensure you are logged in.");
            });
    };
    useEffect(() => {
        loadUser()
    }, []);

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

    const handelAddIngredient = async (name: string): Promise<BaseIngredient> => {
        try {
            const response = await axios.post(`/api/ingredient`, { id: "", name });
            const addedIngredient = response.data;

            // Update global ingredients and new ingredient
            setIngredient((prev) => [...prev, addedIngredient]);
            setNewIngredient(addedIngredient);

            console.log("Ingredient added:", addedIngredient);
            return addedIngredient; // Return the newly added ingredient
        } catch (error) {
            console.error("Error adding ingredient:", error);
            alert("Failed to add the ingredient. Please try again.");
            return  Promise.reject(new Error("no Ingredient saved"));
        }
    };

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
                {!user && <button onClick={login}>Login</button>}
                <p>{user}</p>
                {user && <button onClick={logout}>Logout</button>}
                <Routes>
                    <Route path={"/"} element={<Home recipe={recipes} onDelete={handelDelete}
                                                     onToggleWishlist={handelToggelWishList}/>}/>
                    <Route path={"/details/:id/*"}
                           element={<DetailsPage setRecipes={setRecipe} ingredient={ingredient}/>}/>
                    <Route path={"/WishList"}
                           element={<WishList recipe={recipes} onToggleWishlist={handelToggelWishList}
                                              onDelete={handelDelete}/>}/>
                    <Route path={"/New_Recipe"} element={<AddRecipe setRecipe={setRecipe} ingredient={ingredient}
                                                                    newIngredient={newIngredient}
                                                                    onAddIngredient={handelAddIngredient}/>}/>
                    <Route path={"/Ingredient"}
                           element={<Ingredient ingredient={ingredient} onAddIngredient={handelAddIngredient}
                                                setIngredient={setIngredient}/>}/>

                </Routes>
                <Footer/>
            </div>
        </>
    )
}

export default App
