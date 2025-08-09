import { Routes, Route } from "react-router-dom"
import Home from "../pages/Home";
import Dashboard from "../pages/Dashboard";

function Routing(){
    return(
        <Routes>
            <Route path="/" element={<Home />}></Route>
            <Route path="/dashboard" element={<Dashboard />}></Route>
        </Routes>
    )
}

export default Routing;