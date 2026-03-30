/* eslint-disable @typescript-eslint/no-unused-vars */
import { useEffect, useState } from "react";
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import { getEmployees, type Employee } from "../services/employeeService";

const Dashboard: React.FC = () => {
    const [birthdays, setBirthdays] = useState<Employee[]>([]);

    useEffect(() => {
        loadBirthdays();
    }, []);

    const loadBirthdays = async () => {
        try {
            const employees = await getEmployees();

            const today = new Date();
            const todayMonth = today.getMonth();
            const todayDate = today.getDate();

            const todayBirthdays = employees.filter((emp) => {
                const dob = new Date(emp.dateOfBirth);
                return (
                    dob.getMonth() === todayMonth &&
                    dob.getDate() === todayDate
                );
            });

            setBirthdays(todayBirthdays);
        } catch (err) {
            console.error(err);
        }
    };

    return (
        <div style={{ display: "flex", gap: "20px", padding: "20px" }}>

            <div style={{ flex: 3 }}> 
                <FullCalendar
                    plugins={[dayGridPlugin]}
                    initialView="dayGridMonth"
                    height="auto"
                />
            </div>

            <div
            className="edge"
                style={{
                    flex: 1,
                    background: "#fff",
                    borderRadius: "12px",
                    padding: "20px",
                    boxShadow: "0 2px 8px rgba(0,0,0,0.1)",
                    height: "250px"
                }}
            >
                <h6 style={{ marginBottom: "15px" }}>🎂 Today's Birthdays</h6>

                {birthdays.length === 0 ? (
                    <p>No birthdays today</p>
                ) : (
                    birthdays.map((emp) => (
                        <div
                            key={emp.employeeId}
                            style={{
                                padding: "10px",
                                borderBottom: "1px solid #eee",
                            }}
                        >
                            {emp.fullName}
                        </div>
                    ))
                )}
            </div>
        </div>
    );
};

export default Dashboard;