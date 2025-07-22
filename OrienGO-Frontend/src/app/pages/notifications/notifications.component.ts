import { Component } from '@angular/core';

type UserRole = 'student' | 'coach';
type NotificationType = 'job' | 'test' | 'coaching' | 'progress' | 'system' | 'reminder';
type NotificationStatus = 'unread' | 'read';

interface Notification {
  id: string;
  type: NotificationType;
  title: string;
  message: string;
  timestamp: Date;
  status: NotificationStatus;
  actionUrl?: string;
  actionText?: string;
  avatar?: string;
  metadata?: {
    jobTitle?: string;
    studentName?: string;
    testName?: string;
    sessionDate?: string;
    companyName?: string;
  };
}


@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrl: './notifications.component.css'
})
export class NotificationsComponent {
  currentUserRole: UserRole = 'student';
  activeFilter: string = 'all';
  isLoadingMore: boolean = false;
  hasMoreNotifications: boolean = true;

  notifications: Notification[] = [
    // Student notifications
    {
      id: '1',
      type: 'job',
      title: 'New Job Opportunity',
      message: 'A new Frontend Developer position at TechCorp matches your profile. Apply now to increase your chances!',
      timestamp: new Date(Date.now() - 2 * 60 * 60 * 1000), // 2 hours ago
      status: 'unread',
      actionText: 'View Job',
      actionUrl: '/jobs/1',
      metadata: {
        jobTitle: 'Frontend Developer',
        companyName: 'TechCorp'
      }
    },
    {
      id: '2',
      type: 'test',
      title: 'Test Reminder',
      message: 'Don\'t forget to complete your JavaScript Assessment. You have 2 days remaining.',
      timestamp: new Date(Date.now() - 4 * 60 * 60 * 1000), // 4 hours ago
      status: 'unread',
      actionText: 'Take Test',
      actionUrl: '/tests/js-assessment',
      metadata: {
        testName: 'JavaScript Assessment'
      }
    },
    {
      id: '3',
      type: 'coaching',
      title: 'Coaching Session Scheduled',
      message: 'Your next coaching session with Sarah Johnson is scheduled for tomorrow at 2:00 PM.',
      timestamp: new Date(Date.now() - 6 * 60 * 60 * 1000), // 6 hours ago
      status: 'read',
      actionText: 'View Details',
      actionUrl: '/coaching/session/123',
      metadata: {
        sessionDate: 'Tomorrow 2:00 PM'
      }
    },
    {
      id: '4',
      type: 'system',
      title: 'Profile Updated',
      message: 'Your profile has been successfully updated with new skills and experience.',
      timestamp: new Date(Date.now() - 24 * 60 * 60 * 1000), // 1 day ago
      status: 'read'
    },
    // Coach notifications
    {
      id: '5',
      type: 'progress',
      title: 'Student Progress Update',
      message: 'John Smith has completed the React Fundamentals course with a score of 95%.',
      timestamp: new Date(Date.now() - 1 * 60 * 60 * 1000), // 1 hour ago
      status: 'unread',
      actionText: 'View Progress',
      actionUrl: '/students/john-smith/progress',
      metadata: {
        studentName: 'John Smith',
        testName: 'React Fundamentals'
      }
    },
    {
      id: '6',
      type: 'coaching',
      title: 'New Coaching Request',
      message: 'Emma Wilson has requested a coaching session for career guidance and interview preparation.',
      timestamp: new Date(Date.now() - 3 * 60 * 60 * 1000), // 3 hours ago
      status: 'unread',
      actionText: 'Schedule Session',
      actionUrl: '/coaching/requests/456',
      metadata: {
        studentName: 'Emma Wilson'
      }
    },
    {
      id: '7',
      type: 'reminder',
      title: 'Upcoming Session Reminder',
      message: 'You have a coaching session with Michael Brown in 30 minutes. Please prepare the session materials.',
      timestamp: new Date(Date.now() - 5 * 60 * 60 * 1000), // 5 hours ago
      status: 'read',
      metadata: {
        studentName: 'Michael Brown',
        sessionDate: 'In 30 minutes'
      }
    },
    {
      id: '8',
      type: 'job',
      title: 'Job Posting Approved',
      message: 'Your job posting for "Senior React Developer" has been approved and is now live.',
      timestamp: new Date(Date.now() - 12 * 60 * 60 * 1000), // 12 hours ago
      status: 'read',
      actionText: 'View Posting',
      actionUrl: '/jobs/manage/789',
      metadata: {
        jobTitle: 'Senior React Developer'
      }
    }
  ];

  get filteredNotifications(): Notification[] {
    let filtered = this.notifications;

    // Filter by user role
    if (this.currentUserRole === 'student') {
      filtered = filtered.filter(n => 
        ['job', 'test', 'coaching', 'system'].includes(n.type)
      );
    } else if (this.currentUserRole === 'coach') {
      filtered = filtered.filter(n => 
        ['progress', 'coaching', 'reminder', 'job', 'system'].includes(n.type)
      );
    }

    // Filter by active filter
    if (this.activeFilter !== 'all') {
      if (this.activeFilter === 'unread') {
        filtered = filtered.filter(n => n.status === 'unread');
      } else {
        filtered = filtered.filter(n => n.type === this.activeFilter);
      }
    }

    return filtered.sort((a, b) => b.timestamp.getTime() - a.timestamp.getTime());
  }

