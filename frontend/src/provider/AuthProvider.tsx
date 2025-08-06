import { createContext, useContext, useState } from "react";
import { useNavigate } from "react-router-dom";

const AuthContext = createContext(null)
export const AuthProvider = ({children}) =>{

    const [user, SetUser] = useState({
                                       username: ""
                                    })

    type User={
        username: string,
        password: string
    }

    const navigate = useNavigate()

    const login = async(data: User) =>{
        const url = `${import.meta.env.VITE_BACKEND_URL}/login`
        console.log(url)
        const response = await fetch(url,{
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(data)
        })
        if(response.ok){
            SetUser({username: data.username})
            navigate("/dashboard")
        }
        else{
            alert("Failed to Log In !")
        }
    }

    const logout = () =>{
        SetUser({username: ""})
    }

    return(
        <AuthContext.Provider value={{ user, login, logout }}>{children}</AuthContext.Provider>
    )          
}

export const useAuth = ()=>{
    return useContext(AuthContext)
}