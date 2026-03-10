import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { navigationService } from "../services/navigationService";

export default function NavigationSetter() {
  const navigate = useNavigate();

  useEffect(() => {
    navigationService.setNavigateFunction(navigate);
  }, [navigate]);

  return null;
}