  get availableFilters() {
    const baseFilters = [
      { key: 'all', label: 'All', count: this.filteredNotifications.length },
      { key: 'unread', label: 'Unread', count: this.unreadCount }
    ];

    if (this.currentUserRole === 'student') {
      return [
        ...baseFilters,
        { key: 'job', label: 'Jobs', count: this.getFilterCount('job') },
        { key: 'test', label: 'Tests', count: this.getFilterCount('test') },
        { key: 'coaching', label: 'Coaching', count: this.getFilterCount('coaching') }
      ];
    } else {
      return [
        ...baseFilters,
        { key: 'progress', label: 'Progress', count: this.getFilterCount('progress') },
        { key: 'coaching', label: 'Coaching', count: this.getFilterCount('coaching') },
        { key: 'reminder', label: 'Reminders', count: this.getFilterCount('reminder') }
      ];
    }
  }

  get unreadCount(): number {
    return this.filteredNotifications.filter(n => n.status === 'unread').length;
  }

  getFilterCount(type: string): number {
    let roleFiltered = this.notifications;
    
    if (this.currentUserRole === 'student') {
      roleFiltered = roleFiltered.filter(n => 
        ['job', 'test', 'coaching', 'system'].includes(n.type)
      );
    } else if (this.currentUserRole === 'coach') {
      roleFiltered = roleFiltered.filter(n => 
        ['progress', 'coaching', 'reminder', 'job', 'system'].includes(n.type)
      );
    }

    return roleFiltered.filter(n => n.type === type).length;
  }

  onRoleChange(): void {
    this.activeFilter = 'all';
    console.log('Role changed to:', this.currentUserRole);
  }

  setActiveFilter(filter: string): void {
    this.activeFilter = filter;
  }

  markAsRead(notification: Notification): void {
    if (notification.status === 'unread') {
      notification.status = 'read';
      this.updateNotificationStatus(notification.id, 'read');
    }
  }

  markAllAsRead(): void {
    this.filteredNotifications.forEach(notification => {
      if (notification.status === 'unread') {
        notification.status = 'read';
      }
    });
    this.updateAllNotificationsStatus('read');
  }

  handleNotificationAction(notification: Notification, event: Event): void {
    event.stopPropagation();
    console.log('Action clicked:', notification.actionUrl);
    this.markAsRead(notification);
    // TODO: Navigate to the action URL
    // this.router.navigate([notification.actionUrl]);
  }

  loadMoreNotifications(): void {
    this.isLoadingMore = true;
    // TODO: Load more notifications from backend
    setTimeout(() => {
      this.isLoadingMore = false;
      this.hasMoreNotifications = false; // Simulate no more notifications
    }, 1500);
  }

  trackByNotificationId(index: number, notification: Notification): string {
    return notification.id;
  }

  getNotificationIconClass(type: NotificationType): string {
    const classes = {
      job: 'bg-emerald-100 text-emerald-600',
      test: 'bg-orange-100 text-orange-600',
      coaching: 'bg-purple-100 text-purple-600',
      progress: 'bg-blue-100 text-blue-600',
      system: 'bg-slate-100 text-slate-600',
      reminder: 'bg-amber-100 text-amber-600'
    };
    return classes[type] || 'bg-slate-100 text-slate-600';
  }

  getNotificationIconPath(type: NotificationType): string {
    const paths = {
      job: 'M21 13.255A23.931 23.931 0 0112 15c-3.183 0-6.22-.62-9-1.745M16 6V4a2 2 0 00-2-2h-4a2 2 0 00-2 2v2m4 6h.01M5 20h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z',
      test: 'M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z',
      coaching: 'M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z',
      progress: 'M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z',
      system: 'M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z',
      reminder: 'M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z'
    };
    return paths[type] || paths.system;
  }

  getRelativeTime(timestamp: Date): string {
    const now = new Date();
    const diffInSeconds = Math.floor((now.getTime() - timestamp.getTime()) / 1000);

    if (diffInSeconds < 60) {
      return 'Just now';
    } else if (diffInSeconds < 3600) {
      const minutes = Math.floor(diffInSeconds / 60);
      return `${minutes}m ago`;
    } else if (diffInSeconds < 86400) {
      const hours = Math.floor(diffInSeconds / 3600);
      return `${hours}h ago`;
    } else if (diffInSeconds < 604800) {
      const days = Math.floor(diffInSeconds / 86400);
      return `${days}d ago`;
    } else {
      return timestamp.toLocaleDateString();
    }
  }

  // Backend integration methods
  private updateNotificationStatus(notificationId: string, status: NotificationStatus): void {
    console.log('Updating notification status:', notificationId, status);
    // TODO: API call to update notification status
    // this.notificationService.updateStatus(notificationId, status).subscribe();
  }

  private updateAllNotificationsStatus(status: NotificationStatus): void {
    console.log('Marking all notifications as:', status);
    // TODO: API call to mark all notifications as read
    // this.notificationService.markAllAsRead(this.currentUserRole).subscribe();
  }

  ngOnInit(): void {
    this.loadNotificationsFromBackend();
  }

  private loadNotificationsFromBackend(): void {
    console.log('Loading notifications for role:', this.currentUserRole);
    // TODO: Load notifications from backend based on user role
    // this.notificationService.getNotifications(this.currentUserRole).subscribe(notifications => {
    //   this.notifications = notifications;
    // });
  }
}
