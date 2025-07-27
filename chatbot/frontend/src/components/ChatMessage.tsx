import {motion} from "framer-motion";

interface ChatMsgProps {
    message: string;
}

export default function ChatMessage({ message }:ChatMsgProps) {
    return (
        <motion.div
            className="self-start w-auto max-w-[95%] m-1 p-2 px-4 bg-[#0d3d66] text-white rounded-tr-2xl rounded-b-2xl break-words text-left"
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.3 }}
        >
            {message}
        </motion.div>
    );
}