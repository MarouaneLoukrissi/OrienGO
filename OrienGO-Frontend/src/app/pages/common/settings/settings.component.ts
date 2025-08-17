import { Component, NgModule } from '@angular/core';

type UserRole = 'admin' | 'superadmin' | 'student' | 'coach';

interface NotificationSettings {
  receiveEmailNotifications: boolean;
  newJobNotifications: boolean;
  testReminders: boolean;
  systemAlerts?: boolean; // For admin/superadmin
  coachingUpdates?: boolean; // For coach
  studentProgress?: boolean; // For coach
}

interface ProfileSettings {
  visibilityToTeachers: boolean;
  visibilityToStudents?: boolean; // For coach
  visibilityToCoaches?: boolean; // For student
  publicProfile?: boolean; // For admin/superadmin
}

interface AdminSettings {
  manageUsers?: boolean;
  systemMaintenance?: boolean;
  dataExport?: boolean;
  auditLogs?: boolean;
}

interface SettingsData {
  notifications: NotificationSettings;
  profile: ProfileSettings;
  admin?: AdminSettings;
}

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.css'
})
export class SettingsComponent {
  currentUserRole: UserRole = 'student';

  settings: SettingsData = {
    notifications: {
      receiveEmailNotifications: true,
      newJobNotifications: false,
      testReminders: true,
      systemAlerts: false,
      coachingUpdates: true,
      studentProgress: false
    },
    profile: {
      visibilityToTeachers: false,
      visibilityToStudents: true,
      visibilityToCoaches: false,
      publicProfile: false
    },
    admin: {
      manageUsers: false,
      systemMaintenance: false,
      dataExport: false,
      auditLogs: false
    }
  };

  // Role-based helper methods
  isAdminRole(): boolean {
    return this.currentUserRole === 'admin' || this.currentUserRole === 'superadmin';
  }

  showJobNotifications(): boolean {
    return this.currentUserRole === 'student' || this.currentUserRole === 'coach';
  }

  getSubtitleByRole(): string {
    const subtitles = {
      student: 'Manage your saved Jobs and trainings',
      coach: 'Manage your coaching preferences and notifications',
      admin: 'Manage system settings and user permissions',
      superadmin: 'Full system administration and maintenance'
    };
    return subtitles[this.currentUserRole];
  }

  getPrivacySectionTitle(): string {
    const titles = {
      student: 'Confidential',
      coach: 'Privacy Settings',
      admin: 'Admin Privacy',
      superadmin: 'System Privacy'
    };
    return titles[this.currentUserRole];
  }

  getDangerButtonText(): string {
    const texts = {
      student: 'Delete this account',
      coach: 'Delete coach account',
      admin: 'Delete admin account',
      superadmin: 'Delete manager account'
    };
    return texts[this.currentUserRole];
  }

  // Role change handler
  onRoleChange(): void {
    this.initializeSettingsForRole();
    console.log('Role changed to:', this.currentUserRole);
  }

  // Initialize settings based on role
  initializeSettingsForRole(): void {
    // Reset admin settings
    if (!this.isAdminRole()) {
      this.settings.admin = undefined;
    } else if (!this.settings.admin) {
      this.settings.admin = {
        manageUsers: false,
        systemMaintenance: false,
        dataExport: false,
        auditLogs: false
      };
    }

    // Role-specific default settings
    switch (this.currentUserRole) {
      case 'student':
        this.settings.notifications.coachingUpdates = undefined;
        this.settings.notifications.studentProgress = undefined;
        this.settings.notifications.systemAlerts = undefined;
        this.settings.profile.visibilityToStudents = undefined;
        this.settings.profile.publicProfile = undefined;
        break;
      case 'coach':
        this.settings.notifications.testReminders = false;
        this.settings.notifications.coachingUpdates = true;
        this.settings.notifications.studentProgress = false;
        this.settings.notifications.systemAlerts = undefined;
        this.settings.profile.visibilityToTeachers = false;
        this.settings.profile.visibilityToStudents = true;
        this.settings.profile.visibilityToCoaches = undefined;
        this.settings.profile.publicProfile = undefined;
        break;
      case 'admin':
      case 'superadmin':
        this.settings.notifications.testReminders = false;
        this.settings.notifications.coachingUpdates = undefined;
        this.settings.notifications.studentProgress = undefined;
        this.settings.notifications.systemAlerts = true;
        this.settings.profile.visibilityToTeachers = false;
        this.settings.profile.visibilityToStudents = undefined;
        this.settings.profile.visibilityToCoaches = undefined;
        this.settings.profile.publicProfile = false;
        break;
    }
  }

