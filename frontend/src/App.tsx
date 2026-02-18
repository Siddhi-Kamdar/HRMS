import { BrowserRouter, Routes, Route } from "react-router-dom";
import GameSchedule from "./pages/GameSchedule";
import Login from "./components/Login";
import AppLayout from "./pages/AppLayout";
import BookingForm from "./pages/BookingForm";
import TravelDisplay from "./pages/TravelDisplay";

function App() {
  return (
    <BrowserRouter>
      <Routes>

        <Route path="/" element={<Login />} />

        <Route path="/app" element={<AppLayout />}>
          <Route path="travel" element={<TravelDisplay/>} />
          <Route path="achievements" element={<div>Achievements Page</div>} />
          <Route path="games" element={<GameSchedule />} />
          <Route path="jobs" element={<div>Jobs Page</div>} />
          <Route path="games/slot/:slotId/book" element={<BookingForm/>}/>
        </Route>

      </Routes>
    </BrowserRouter>
  );
}

export default App;