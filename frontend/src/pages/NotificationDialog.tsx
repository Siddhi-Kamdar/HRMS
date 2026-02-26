import React, { useEffect, useState } from "react";
import {
  getNotifications,
  type Notification,
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

  return (
    <div className="modal show fade d-block" tabIndex={-1}>
      <div className="modal-dialog modal-dialog-centered">
        <div className="modal-content shadow border-0 rounded-4">

          <div className="modal-header">
            <h5 className="modal-title fw-semibold">Notifications</h5>
            <button
              type="button"
              className="btn-close"
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
                  className="card mb-3 shadow-sm border-0"
                >
                  <div className="card-body">
                    {notification.message}
                  </div>
                </div>
              ))}
          </div>

        </div>
      </div>
    </div>
  );
};