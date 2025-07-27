interface ChatButtonProps {
    onClick: () => void;
}

export default function ChatButton({ onClick}: ChatButtonProps) {


    return (
        <button
            onClick={onClick}
            className="bg-blue-600 text-white rounded-full p-4 shadow-lg hover:bg-blue-700 transition"
        >
            💬
        </button>
    );
}