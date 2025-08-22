import { useRef, useState } from "react";
import PCM16_JS from '../assets/code/pcm16.js?raw';
import Analyzer from "./Analyzer";

function Recorder() {

  const [start, setStart] = useState(false)
  const [seconds, setSeconds] = useState(0)
  const [transcript, setTranscript] = useState(null)
  const timerRef = useRef(null);
  const audioContextRef = useRef<AudioContext | null>(null);
  const processorNodeRef = useRef<AudioWorkletNode | null>(null);
  const audioInputNodeRef = useRef<MediaStreamAudioSourceNode | null>(null);
  const streamRef = useRef<MediaStream | null>(null);
  const wsRef = useRef(null)
  const [indicator, setIndicator] = useState(false)


  const registerAudioProcessor = async (audioContext: AudioContext, javascript: string) => {
    return new Promise<void>((resolve, reject) => {
      const blob = new Blob([javascript], { type: 'application/javascript' });
      const url = URL.createObjectURL(blob);

      audioContext.audioWorklet.addModule(url)
        .then(() => {
          URL.revokeObjectURL(url);
          resolve();
        })
        .catch(reject)
    });
  }

  const startRecording = async () => {
    wsRef.current = new WebSocket("ws://localhost:8080/ws/transcribe");
    wsRef.current.binaryType = "arraybuffer";
    audioContextRef.current = new AudioContext({ sampleRate: 16000 });
    await registerAudioProcessor(audioContextRef.current, PCM16_JS);
    streamRef.current = await navigator.mediaDevices.getUserMedia({ audio: true })
    audioInputNodeRef.current = audioContextRef.current.createMediaStreamSource(streamRef.current);
    processorNodeRef.current = new AudioWorkletNode(audioContextRef.current, 'pcm16');
    audioInputNodeRef.current.connect(processorNodeRef.current);

    wsRef.current.onclose = () => {
      console.log("websocket closed")
      wsRef.current.close()
      wsRef.current = null
    }

    wsRef.current.onmessage = event => {
      console.log(event.data)
      setTranscript(event.data)
      setIndicator(false)    
    }

    processorNodeRef.current.port.onmessage = (event) => {
      const pcmChunk = event.data as ArrayBuffer;
      if (wsRef.current && wsRef.current.readyState === WebSocket.OPEN) {
        wsRef.current.send(pcmChunk);
      }
    }

    timerRef.current = setInterval(() => {
      setSeconds(prev => prev + 1);
    }, 1000);

    setStart(true)
  }

  const stopRecording = async () => {
    if (streamRef.current.getTracks) {
      streamRef.current.getTracks().forEach(track => track.stop());
      streamRef.current = null;
    }

    audioInputNodeRef.current.disconnect();
    audioInputNodeRef.current = null;

    processorNodeRef.current.port.onmessage = null;
    processorNodeRef.current.disconnect();
    processorNodeRef.current = null;

    await audioContextRef.current.close();
    audioContextRef.current = null;

    clearTimeout(timerRef.current);
    timerRef.current = null;
    setSeconds(0);
    setStart(false)
    setIndicator(true)

    console.log("Stopped recording, context closed, processor disabled");
  };

  return (
    <div className="mt-10 text-center">
      <h1 className="text-2xl font-semibold text-gray-800 mb-4">Record your audio / speech</h1>
      {!start && <button type="button" onClick={startRecording} className="bg-green-600 font-medium px-5 py-2.5 text-sm mt-10">Start Recording</button>}
      {start && <div className="mt-4">
        <h1>Timer: {seconds} seconds</h1>
        <button type="button" onClick={stopRecording} className="bg-red-600 text-gray-800font-medium px-5 py-2.5 text-sm">Stop Recording</button>
      </div>}
      {indicator && <p>Please wait a moment, Preparing your audio</p>}
      {transcript && <Analyzer transcript={transcript}></Analyzer>}
    </div>
  )
}

export default Recorder