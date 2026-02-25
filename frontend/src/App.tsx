import { BrowserRouter, Routes, Route } from "react-router-dom";
import GameSchedule from "./pages/GameSchedule";
import Login from "./components/Login";
import AppLayout from "./pages/AppLayout";
import BookingForm from "./pages/BookingForm";
import TravelDisplay from "./pages/TravelDisplay";
import JobDisplay from "./pages/JobDisplay";
import TravelCreate from "./pages/TravelCreate";
import TravelDetail from "./pages/TravelDetail";
import ExpenseCreate from "./pages/ExpenseCreate";
import ExpenseDashboard from "./pages/ExpenseDashboard";
import OrgChart from "./pages/OrgChart";
import SlotDetailPage from "./pages/SlotDetailPage";
import ExpenseSection from "./pages/ExpenseSection";
import { AchievementsPage } from "./pages/AchievementsPage";


function App() {
  return (
    <BrowserRouter>
      <Routes>

        <Route path="/" element={<Login />} />

        <Route path="/app" element={<AppLayout />}>
          <Route path="travel" element={<TravelDisplay />} />
          <Route  path="games" element={<GameSchedule />} />
          <Route path="jobs" element={<JobDisplay />} />
          <Route path="games/slot/:slotId/book" element={<BookingForm />} />
          <Route path="travel/create" element={<TravelCreate />} />
          <Route path="travel/:travelId" element={<TravelDetail />} />
          <Route
            path="expense/create/:travelId"
            element={<ExpenseCreate />}
          />
          <Route path="achievements" element={<AchievementsPage />} /> 
          <Route
            path="expenses"
            element={<ExpenseDashboard />}
          />
          <Route path="org-chart/:empId" element={<OrgChart />} />
          <Route
            path="games/slot/:slotId"
            element={<SlotDetailPage />}
          />
          <Route path = "travel/personal-expenses" element={<ExpenseSection/>} />
        </Route>

      </Routes>
    </BrowserRouter>
  );
}

export default App;