import {motion} from "framer-motion";

interface ChatMsgProps {
    message: string;
}

export default function UserMessage({ message }: ChatMsgProps) {
    return (
        <motion.div
            className="self-end w-auto max-w-[95%] m-1 p-2 px-4 bg-gray-100 text-black rounded-tl-2xl rounded-b-2xl break-words text-left"
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.3 }}
        >
            {message}
        </motion.div>
    );
}