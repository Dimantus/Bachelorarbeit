import { useState } from 'react';
import ChatButton from './ChatButton';
import ChatWindow from './ChatWindow';
import { AnimatePresence, motion } from 'framer-motion';

export default function ChatWidget() {
        const [showButton, setShowButton] = useState(true);
        const [showWindow, setShowWindow] = useState(false);

        const handleButtonExit = () => {
            setShowWindow(true);
        };
        const handleWindowExit = () => {
            setShowButton(true);
        };

        return (
            <div className="fixed bottom-4 right-4 z-50">
                <AnimatePresence onExitComplete={handleWindowExit}>
                    {showWindow && (
                        <motion.div
                            key="chat-window"
                            className="absolute bottom-0 right-0"
                            initial={{ opacity: 0, y: 30 }}
                            animate={{ opacity: 1, y: 0 }}
                            exit={{ opacity: 0, y: 30 }}
                            transition={{ duration: 0.3 }}
                        >
                            <ChatWindow onClick={() => setShowWindow(false)} />
                        </motion.div>
                    )}
                </AnimatePresence>

                <AnimatePresence onExitComplete={handleButtonExit}>
                    {showButton && (
                        <motion.div
                            key="chat-button"
                            className="absolute bottom-0 right-0"
                            initial={{ opacity: 0, scale: 0.8 }}
                            animate={{ opacity: 1, scale: 1 }}
                            exit={{ opacity: 0, scale: 0.8 }}
                            transition={{ duration: 0.2 }}
                        >
                            <ChatButton onClick={() => setShowButton(false)} />
                        </motion.div>
                    )}
                </AnimatePresence>
            </div>
        );
    }

