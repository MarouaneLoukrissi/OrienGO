import { Injectable } from '@angular/core';
import { MenuItem } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class MenuService {
  private menuItems: MenuItem[] = [
    // Core navigation items
    {
      id: 'dashboard',
      label: 'SIDEBAR.MENU_ITEMS.DASHBOARD',
      icon: 'dashboard',
      route: '/dashboard',
      roles: ['admin', 'superAdmin', 'student', 'coach']
    },
    {
      id: 'notification',
      label: 'SIDEBAR.MENU_ITEMS.NOTIFICATIONS',
      icon: 'notifications',
      route: '/notifications',
      roles: ['student', 'coach'],
      badge: 3
    },

    // Admin items
    {
      id: 'user-management',
      label: 'SIDEBAR.MENU_ITEMS.USERS',
      icon: 'users',
      route: '/users',
      roles: ['admin', 'superAdmin']
    },
    {
      id: 'questions-management',
      label: 'SIDEBAR.MENU_ITEMS.QUESTIONS',
      icon: 'questions',
      route: '/questions',
      roles: ['admin', 'superAdmin']
    },
    // {
    //   id: 'analytics',
    //   label: 'Analytics',
    //   icon: 'analytics',
    //   route: '/analytics',
    //   roles: ['admin', 'superAdmin', 'coach']
    // },
    // {
    //   id: 'reports',
    //   label: 'Reports',
    //   icon: 'reports',
    //   route: '/reports',
    //   roles: ['admin', 'superAdmin', 'coach']
    // },

    // Coach specific items
    // {
    //   id: 'my-students',
    //   label: 'My Students',
    //   icon: 'students',
    //   route: '/students',
    //   roles: ['coach']
    // },
    // {
    //   id: 'coaching-sessions',
    //   label: 'Coaching Sessions',
    //   icon: 'coaching',
    //   route: '/coaching-sessions',
    //   roles: ['coach']
    // },
    // {
    //   id: 'student-progress',
    //   label: 'Student Progress',
    //   icon: 'progress',
    //   route: '/student-progress',
    //   roles: ['coach']
    // },

    // Assessment tools
    {
      id: 'riasec-test',
      label: 'SIDEBAR.MENU_ITEMS.RIASEC',
      icon: 'riasec',
      route: '/test',
      roles: ['student']
    },
    // {
    //   id: 'riasec-test-results',
    //   label: 'SIDEBAR.MENU_ITEMS.RIASEC',
    //   icon: 'riasec',
    //   route: '/test/results',
    //   roles: ['student']
    // },
    // {
    //   id: 'riasec-test-recommendations',
    //   label: 'SIDEBAR.MENU_ITEMS.RIASEC',
    //   icon: 'riasec',
    //   route: '/test/recommendations',
    //   roles: ['student']
    // },
    // {
    //   id: 'riasec-test-jobs',
    //   label: 'SIDEBAR.MENU_ITEMS.RIASEC',
    //   icon: 'riasec',
    //   route: '/test/personalizedjobs',
    //   roles: ['student']
    // },

    // Student specific items
    {
      id: 'network',
      label: 'SIDEBAR.MENU_ITEMS.NETWORK',
      icon: 'network',
      route: '/network',
      roles: ['student', 'coach']
    },
    {
      id: 'jobs',
      label: 'SIDEBAR.MENU_ITEMS.CAREER',
      icon: 'jobs',
      route: '/jobs',
      roles: ['student']
    },
    {
      id: 'saved',
      label: 'SIDEBAR.MENU_ITEMS.SAVED',
      icon: 'saved',
      route: '/saved',
      roles: ['student']
    },
    {
      id: 'favorites',
      label: 'SIDEBAR.MENU_ITEMS.FAVORITES',
      icon: 'favorites',
      route: '/favorites',
      roles: ['student'],
      isHighlighted: false
    },

    // Common items
    {
      id: 'history',
      label: 'SIDEBAR.MENU_ITEMS.HISTORY',
      icon: 'history',
      route: '/history',
      roles: ['student']
    },
    {
      id: 'profile',
      label: 'SIDEBAR.MENU_ITEMS.PROFILE',
      icon: 'profile',
      route: '/profile/current-user-123',
      roles: ['student'],
      divider: true
    },
    {
      id: 'coach-profile',
      label: 'SIDEBAR.MENU_ITEMS.PROFILE',
      icon: 'profile',
      route: '/profile/1',
      roles: ['coach'],
      divider: true
    },
    {
      id: 'manageJobs',
      label: 'SIDEBAR.MENU_ITEMS.MG_JOBS',
      icon: 'jobs',
      route: '/jobs',
      roles: ['admin', 'superAdmin']
    },
    {
      id: 'manager',
      label: 'SIDEBAR.MENU_ITEMS.ADMINS',
      icon: 'manager',
      route: '/manager',
      roles: ['superAdmin']
    },
    {
      id: 'settings',
      label: 'SIDEBAR.MENU_ITEMS.SETTINGS',
      icon: 'settings',
      route: '/settings',
      roles: ['admin', 'superAdmin', 'student', 'coach']
    },
    {
      id: 'logout',
      label: 'SIDEBAR.MENU_ITEMS.LOGOUT',
      icon: 'logout',
      roles: ['admin', 'superAdmin', 'student', 'coach']
    }
  ];

  constructor() {}

  getMenuItemsForRole(role: 'admin' | 'superAdmin' | 'student' | 'coach'): MenuItem[] {
    return this.menuItems.filter(item => item.roles.includes(role));
  }

  getAllMenuItems(): MenuItem[] {
    return this.menuItems;
  }

  updateMenuItem(id: string, updates: Partial<MenuItem>): void {
    const index = this.menuItems.findIndex(item => item.id === id);
    if (index !== -1) {
      this.menuItems[index] = { ...this.menuItems[index], ...updates };
    }
  }
}
