import Recorder from "../components/Recorder";
import History from "../components/History";

function Dashboard(){
    return(

        <div className="flex h-screen">
            <div className="w-3/4 bg-gray-100 p-4">
                <Recorder />
            </div>
            <div className="w-1/4 bg-gray-200 p-4">
                <History/>
            </div>
        </div>
    )
}

export default Dashboard;