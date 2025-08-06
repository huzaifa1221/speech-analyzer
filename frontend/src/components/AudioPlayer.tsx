import { useEffect, useState } from "react";

type Props = {
  recordedAudio: Blob;
};

const AudioPlayer= ({recordedAudio}: Props) =>{

    const [recordedUrl, setRecordedUrl] = useState("")

    useEffect(()=>{
        const url = URL.createObjectURL(recordedAudio)
        setRecordedUrl(url)
    }, [])


    return(
        <audio className="block mx-auto mt-10"controls src={recordedUrl}></audio>
    )

}

export default AudioPlayer