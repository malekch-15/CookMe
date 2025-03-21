import './App.css';
import { useEffect, useState } from "react";
import axios from "axios";
import { Recipe } from "./Model/Recipe.ts";
import Home from "./Home/Home.tsx";
import { Route, Routes } from "react-router-dom";
import Header from "./Header.tsx";
import Footer from "./Footer.tsx";
import AddRecipe from "./Recipe/AddRecipe.tsx";
import DetailsPage from "./Details/DetailsPage.tsx";
import WishList from "./Home/WishList.tsx";
import { BaseIngredient } from "./Model/BaseIngredient.ts";
import Ingredient from "./Ingredient/Ingredient.tsx";
import {AppUser} from "./Model/AppUser.ts";
import ProtectedRoute from "./ProtectedRoute.tsx";
import UserIngredient from "./Ingredient/UserIngredient.tsx";
import MealPlan from "./MealPlan.tsx";

    function App() {
        const [recipes, setRecipes] = useState<Recipe[]>([]);
        const [ingredients, setIngredients] = useState<BaseIngredient[]>([]);
        const [newIngredient, setNewIngredient] = useState<BaseIngredient>({ id: "", name: "" });
        const [user, setUser] = useState<AppUser>();

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
            if (!user) return ;


            const recipe = recipes.find(r => r.id === id);
            if (!recipe) return;

            if (user.favorites.some(fav => fav.id === id)) {
                // Remove from favorites
                axios.delete(`/api/cookMe/users/${recipe.id}/favorite`,{data
                :user.id,headers:{"Content-Type":"text/plain"}})
                    .then(() => {
                        setUser(prevUser => prevUser?{
                            ...prevUser,
                            favorites: prevUser.favorites.filter(fav => fav.id !== id)}:prevUser);

                    })
                    .catch(error => console.error("Error removing from favorites:", error));
            } else {
                // Add to favorites
                axios.post(`/api/cookMe/user/favorites/${recipe.id}`,user.id,{headers:{"Content-Type":"text/plain"}})
                    .then(() => {
                        setUser(prevUser => prevUser?{
                            ...prevUser,
                            favorites: [...prevUser.favorites, recipe]
                        }:prevUser);
                    })
                    .catch(error => console.error("Error adding to favorites:", error));
            }
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

        const loadCurrentUser = () => {
            axios.get("/api/users/me")
                .then((response) => {
                    setUser(response.data);
                })
                .catch((error) => {
                    console.error(error);
                    setUser(undefined);
                });
        };

        useEffect(() => {
            loadCurrentUser();
        }, []);

        return (
            <>
                <Header user={user} />
                <Routes>

                    <Route path="/" element={
                        <Home
                            recipe={recipes}
                            onDelete={handleDelete}
                            onToggleWishlist={handleToggleWishList}
                            user={user}
                        />
                    } />
                    <Route path="/details/:id/*" element={
                        <DetailsPage
                            setRecipes={setRecipes}
                            ingredient={ingredients}
                        />

                    } />
                    <Route path="/WishList" element={<WishList recipe={recipes} onToggleWishlist={handleToggleWishList} onDelete={handleDelete} user={user} />} />
                    <Route element={<ProtectedRoute user={user} />}>
                        <Route path="/meal/:mealName/*" element={<DetailsPage setRecipes={setRecipes} ingredient={ingredients} />} />
                        <Route path="/mealPlan" element={<MealPlan user={user} setRecipes={setRecipes} />} />
                        <Route path="/New_Recipe" element={<AddRecipe setRecipe={setRecipes} ingredient={ingredients} newIngredient={newIngredient} onAddIngredient={handleAddIngredient} />} />
                        <Route path="/Ingredient" element={<Ingredient ingredient={ingredients} onAddIngredient={handleAddIngredient} setIngredient={setIngredients} />} />
                        <Route path="/UserIngredient" element={<UserIngredient  user={user} ingredient={ingredients} setUser={setUser}  onAddIngredient={handleAddIngredient}/>} />
                    </Route>
                </Routes>
                <Footer />
            </>
        );
    }
    export default App;