import { Navigate } from "react-router-dom";

interface Props {
  children: React.ReactNode;
  allowedRoles: string[];
}

const RoleGuard: React.FC<Props> = ({ children, allowedRoles }) => {

  const user = JSON.parse(localStorage.getItem("user") || "{}");

  if (!user?.role) {
    return <Navigate to="/" replace />;
  }

  if (!allowedRoles.includes(user.role)) {
    return <Navigate to="/app" replace />;
  }

  return <>{children}</>;
};

export default RoleGuard;