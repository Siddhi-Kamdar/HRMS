import React, { useEffect, useState } from "react";
import ReactDOM from "react-dom";
import "../index.css";
import {
    getNotifications,
    type Notification
} from "../services/notificationService";

export const NotificationDialog = ({ onClose }: any) => {
    const [notifications, setNotifications] = useState<Notification[]>([]);
    useEffect(() => {
        loadJobs();
    }, []);

    const loadJobs = async () => {
        const data = await getNotifications();
        setNotifications(data);
    }

    return (
        <div className="overlay">
            <div className="dialog overflow-scroll">
                <h3>Notification: </h3> 
                {
                    
                    notifications.map(notification =>(
                        <div className="border rounded m-2" key={notification.notificationId} >
                            {notification.message}
                        </div>
                    ))
                }
                <button onClick={onClose}>❌</button>
            </div>
        </div>
    );
}
