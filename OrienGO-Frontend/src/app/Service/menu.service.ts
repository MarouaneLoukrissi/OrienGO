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
      label: 'Dashboard',
      icon: 'dashboard',
      route: '/dashboard',
      roles: ['admin', 'superAdmin', 'student', 'coach']
    },
    {
      id: 'notifications',
      label: 'Notifications',
      icon: 'notifications',
      route: '/notifications',
      roles: ['admin', 'superAdmin', 'student', 'coach'],
      badge: 3
    },

    // Super Admin exclusive items
    {
      id: 'system-management',
      label: 'System Management',
      icon: 'system',
      route: '/system',
      roles: ['superAdmin']
    },
    {
      id: 'global-analytics',
      label: 'Global Analytics',
      icon: 'global-analytics',
      route: '/global-analytics',
      roles: ['superAdmin']
    },
    {
      id: 'admin-management',
      label: 'Admin Management',
      icon: 'admin-users',
      route: '/admin-management',
      roles: ['superAdmin']
    },

    // Admin items
    {
      id: 'user-management',
      label: 'User Management',
      icon: 'users',
      route: '/users',
      roles: ['admin', 'superAdmin']
    },
    {
      id: 'analytics',
      label: 'Analytics',
      icon: 'analytics',
      route: '/analytics',
      roles: ['admin', 'superAdmin', 'coach']
    },
    {
      id: 'reports',
      label: 'Reports',
      icon: 'reports',
      route: '/reports',
      roles: ['admin', 'superAdmin', 'coach']
    },

    // Coach specific items
    {
      id: 'my-students',
      label: 'My Students',
      icon: 'students',
      route: '/students',
      roles: ['coach']
    },
    {
      id: 'coaching-sessions',
      label: 'Coaching Sessions',
      icon: 'coaching',
      route: '/coaching-sessions',
      roles: ['coach']
    },
    {
      id: 'student-progress',
      label: 'Student Progress',
      icon: 'progress',
      route: '/student-progress',
      roles: ['coach']
    },

    // Assessment tools
    {
      id: 'riasec-test',
      label: 'RIASEC Test',
      icon: 'riasec',
      route: '/riasec',
      roles: ['student', 'coach']
    },

    // Student specific items
    {
      id: 'my-network',
      label: 'My Network',
      icon: 'network',
      route: '/network',
      roles: ['student']
    },
    {
      id: 'career-path',
      label: 'Career Path',
      icon: 'career',
      route: '/career-path',
      roles: ['student']
    },
    {
      id: 'saved',
      label: 'Saved',
      icon: 'saved',
      route: '/saved',
      roles: ['student']
    },
    {
      id: 'favorites',
      label: 'Favorites',
      icon: 'favorites',
      route: '/favorites',
      roles: ['student'],
      isHighlighted: false
    },

    // Common items
    {
      id: 'history',
      label: 'History',
      icon: 'history',
      route: '/history',
      roles: ['admin', 'superAdmin', 'student', 'coach']
    },
    {
      id: 'profile',
      label: 'Profile',
      icon: 'profile',
      route: '/profile',
      roles: ['admin', 'superAdmin', 'student', 'coach'],
      divider: true
    },
    {
      id: 'settings',
      label: 'Settings',
      icon: 'settings',
      route: '/settings',
      roles: ['admin', 'superAdmin', 'student', 'coach']
    },
    {
      id: 'help',
      label: 'Help & Support',
      icon: 'help',
      route: '/help',
      roles: ['admin', 'superAdmin', 'student', 'coach']
    },
    {
      id: 'logout',
      label: 'Log out',
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