class NavigationService {
  private navigateFn: ((path: string, params?: object) => void) | null = null;

  setNavigateFunction(fn: (path: string, params?: object) => void) {
      this.navigateFn = fn;
    }
    navigate(path: string, params = {}) {
      if (this.navigateFn) {
        this.navigateFn(path, params);
      } else {
        console.warn("Navigate function not set yet!");
      }
    }
    navigateToOrgChart(user: any) {
      this.navigate(`/app/org-chart/${user.id}`);
    }
  }
  export const navigationService = new NavigationService();