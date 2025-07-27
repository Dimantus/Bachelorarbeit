import ChatMessage from "./ChatMessage.tsx";
import {useEffect, useRef, useState} from "react";
import UserMessage from "./UserMessage.tsx";

interface ChatWindowProps {
    onClick: () => void;
}

interface MessageWithType {
    role: boolean;
    message: string;
}

export default function ChatWindow({onClick}: ChatWindowProps) {

    const chatContainerRef = useRef<HTMLDivElement | null>(null);
    const [chat, setChat] = useState<MessageWithType[]>(() =>
        JSON.parse(localStorage.getItem("Chat") || '[]'),
    );
    const [inputValue, setInputValue] = useState("");

    useEffect(() => {
        localStorage.setItem("Chat", JSON.stringify(chat));
    }, [chat]);
    useEffect(() => {
        chatContainerRef.current?.scrollIntoView({behavior: "smooth"});
    }, [chat]);

    const getStream = async (input: String) => {
        const response = await fetch("/chat/webflux-stream", {
            method: "POST",
            body: JSON.stringify({
                param: input
            })
        })

        if (!response.body) return;
        const reader = response.body.getReader();
        const decoder = new TextDecoder("utf-8");
        let partialText = "";
        const tempChat: MessageWithType[] = chat;
        tempChat.push({role: true, message: inputValue})
        tempChat.push({role: false, message: ""});

        while (true) {
            const { value, done } = await reader.read();
            if (done) break;
            const chunk = decoder.decode(value, { stream: true });
            partialText += chunk;
            tempChat[tempChat.length - 1] = {role: false, message: partialText};
            setChat([...tempChat]);
        }
    };

    //Implementation ohne Stream
    // const getStream = async (input: String) => {
    //     try {
    //         const response = await fetch("http://localhost:8080/chat/mvc", {
    //             method: "POST",
    //             body: JSON.stringify({
    //                 param: input
    //             })
    //         })
    //         const responseText = await response.text()
    //
    //         setChat((prev) => [...prev, {role: false, message: responseText}]);
    //     } catch (error) {
    //         console.log(error)
    //     }
    // }

    return (
        <div className="w-80 h-96 bg-white rounded-xl shadow-xl p-4 flex flex-col">
            <div className="flex justify-between items-center border-b pb-2 mb-2">
                <h2 className="text-lg font-semibold">ReACD Chatbot</h2>
                <button onClick={onClick} className="text-gray-500 hover:text-black text-xl">
                    ✕
                </button>
            </div>
            <div className="flex flex-col flex-1 overflow-y-auto min-h-0 scrollbar-hide">

                {chat.map((msg, i) => {
                    if (msg.role) {
                        return <UserMessage key={i} message={msg.message}/>
                    } else if (!msg.role) {
                        return <ChatMessage key={i} message={msg.message}/>
                    }
                })}
                <div ref={chatContainerRef}/>
            </div>
            <input
                type="text"
                placeholder="Nachricht eingeben..."
                className="mt-2 border rounded px-2 py-1 text-sm"
                value={inputValue}
                onChange={(e) => setInputValue(e.target.value)}
                onKeyDown={(e) => {
                    if (e.key === "Enter" && inputValue.trim() !== "") {
                        setChat((prev) => [...prev, {role: true, message: inputValue}]);
                        getStream(inputValue)
                        setInputValue("");
                    }
                }}
            />
        </div>
    );
}