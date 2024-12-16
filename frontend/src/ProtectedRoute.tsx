import {Navigate, Outlet} from "react-router-dom";
import {AppUser} from "./Model/AppUser.ts";


type ProtectedRouteProps = {
    user: AppUser|undefined ;
}

export default function ProtectedRoute(props: ProtectedRouteProps){
if (props.user) {
    const isAuthenticated = props.user.username !== "anonymousUser";

    return(
        isAuthenticated ? <Outlet /> : <Navigate to={"/"} />
    )
}}