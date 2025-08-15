import { useNavigate } from "react-router-dom";

function Home() {

    const navigate = useNavigate();

    const handleClick = () =>{
        navigate("/dashboard")
    }

    return (
        <div>
            <div className="grid place-items-center grid-cols-1 md:grid-cols-3 rounded-lg-shadow gap-6 p-6 mt-15">
                <div className="bg-white col-span-2 ">
                    <h1 className="text-9xl font-bold">Your Personal AI Speech Coach</h1>
                </div>
                <div className="bg-white col-span-1">
                    <img src="./src/assets/microphone-with-hole-and-sound-wave-icon-set-sound-and-rhythms-vector.jpg" className="w-full h-full object-cover" alt="" />
                </div>
            </div>
            <p className="m-5 text-center text-xl">Upload your speech and get instant AI-powered feedback on clarity, pacing, word choice, and confidence.</p>
            <div className="flex justify-center">
            <button onClick={handleClick} className="flex items-center rounded-lg border border-slate-300 py-2 px-6 text-center text-lg transition-all shadow-sm hover:shadow-lg text-slate-600 hover:text-white hover:bg-slate-800 hover:border-slate-800 focus:text-white focus:bg-slate-800 focus:border-slate-800 active:border-slate-800 active:text-white active:bg-slate-800 disabled:pointer-events-none disabled:opacity-50 disabled:shadow-none" type="button">
                Get Started
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="w-4 h-4 ml-1.5">
                    <path fill-rule="evenodd" d="M16.28 11.47a.75.75 0 0 1 0 1.06l-7.5 7.5a.75.75 0 0 1-1.06-1.06L14.69 12 7.72 5.03a.75.75 0 0 1 1.06-1.06l7.5 7.5Z" clip-rule="evenodd" />
                </svg>
            </button>
            </div>
        </div>
    )
}

export default Home;