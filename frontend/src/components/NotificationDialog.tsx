import React, { useEffect, useState } from "react";
import {
  getNotifications,
  type Notification,
  readNotification,
  readAllNotifications
} from "../services/notificationService";

export const NotificationDialog = ({ onClose }: any) => {
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadNotifications();
    document.body.style.overflow = "hidden";
    return () => {
      document.body.style.overflow = "auto";
    };
  }, []);

  const loadNotifications = async () => {
    try {
      const data = await getNotifications();
      setNotifications(data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleReadNotification = async (notification: Notification) => {
    await readNotification(notification.notificationId);
    loadNotifications();
  }

  const handleReadAllNotification = async() =>  {
    await readAllNotifications();
    loadNotifications();
  }

  return (
    <div className="modal show fade d-block edge" tabIndex={-1}>
      <div className="modal-dialog modal-dialog-centered edge">
        <div className="modal-content shadow border-0 edge">

          <div className="modal-header d-flex">
            <h5 className="modal-title fw-semibold mr-auto p-2">Notifications</h5>
            <button type="button" className="btn btn-default btn-outline-default p-2 edge" onClick={handleReadAllNotification}>Clear All</button>
            <button
              type="button"
              className="btn-close p-2 edge"
              onClick={onClose}
            ></button>
          </div>

          <div
            className="modal-body"
            style={{ maxHeight: "400px", overflowY: "auto" }}
          >
            {loading && (
              <div className="text-center py-3">
                <div className="spinner-border text-primary" />
              </div>
            )}

            {!loading && notifications.length === 0 && (
              <p className="text-muted text-center">
                No notifications available.
              </p>
            )}

            {!loading &&
              notifications.map((notification) => (
                <div
                  key={notification.notificationId}
                  className="shadow-sm alert alert-dismissible fade show d-flex align-items-center justify-content-between"
                >
                  {/* <h6>{notification.notificationId}</h6> */}
                  <div className="card-body">
                    {notification.message}
                  </div>
                  <button
                    className="btn btn-sm border-0 bg-transparent d-flex align-items-center gap-1 edge"
                    onClick={() => handleReadNotification(notification)}
                  >
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-x" viewBox="0 0 16 16">
                      <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708" />
                    </svg>
                  </button>
                </div>

              ))}
          </div>

        </div>
      </div>
    </div>
  );
};