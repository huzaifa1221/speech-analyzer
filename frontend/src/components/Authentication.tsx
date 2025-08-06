import { Navigate } from "react-router-dom";
import { useAuth } from "../provider/AuthProvider";

function Authentication({ children }) {

    const { user } = useAuth()

    if (!user.username) {
        return (
            <Navigate to="/login" state={{ message: "Access Denied ! Please Log In." }} />
        )
    }
    return children;


}

export default Authentication;