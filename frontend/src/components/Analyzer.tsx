import { useEffect, useState } from "react";

type better_words_sentences = {
    original: string,
    suggestion: string
}

type Data = {
    confidence_analysis: {
        score: number,
        feedback: String
    },
    improvement_suggestions: string[],
    better_words_sentences: better_words_sentences[],
    overall_rating: number
}


function Analyzer({ transcript }) {

    const [loading, setLoading] = useState(true)
    const url = `${import.meta.env.VITE_BACKEND_URL}/analyze`
    const [data, setData] = useState<Data>(null)


    useEffect(()=>{
        const analyzeTranscript = async () => {
            const response = await fetch(url, {
                method: "POST",
                headers: { "Content-Type": "text/plain" },
                body: transcript
            })
            const output = await response.json();
            setLoading(false)
            console.log(output);
            setData(output)
        }

        analyzeTranscript()
    },[])

    if (loading) return <h1>Analyzing audio...Please wait</h1>

    return (
        <div className="max-w-250 mx-auto">
            <div className="max-w-4xl mx-auto p-6 bg-gray-50 rounded-2xl shadow-lg space-y-6">
                <div className="bg-white rounded-xl shadow p-4">
                    <h2 className="text-2xl font-semibold text-gray-800 mb-4">Speech Analysis Summary</h2>
                    <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 text-lg">
                        <p className="bg-blue-100 px-4 py-2 rounded-lg">
                            <span className="font-semibold">Score:</span> {data.confidence_analysis.score}
                        </p>
                        <p className="bg-green-100 px-4 py-2 rounded-lg">
                            <span className="font-semibold">Overall Rating:</span> {data.overall_rating}
                        </p>
                        <p className="bg-yellow-100 px-4 py-2 rounded-lg sm:col-span-3">
                            <span className="font-semibold">Feedback:</span> {data.confidence_analysis.feedback}
                        </p>
                    </div>
                </div>
            </div>
            <div className="bg-white rounded-xl shadow p-4">
                <h2 className="text-xl font-semibold text-gray-800 mb-3">Improvement Suggestions</h2>
                <ul className="list-disc list-inside space-y-2 text-gray-700">
                    {data.improvement_suggestions.map((item, index) => (
                        <li key={index} className="bg-gray-100 px-3 py-2 rounded-lg">{item}</li>
                    ))}
                </ul>
            </div>
            <div className="bg-white rounded-xl shadow p-4">
                <h2 className="text-xl font-semibold text-gray-800 mb-3">Better Words & Sentences</h2>
                <ul className="space-y-4">
                    {data.better_words_sentences.map((item, index) => (
                        <li key={index} className="border border-gray-200 rounded-lg p-3">
                            <p className="text-gray-600"><strong>Original:</strong> {item.original}</p>
                            <p className="text-gray-800"><strong>Suggestion:</strong> {item.suggestion}</p>
                        </li>
                    ))}
                </ul>
            </div>
        </div>

    )
}

export default Analyzer;