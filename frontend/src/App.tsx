import './App.css';
import { useEffect, useState } from "react";
import axios from "axios";
import { Recipe } from "./Model/Recipe.ts";
import Home from "./Home/Home.tsx";
import { Route, Routes } from "react-router-dom";
import Header from "./Home/Header.tsx";
import Footer from "./Home/Footer.tsx";
import AddRecipe from "./AddRecipe.tsx";
import DetailsPage from "./Details/DetailsPage.tsx";
import WishList from "./WishList.tsx";
import { Status } from "./Model/Status.ts";
import { BaseIngredient } from "./Model/BaseIngredient.ts";
import Ingredient from "./Ingredient.tsx";

function App() {
    const [recipes, setRecipes] = useState<Recipe[]>([]);
    const [ingredients, setIngredients] = useState<BaseIngredient[]>([]);
    const [newIngredient, setNewIngredient] = useState<BaseIngredient>({ id: "", name: "" });

    const fetchRecipes = () => {
        axios.get("/api/cookMe")
            .then(response => setRecipes(response.data))
            .catch(error => console.error("Error fetching recipes:", error));
    };

    useEffect(fetchRecipes, []);

    const handleDelete = (id: string) => {
        axios.delete(`/api/cookMe/${id}`)
            .then(() => setRecipes(prev => prev.filter(recipe => recipe.id !== id)))
            .catch(error => console.error("Error deleting recipe:", error));
    };

    const handleToggleWishList = (id: string) => {
        const recipe = recipes.find(r => r.id === id);
        if (!recipe) return;

        const updatedStatus: Status = recipe.status === "FAVORITE" ? "NOT_FAVORITE" : "FAVORITE";
        const updatedRecipe = { ...recipe, status: updatedStatus };

        axios.put(`/api/cookMe/update/${id}`, updatedRecipe)
            .then(response => setRecipes(prev => prev.map(r => r.id === id ? response.data : r)))
            .catch(error => console.error("Error toggling wishlist:", error));
    };

    const handleAddIngredient = async (name: string): Promise<BaseIngredient> => {
        try {
            const response = await axios.post(`/api/ingredient`, { id: "", name });
            const addedIngredient = response.data;

            setIngredients(prev => [...prev, addedIngredient]);
            setNewIngredient(addedIngredient);

            console.log("Ingredient added:", addedIngredient);
            return addedIngredient;
        } catch (error) {
            console.error("Error adding ingredient:", error);
            alert("Failed to add the ingredient.");
            throw new Error("Ingredient not saved.");
        }
    };

    const fetchIngredients = () => {
        axios.get("/api/ingredient")
            .then(response => setIngredients(response.data))
            .catch(error => console.error("Error fetching ingredients:", error));
    };

    useEffect(fetchIngredients, []);
    const [user,setUser]=useState<string|null>( null)


        const loadCurrentUser = () => {
            axios.get("/api/users/me")
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
            <Header user={user}/>

            <Routes>
                <Route path="/" element={
                    <Home
                        recipe={recipes}
                        onDelete={handleDelete}
                        onToggleWishlist={handleToggleWishList}
                    />
                }/>
                <Route path="/details/:id/*" element={
                    <DetailsPage
                        setRecipes={setRecipes}
                        ingredient={ingredients}
                    />
                }/>
                <Route path="/WishList" element={
                    <WishList
                        recipe={recipes}
                        onToggleWishlist={handleToggleWishList}
                        onDelete={handleDelete}
                    />
                }/>
                <Route path="/New_Recipe" element={
                    <AddRecipe
                        setRecipe={setRecipes}
                        ingredient={ingredients}
                        newIngredient={newIngredient}
                        onAddIngredient={handleAddIngredient}
                    />
                }/>
                <Route path="/Ingredient" element={
                    <Ingredient
                        ingredient={ingredients}
                        onAddIngredient={handleAddIngredient}
                        setIngredient={setIngredients}
                    />
                }/>
            </Routes>
            <Footer/>
        </div>
    );
}

export default App;