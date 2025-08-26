import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

function Home() {
    const url = `${import.meta.env.VITE_BACKEND_URL}`

    useEffect(() => {

        const healthcheck = async () => {
            try {
                const response = await fetch(url)
                const data = await response.json()
                console.log(data)
            } catch (error) {
                console.error("healthcheck failed:", error);
            }
        }
        healthcheck();
    }, [])

    const navigate = useNavigate();

    const handleClick = () => {
        navigate("/dashboard")
    }

    return (
        <div className="h-screen bg-linear-to-b from-white to-blue-200">
            <div className="max-w-250 mx-auto pt-20">
                <div className="grid place-items-center grid-cols-1 md:grid-cols-3 rounded-lg-shadow gap-6 p-6">
                    <div className="col-span-2 ">
                        <h1 className="text-8xl font-bold">Your Personal AI Speech Coach</h1>
                    </div>
                    <svg xmlns="http://www.w3.org/2000/svg" fill="#000000" height="200px" width="800px" version="1.1" viewBox="0 0 512 512" enable-background="new 0 0 512 512">
                        <g>
                            <g>
                                <path d="m439.5,236c0-11.3-9.1-20.4-20.4-20.4s-20.4,9.1-20.4,20.4c0,70-64,126.9-142.7,126.9-78.7,0-142.7-56.9-142.7-126.9 0-11.3-9.1-20.4-20.4-20.4s-20.4,9.1-20.4,20.4c0,86.2 71.5,157.4 163.1,166.7v57.5h-23.6c-11.3,0-20.4,9.1-20.4,20.4 0,11.3 9.1,20.4 20.4,20.4h88c11.3,0 20.4-9.1 20.4-20.4 0-11.3-9.1-20.4-20.4-20.4h-23.6v-57.5c91.6-9.3 163.1-80.5 163.1-166.7z" />
                                <path d="m256,323.5c51,0 92.3-41.3 92.3-92.3v-127.9c0-51-41.3-92.3-92.3-92.3s-92.3,41.3-92.3,92.3v127.9c0,51 41.3,92.3 92.3,92.3zm-52.3-220.2c0-28.8 23.5-52.3 52.3-52.3s52.3,23.5 52.3,52.3v127.9c0,28.8-23.5,52.3-52.3,52.3s-52.3-23.5-52.3-52.3v-127.9z" />
                            </g>
                        </g>
                    </svg>
                </div>
                <p className="m-5 text-center text-gray-700 py-10 text-xl">Upload your speech and get instant AI-powered feedback on clarity, pacing, word choice, and confidence.</p>
                <div className="flex justify-center">
                    <button onClick={handleClick} className="flex items-center rounded-lg border cursor-pointer border-slate-300 py-2 px-6 text-center text-lg transition-all shadow-sm hover:shadow-lg text-slate-600 hover:text-white hover:bg-slate-800 hover:border-slate-800 focus:text-white focus:bg-slate-800 focus:border-slate-800 active:border-slate-800 active:text-white active:bg-slate-800 disabled:pointer-events-none disabled:opacity-50 disabled:shadow-none" type="button">
                        Get Started
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="w-4 h-4 ml-1.5">
                            <path fill-rule="evenodd" d="M16.28 11.47a.75.75 0 0 1 0 1.06l-7.5 7.5a.75.75 0 0 1-1.06-1.06L14.69 12 7.72 5.03a.75.75 0 0 1 1.06-1.06l7.5 7.5Z" clip-rule="evenodd" />
                        </svg>
                    </button>
                </div>
            </div>
        </div>
    )
}

export default Home;