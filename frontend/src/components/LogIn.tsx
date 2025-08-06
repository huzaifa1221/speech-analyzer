import { useLocation } from "react-router-dom";
import { useAuth } from "../provider/AuthProvider";

function LogIn() {

    const {login} = useAuth();
    const location = useLocation();
    const message = location.state?.message;

    const handleSubmit = (formdata: FormData) =>{
        const data={
            username: formdata.get("username")as string,
            password: formdata.get("password")as string
        }
        login(data);
    }

    return (
        <>
           { message && <div className="flex items-center p-4 my-4 text-sm text-blue-800 border border-blue-300 rounded-lg bg-blue-50 dark:bg-gray-800 dark:text-blue-400 dark:border-blue-800" role="alert">
                <svg className="shrink-0 inline w-4 h-4 me-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 20 20">
                    <path d="M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5ZM9.5 4a1.5 1.5 0 1 1 0 3 1.5 1.5 0 0 1 0-3ZM12 15H8a1 1 0 0 1 0-2h1v-3H8a1 1 0 0 1 0-2h2a1 1 0 0 1 1 1v4h1a1 1 0 0 1 0 2Z"/>
                </svg>
                <span className="sr-only">Info</span>
                <div>
                    <span className="font-medium">{message}</span>
                </div>
            </div>}
            <div className="flex min-h-full flex-col justify-center px-6 py-12 lg:px-8">
                <h2 className="mt-10 text-center text-2xl/9 font-bold tracking-tight text-gray-900">Log in</h2>
                <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
                    <form action={handleSubmit} className="space-y-6">
                        <div>
                            <label htmlFor="username" className="block text-sm/6 font-medium text-gray-900">Username</label>
                            <div className="mt-2">
                                <input id="username" type="username" name="username" required autoComplete="username" className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6" />
                            </div>
                        </div>

                        <div>
                            <div className="flex items-center justify-between">
                                <label htmlFor="password" className="block text-sm/6 font-medium text-gray-900">Password</label>
                            </div>
                            <div className="mt-2">
                                <input id="password" type="password" name="password" required autoComplete="current-password" className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6" />
                            </div>
                        </div>

                        <div>
                            <button type="submit" className="flex w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm/6 font-semibold text-white shadow-xs hover:bg-indigo-500 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">Sign in</button>
                        </div>
                    </form>

                    <p className="mt-10 text-center text-sm/6 text-gray-500">
                        Don't have an account?
                        <a href="/signup" className="font-semibold text-indigo-600 hover:text-indigo-500"> SignUp</a>
                    </p>
                </div>
            </div>
        </>
    )
}

export default LogIn