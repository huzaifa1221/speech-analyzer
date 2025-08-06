import { useAuth } from "../provider/AuthProvider"

function SignUp(){

    type User={
        name: string,
        username: string,
        password: string
    }

    const {login} = useAuth()

    const createUser = async (data: User) =>{
        const url = `${import.meta.env.VITE_BACKEND_URL}/signup`
        console.log(url)
        const response = await fetch(url,{
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(data)
        })
        if(response.ok){
            alert("User created")
            login({username: data.username, password: data.password});
        }
        else{
            alert("User creation failed !")
        }
    }

    const handleSubmit = (formdata: FormData) =>{
        const data: User = {
            name: formdata.get("name") as string,
            username: formdata.get("username")as string,
            password: formdata.get("password")as string
        }
        createUser(data);
    }

    return(
        <div className="flex min-h-full flex-col justify-center px-6 py-12 lg:px-8">
                <h2 className="mt-10 text-center text-2xl/9 font-bold tracking-tight text-gray-900">SignUp</h2>
                <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
                    <form action={handleSubmit} className="space-y-6">
                        <div>
                            <label htmlFor="name" className="block text-sm/6 font-medium text-gray-900">Name</label>
                            <div className="mt-2">
                                <input id="name" type="name" name="name" required autoComplete="name" className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6" />
                            </div>
                        </div>

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
                            <button type="submit" className="flex w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm/6 font-semibold text-white shadow-xs hover:bg-indigo-500 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">Create an account</button>
                        </div>
                    </form>
                </div>
            </div>
    )
}

export default SignUp;