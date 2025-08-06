import { useEffect, useState } from "react";
import { useAuth } from "../provider/AuthProvider";

type Props = {
  audioBlob: Blob,
  title: string
};

function Analyzer({audioBlob, title}: Props){

    const [loading, setLoading] = useState(true)
    const {user} = useAuth();
    const url = `${import.meta.env.VITE_BACKEND_URL}/analyze`

    const formData = new FormData();
    formData.append("file", audioBlob, "recording.webm");
    formData.append("username", user.username)
    formData.append("title", title)

    const analyzeAudio = async () =>{

        const response = await fetch(url,{
            method: "POST",
            body: formData
        })
    }

    useEffect(() => {
    analyzeAudio();
    }, [])

    if(loading) return <h1>Analyzing audio...Please wait</h1>

    return(
        <p></p>

    )
}

export default Analyzer;