import { Routes, Route } from "react-router-dom"
import Home from "../pages/Home";
import Dashboard from "../pages/Dashboard";
import LogIn from "../components/LogIn";
import SignUp from "../components/SignUp";
import Authentication from "../components/Authentication";

function Routing(){
    return(
        <Routes>
            <Route path="/" element={<Home />}></Route>
            <Route path="/dashboard" element={<Authentication>
                                                <Dashboard />
                                            </Authentication>}></Route>
            <Route path="/login" element={<LogIn />}></Route>
            <Route path="/signup" element={<SignUp />}></Route>
        </Routes>
    )
}

export default Routing;