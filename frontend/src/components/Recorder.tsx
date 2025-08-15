import { useRef, useState } from "react";
import Analyzer from "./Analyzer";
function Recorder(){

  const [playing, setPlaying] = useState(false);
  const [seconds, setSeconds] = useState(0)

  const mediaStream = useRef<MediaStream>(null)
  const mediaRecorder = useRef<MediaRecorder>(null)

    const startRecording = async () =>{
        setPlaying(true)
        setSeconds(0)
        const timer = setInterval(() => {
        setSeconds(prev => prev + 1);
        }, 1000);
        
        const ws = new WebSocket("ws://localhost:8080/ws/transcribe");
        ws.binaryType = "arraybuffer";

        ws.onmessage = event =>{
        console.log(event.data);
        }
        
        const stream = await navigator.mediaDevices.getUserMedia({audio: true})
        mediaStream.current = stream
        mediaRecorder.current = new MediaRecorder(stream, { mimeType: "audio/webm;codecs=pcm" })
        
        
        mediaRecorder.current.ondataavailable = async (e) =>{
          if (e.data.size > 0 && ws.readyState === WebSocket.OPEN) {
            const buffer = await e.data.arrayBuffer();
            console.log(buffer)
            ws.send(buffer);
          }else{
              console.log("websocket is not in the reasy state..")
          }
        }
        
        mediaRecorder.current.start(200); 

        mediaRecorder.current.onstop = () =>{
          ws.close()
          clearTimeout(timer)
          }
        }

    const stopRecording = () =>{
      if(mediaRecorder.current){
        mediaRecorder.current.stop()
        mediaStream.current?.getTracks().forEach(track => {track.stop()
        });
      }
      setPlaying(false)
    }

    return(
      <div className="mt-10 text-center">
        <h1 className="text-2xl font-semibold text-gray-800 mb-4">Record your audio / speech</h1>
        {playing ? <div className="mt-4">
          <h1>Timer: {seconds} seconds</h1>
          <button type="button" onClick={stopRecording} className="bg-red-600 text-gray-800font-medium px-5 py-2.5 text-sm">Stop Recording</button>
        </div> :
        <button type="button" onClick={startRecording} className="bg-green-600 font-medium px-5 py-2.5 text-sm mt-10">Start Recording</button>}
      {/* { recordedAudio && <AudioPlayer recordedAudio={recordedAudio}/>} */}
      {/* { audioBlob && <Analyzer audioBlob={audioBlob}/>} */}
      </div>
    ) 
}

export default Recorder