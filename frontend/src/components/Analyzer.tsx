import { useEffect, useState } from "react";

type Props = {
  audioBlob: Blob;
};

function Analyzer({audioBlob}: Props){

    const [loading, setLoading] = useState(true)
    const url = `${import.meta.env.VITE_BACKEND_URL}/analyze`

    const analyzeAudio = async () =>{

        const response = await fetch(url,{
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: audioBlob
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