  // Notification toggle methods
  toggleEmailNotifications(): void {
    this.settings.notifications.receiveEmailNotifications =
      !this.settings.notifications.receiveEmailNotifications;
    this.updateSettings();
  }

  toggleNewJobNotifications(): void {
    this.settings.notifications.newJobNotifications =
      !this.settings.notifications.newJobNotifications;
    this.updateSettings();
  }

  toggleTestReminders(): void {
    this.settings.notifications.testReminders =
      !this.settings.notifications.testReminders;
    this.updateSettings();
  }

  toggleSystemAlerts(): void {
    this.settings.notifications.systemAlerts =
      !this.settings.notifications.systemAlerts;
    this.updateSettings();
  }

  toggleCoachingUpdates(): void {
    this.settings.notifications.coachingUpdates =
      !this.settings.notifications.coachingUpdates;
    this.updateSettings();
  }

  toggleStudentProgress(): void {
    this.settings.notifications.studentProgress =
      !this.settings.notifications.studentProgress;
    this.updateSettings();
  }

  // Profile toggle methods
  toggleProfileVisibility(): void {
    this.settings.profile.visibilityToTeachers =
      !this.settings.profile.visibilityToTeachers;
    this.updateSettings();
  }

  toggleVisibilityToStudents(): void {
    this.settings.profile.visibilityToStudents =
      !this.settings.profile.visibilityToStudents;
    this.updateSettings();
  }

  toggleVisibilityToCoaches(): void {
    this.settings.profile.visibilityToCoaches =
      !this.settings.profile.visibilityToCoaches;
    this.updateSettings();
  }

  togglePublicProfile(): void {
    this.settings.profile.publicProfile =
      !this.settings.profile.publicProfile;
    this.updateSettings();
  }

  // Admin toggle methods
  toggleUserManagement(): void {
    if (this.settings.admin) {
      this.settings.admin.manageUsers = !this.settings.admin.manageUsers;
      this.updateSettings();
    }
  }

  toggleSystemMaintenance(): void {
    if (this.settings.admin) {
      this.settings.admin.systemMaintenance = !this.settings.admin.systemMaintenance;
      this.updateSettings();
    }
  }

  toggleDataExport(): void {
    if (this.settings.admin) {
      this.settings.admin.dataExport = !this.settings.admin.dataExport;
      this.updateSettings();
    }
  }

  // Action methods - ready for backend integration
  modifyPassword(): void {
    console.log('Modify password clicked for role:', this.currentUserRole);
    // TODO: Implement role-specific password modification logic
    // Different roles might have different password requirements
  }

  deleteAccount(): void {
    const roleSpecificMessage = this.currentUserRole === 'superadmin'
      ? 'Are you sure you want to delete the superadmin account? This will remove all system access.'
      : `Are you sure you want to delete your ${this.currentUserRole} account? This action cannot be undone.`;

    const confirmed = confirm(roleSpecificMessage);
    if (confirmed) {
      console.log('Delete account confirmed for role:', this.currentUserRole);
      // TODO: Backend API call with role-specific deletion logic
    }
  }

  // Method to handle settings updates - ready for backend integration
  private updateSettings(): void {
    console.log('Settings updated for role:', this.currentUserRole, this.settings);
    // TODO: Send updated settings to backend with role context
    // Example: this.settingsService.updateSettings(this.currentUserRole, this.settings)
  }

  // Lifecycle hook for loading initial settings from backend
  ngOnInit(): void {
    this.initializeSettingsForRole();
    // TODO: Load role-specific settings from backend
    // this.loadSettingsFromBackend(this.currentUserRole);
  }

  // Method to load settings from backend based on role
  private loadSettingsFromBackend(): void {
    // TODO: Implement backend settings loading with role context
    // this.settingsService.getSettings(this.currentUserRole).subscribe(settings => {
    //   this.settings = settings;
    // });
  }
}
