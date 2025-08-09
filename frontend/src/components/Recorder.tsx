import { useRef, useState } from "react";
import Analyzer from "./Analyzer";
function Recorder(){

  const [playing, setPlaying] = useState(false);
  const [audioBlob, setaudioBlob] = useState(null);
  const [seconds, setSeconds] = useState(0)

  const mediaStream = useRef<MediaStream>(null)
  const mediaRecorder = useRef<MediaRecorder>(null)
  const chunks = useRef([])

    const startRecording = async () =>{
        setPlaying(true)
        setSeconds(0)
        
        try {
          const stream = await navigator.mediaDevices.getUserMedia({audio: true})
          mediaStream.current = stream
          mediaRecorder.current = new MediaRecorder(stream, { mimeType: "audio/webm" })
          
          const timer = setInterval(() => {
          setSeconds(prev => prev + 1);
          }, 1000);

          mediaRecorder.current.start(); 

          mediaRecorder.current.ondataavailable = (e) =>{
          chunks.current.push(e.data)
          }

          mediaRecorder.current.onstop = () =>{
            const recordedBlob = new Blob(chunks.current,{type: "audio/webm"})
            setaudioBlob(recordedBlob)
            chunks.current = []
            clearTimeout(timer)
          }

        } catch (error) {
          console.log(error)
        }

    };

    const stopRecording= () =>{
      if(mediaRecorder.current){
        mediaRecorder.current.stop()
        mediaStream.current?.getTracks().forEach(track => {track.stop()
        });
      }
      setPlaying(false)
    }

    return(
      <div className="mt-10 text-center">
        <h1>Record your audio / speech</h1>
        {playing ? <div className="mt-4">
          <h1>Timer: {seconds} seconds</h1>
          <button type="button" onClick={stopRecording} className="bg-red-600 font-medium px-5 py-2.5 text-sm">Stop Recording</button>
        </div> :
        <button type="button" onClick={startRecording} className="bg-green-600 font-medium px-5 py-2.5 text-sm mt-10">Start Recording</button>}
      {/* { recordedAudio && <AudioPlayer recordedAudio={recordedAudio}/>} */}
      { audioBlob && <Analyzer audioBlob={audioBlob}/>}
      </div>
    ) 
}

export default Recorder