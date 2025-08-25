function Navbar() {
    
    return (
        <nav className="bg-white dark:bg-gray-900 border-gray-200 dark:border-gray-600">
            <div className="max-w-screen-xl flex flex-wrap items-center justify-between mx-auto p-4">
                <a href="/" className="flex items-center space-x-3 rtl:space-x-reverse">
                    <img src="https://flowbite.com/docs/images/logo.svg" className="h-8" alt="Flowbite Logo"/>
                        <span className="self-center text-2xl font-semibold whitespace-nowrap dark:text-white">Speech Analyzer</span>
                </a>
            </div>
        </nav>
    )
}

export default Navbar;