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
import { BaseIngredient } from "./Model/BaseIngredient.ts";
import Ingredient from "./Ingredient.tsx";
import {AppUser} from "./Model/AppUser.ts";
import ProtectedRoute from "./Home/ProtectedRoute.tsx";

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
                axios.delete(`/api/cookMe/user/${user.id}/favorites/${id}`)
                    .then(() => {
                        setUser(prevUser => prevUser?{
                            ...prevUser,
                            favorites: prevUser.favorites.filter(fav => fav.id !== id)}:prevUser);

                    })
                    .catch(error => console.error("Error removing from favorites:", error));
            } else {
                // Add to favorites
                axios.post(`/api/cookMe/user/${user.id}/favorites/${recipe.id}`)
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
                    if (response.data && response.data.id) {
                        axios.get(`/api/cookMe/user/${response.data.id}/favorites`)
                            .then(favoritesResponse => {
                                setUser(prevUser => prevUser ? {
                                    ...prevUser,
                                    favorites: favoritesResponse.data // Update favorites
                                } : prevUser);
                            })
                            .catch(error => console.error("Error fetching favorites:", error));
                    }
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

                        <Route path="/New_Recipe" element={<AddRecipe setRecipe={setRecipes} ingredient={ingredients} newIngredient={newIngredient} onAddIngredient={handleAddIngredient} />} />
                        <Route path="/Ingredient" element={<Ingredient ingredient={ingredients} onAddIngredient={handleAddIngredient} setIngredient={setIngredients} />} />
                    </Route>
                </Routes>
                <Footer />
            </>
        );
    }
    export default App;