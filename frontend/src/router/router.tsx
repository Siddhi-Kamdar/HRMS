import { createBrowserRouter, Navigate } from "react-router-dom";

import Login from "../components/Login";
import AppLayout from "../pages/AppLayout";

import GameSchedule from "../pages/GameSchedule";
import SlotDetailPage from "../pages/SlotDetailPage";
import BookingForm from "../pages/BookingForm";

import TravelDisplay from "../pages/TravelDisplay";
import TravelCreate from "../components/CreateTravel";
import TravelDetail from "../pages/TravelDetail";

import ExpenseDashboard from "../pages/ExpenseDashboard";
import ExpenseCreate from "../components/CreateExpense";
import ExpenseSection from "../pages/ExpenseSection";

import JobDisplay from "../pages/JobDisplay";
import CreateJob from "../components/CreateJob";

import { AchievementsPage } from "../pages/AchievementsPage";
import OrgChart from "../pages/OrgChart";
import ReferralDashboard from "../components/ReferralDashboard";

import RoleGuard from "../components/RoleGuard";
import HrSlotManager from "../pages/HrSlotManager";

function NotFoundPage() {
  return <h1>404 - Page Not Found</h1>;
}

export const router = createBrowserRouter([
  {
    path: "/",
    element: <Login />,
  },

  {
    path: "/app",
    element: <AppLayout />,
    children: [
      {
        index: true,
        element: <Navigate to="games" replace />,
      },

      {
        path: "games",
        element: <GameSchedule />,
      },
      {
        path: "games/:slotId",
        element: <SlotDetailPage />,
      },
      {
        path: "games/:slotId/book",
        element: <BookingForm />,
      },

      {
        path: "travel",
        element: <TravelDisplay />,
      },
      {
        path: "travel/create",
        element: <TravelCreate />,
      },
      {
        path: "travel/:travelId",
        element: <TravelDetail />,
      },

      {
        path: "expenses",
        element: <ExpenseDashboard />,
      },
      {
        path: "expenses/create/:travelId",
        element: <ExpenseCreate />,
      },
      {
        path: "expenses/personal",
        element: <ExpenseSection />,
      },

      {
        path: "jobs",
        element: <JobDisplay />,
      },
      {
        path: "jobs/create",
        element: <CreateJob />,
      },
      {
        path: "jobs/:jobId/edit",
        element: <CreateJob />,
      },

      {
        path: "achievements",
        element: <AchievementsPage />,
      },

      {
        path: "org-chart/:empId",
        element: <OrgChart />,
      },

      {
        path: "hr/referrals",
        element: <ReferralDashboard />,
      },
      {
        path: "games/manage-slots",
        element: (
          <RoleGuard allowedRoles={["HR"]}>
            <HrSlotManager />
          </RoleGuard>
        ),
      }
    ],
  },

  {
    path: "*",
    element: <NotFoundPage />,
  },
]);