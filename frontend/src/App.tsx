
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./components/Login";
import Dashboard from "./pages/Dashboard";
import GameSchedule from "./pages/GameSchedule";

function App() {
  return (
    <div style={{width: "100%"}}>
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/games" element={<GameSchedule/>}/>
      
      </Routes>
    </Router>
    </div>
  );
}


export default App;
