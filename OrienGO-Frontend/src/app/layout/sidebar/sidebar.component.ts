import { Component, OnInit, OnDestroy, Input, HostListener } from '@angular/core';
import { trigger, state, style, transition, animate } from '@angular/animations';
import { Subject, takeUntil, combineLatest } from 'rxjs';
import { UserService, User, MenuItem } from '../../Service/user.service';
import { MenuService } from '../../Service/menu.service';

type DeviceType = 'mobile' | 'tablet' | 'laptop' | 'desktop';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css'],
  animations: [
    trigger('slideInOut', [
      state('expanded', style({ width: '280px' })),
      state('collapsed', style({ width: '80px' })),
      state('mobile-open', style({ 
        width: '280px',
        transform: 'translateX(0)'
      })),
      state('mobile-closed', style({ 
        width: '280px',
        transform: 'translateX(-100%)'
      })),
      transition('expanded <=> collapsed', animate('300ms cubic-bezier(0.4, 0, 0.2, 1)')),
      transition('mobile-open <=> mobile-closed', animate('300ms cubic-bezier(0.4, 0, 0.2, 1)'))
    ]),
    trigger('fadeInOut', [
      state('visible', style({ opacity: 1, transform: 'translateX(0)' })),
      state('hidden', style({ opacity: 0, transform: 'translateX(-10px)' })),
      transition('visible <=> hidden', animate('200ms ease-in-out'))
    ]),
    trigger('overlayFade', [
      state('visible', style({ opacity: 0.5, visibility: 'visible' })),
      state('hidden', style({ opacity: 0, visibility: 'hidden' })),
      transition('visible <=> hidden', animate('300ms ease-in-out'))
    ])
  ]
})
export class SidebarComponent implements OnInit, OnDestroy {
  @Input() initialExpanded: boolean = false;
  @Input() userRole?: 'admin' | 'superAdmin' | 'student' | 'coach';

  isExpanded = true;
  currentDeviceType: DeviceType = 'desktop';
  showMobileOverlay = false;
  currentUser: User | null = null;
  menuItems: MenuItem[] = [];
  activeItem = 'dashboard';
  
  private destroy$ = new Subject<void>();
  private readonly STORAGE_KEY = 'sidebar-preferences';

  private readonly breakpoints = {
    mobile: 640,   
    tablet: 1024,
    laptop: 1440,
    desktop: 1441
  };

  constructor(
    private userService: UserService,
    private menuService: MenuService
  ) {}

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    const previousDeviceType = this.currentDeviceType;
    this.checkScreenSize();
    
    if (previousDeviceType !== this.currentDeviceType) {
      this.handleDeviceTypeChange(previousDeviceType);
    }
  }

  ngOnInit() {
    this.checkScreenSize();
    this.initializeSidebarState();

    combineLatest([
      this.userService.currentUser$
    ]).pipe(
      takeUntil(this.destroy$)
    ).subscribe(([user]) => {
      this.currentUser = user;
      this.loadMenuItems();
    });

    if (this.userRole) {
      this.userService.updateUserRole(this.userRole);
    }
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private checkScreenSize() {
    const width = window.innerWidth;
    
    if (width < this.breakpoints.mobile) {
      this.currentDeviceType = 'mobile';
    } else if (width < this.breakpoints.tablet) {
      this.currentDeviceType = 'tablet';
    } else if (width < this.breakpoints.laptop) {
      this.currentDeviceType = 'laptop';
    } else {
      this.currentDeviceType = 'desktop';
    }
  }

  private initializeSidebarState() {
    const savedPreferences = this.getSavedPreferences();
    
    switch (this.currentDeviceType) {
      case 'mobile':
        this.isExpanded = false;
        this.showMobileOverlay = false;
        break;
        
      case 'tablet':
        this.isExpanded = savedPreferences?.tablet?.isExpanded ?? false;
        break;
        
      case 'laptop':
        this.isExpanded = savedPreferences?.laptop?.isExpanded ?? false;
        break;
        
      case 'desktop':
        this.isExpanded = savedPreferences?.desktop?.isExpanded ?? true; //false://ok////
        break;
    }

    if (this.initialExpanded !== undefined && this.currentDeviceType !== 'mobile' && !savedPreferences?.[this.currentDeviceType]) {
      this.isExpanded = this.initialExpanded;
    }
  }

  private handleDeviceTypeChange(previousDeviceType: DeviceType) {
    if (previousDeviceType !== 'mobile') {
      this.savePreferences();
    }

    this.initializeSidebarState();
  }

  private getSavedPreferences(): any {
    try {
      const saved = localStorage.getItem(this.STORAGE_KEY);
      return saved ? JSON.parse(saved) : null;
    } catch (error) {
      console.warn('Failed to load sidebar preferences:', error);
      return null;
    }
  }

  private savePreferences() {
    // Only save preferences for tablet, laptop, and desktop
    if (this.currentDeviceType === 'mobile') return;

    try {
      const currentPreferences = this.getSavedPreferences() || {};
      
      currentPreferences[this.currentDeviceType] = {
        isExpanded: this.isExpanded,
        lastUpdated: new Date().toISOString()
      };

      localStorage.setItem(this.STORAGE_KEY, JSON.stringify(currentPreferences));
    } catch (error) {
      console.warn('Failed to save sidebar preferences:', error);
    }
  }

  private loadMenuItems() {
    if (this.currentUser) {
      this.menuItems = this.menuService.getMenuItemsForRole(this.currentUser.role);
    }
  }

  toggleSidebar() {
    if (this.currentDeviceType === 'mobile') {
      this.showMobileOverlay = !this.showMobileOverlay;
      this.toggleBodyScroll();
    } else {
      this.isExpanded = !this.isExpanded;
      this.savePreferences();
    }
  }

  closeMobileSidebar() {
    if (this.currentDeviceType === 'mobile') {
      this.showMobileOverlay = false;
      this.enableBodyScroll();
    }
  }

  selectItem(itemId: string) {
    this.activeItem = itemId;
    
    this.menuItems = this.menuItems.map(item => ({
      ...item,
      isActive: item.id === itemId
    }));

    if (this.currentDeviceType === 'mobile') {
      this.closeMobileSidebar();
    }

    if (itemId === 'logout') {
      this.handleLogout();
      return;
    }
    //OK//
    console.log(`Navigating to: ${itemId}`);
  }

  private handleLogout() {
    try {
      localStorage.removeItem(this.STORAGE_KEY);
    } catch (error) {
      console.warn('Failed to clear sidebar preferences:', error);
    }
    //OK//
    console.log('Logging out...');
  }

  private toggleBodyScroll() {
    if (this.showMobileOverlay) {
      this.disableBodyScroll();
    } else {
      this.enableBodyScroll();
    }
  }
  private scrollY = 0;
  private disableBodyScroll() {
    this.scrollY = window.scrollY;
    document.body.style.position = 'fixed';
    document.body.style.top = `-${this.scrollY}px`;
    document.body.style.width = '100%';
    document.body.style.overflow = 'hidden';
    document.documentElement.style.overflow = 'hidden';
  }

  private enableBodyScroll() {
    document.body.style.position = '';
    document.body.style.top = '';
    document.body.style.width = '';
    document.body.style.overflow = '';
    document.documentElement.style.overflow = '';
    window.scrollTo(0, this.scrollY);
  }

  switchRole(role: 'admin' | 'superAdmin' | 'student' | 'coach') {
    this.userService.updateUserRole(role);
  }

  resetPreferences() {
    try {
      localStorage.removeItem(this.STORAGE_KEY);
      this.initializeSidebarState();
      console.log('Sidebar preferences reset');
    } catch (error) {
      console.warn('Failed to reset sidebar preferences:', error);
    }
  }

  get deviceType(): DeviceType {
    return this.currentDeviceType;
  }

  get isMobile(): boolean {
    return this.currentDeviceType === 'mobile';
  }

  get isTablet(): boolean {
    return this.currentDeviceType === 'tablet';
  }

  get supportsPreferences(): boolean {
    return ['tablet', 'laptop', 'desktop'].includes(this.currentDeviceType);
  }

  trackByItemId(index: number, item: MenuItem): string {
    return item.id;
  }

  getSidebarState(): string {
    if (this.currentDeviceType === 'mobile') {
      return this.showMobileOverlay ? 'mobile-open' : 'mobile-closed';
    }
    return this.isExpanded ? 'expanded' : 'collapsed';
  }

  getOverlayState(): string {
    return this.showMobileOverlay ? 'visible' : 'hidden';
  }

  //OK//
  getRoleDisplayName(role: string): string {
    const roleNames = {
      'admin': 'Administrator',
      'superAdmin': 'Super Admin',
      'student': 'Student',
      'coach': 'Coach'
    };
    return roleNames[role as keyof typeof roleNames] || role;
  }

  //OK//
  getRoleColor(role: string): string {
    const roleColors = {
      'admin': 'bg-blue-500',
      'superAdmin': 'bg-purple-500',
      'student': 'bg-green-500',
      'coach': 'bg-orange-500'
    };
    return roleColors[role as keyof typeof roleColors] || 'bg-gray-500';
  }

  /*getIconSvg(iconType: string): string {
    const icons: { [key: string]: string } = {
      dashboard: `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <rect x="3" y="3" width="7" height="7"/>
        <rect x="14" y="3" width="7" height="7"/>
        <rect x="14" y="14" width="7" height="7"/>
        <rect x="3" y="14" width="7" height="7"/>
      </svg>`,
      notifications: `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"/>
        <path d="M13.73 21a2 2 0 0 1-3.46 0"/>
      </svg>`,
      system: `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <rect x="2" y="3" width="20" height="14" rx="2" ry="2"/>
        <line x1="8" y1="21" x2="16" y2="21"/>
        <line x1="12" y1="17" x2="12" y2="21"/>
      </svg>`,
      'global-analytics': `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <circle cx="12" cy="12" r="10"/>
        <line x1="2" y1="12" x2="22" y2="12"/>
        <path d="M12 2a15.3 15.3 0 0 1 4 10 15.3 15.3 0 0 1-4 10 15.3 15.3 0 0 1-4-10 15.3 15.3 0 0 1 4-10z"/>
        <polyline points="8,12 12,8 16,12"/>
      </svg>`,
      'admin-users': `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/>
        <circle cx="9" cy="7" r="4"/>
        <path d="M22 21v-2a4 4 0 0 0-3-3.87"/>
        <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
        <circle cx="18" cy="8" r="2"/>
      </svg>`,
      users: `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
        <circle cx="9" cy="7" r="4"/>
        <path d="M23 21v-2a4 4 0 0 0-3-3.87"/>
        <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
      </svg>`,
      analytics: `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <polyline points="22,12 18,12 15,21 9,3 6,12 2,12"/>
      </svg>`,
      reports: `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
        <polyline points="14,2 14,8 20,8"/>
        <line x1="16" y1="13" x2="8" y2="13"/>
        <line x1="16" y1="17" x2="8" y2="17"/>
        <polyline points="10,9 9,9 8,9"/>
      </svg>`,
      students: `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <path d="M22 10v6M2 10l10-5 10 5-10 5z"/>
        <path d="M6 12v5c3 3 9 3 12 0v-5"/>
      </svg>`,
      coaching: `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
        <path d="M8 9h8"/>
        <path d="M8 13h6"/>
      </svg>`,
      progress: `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <line x1="18" y1="20" x2="18" y2="10"/>
        <line x1="12" y1="20" x2="12" y2="4"/>
        <line x1="6" y1="20" x2="6" y2="14"/>
      </svg>`,
      riasec: `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z"/>
        <path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z"/>
      </svg>`,
      network: `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <circle cx="12" cy="12" r="10"/>
        <line x1="2" y1="12" x2="22" y2="12"/>
        <path d="M12 2a15.3 15.3 0 0 1 4 10 15.3 15.3 0 0 1-4 10 15.3 15.3 0 0 1-4-10 15.3 15.3 0 0 1 4-10z"/>
      </svg>`,
      career: `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
      </svg>`,
      saved: `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <path d="M19 21l-7-5-7 5V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2z"/>
      </svg>`,
      favorites: `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <polygon points="12,2 15.09,8.26 22,9.27 17,14.14 18.18,21.02 12,17.77 5.82,21.02 7,14.14 2,9.27 8.91,8.26"/>
      </svg>`,
      history: `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <circle cx="12" cy="12" r="10"/>
        <polyline points="12,6 12,12 16,14"/>
      </svg>`,
      profile: `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
        <circle cx="12" cy="7" r="4"/>
      </svg>`,
      settings: `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <circle cx="12" cy="12" r="3"/>
        <path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1 0 2.83 2 2 0 0 1-2.83 0l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-2 2 2 2 0 0 1-2-2v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 0 1-2.83 0 2 2 0 0 1 0-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1-2-2 2 2 0 0 1 2-2h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 0 1 0-2.83 2 2 0 0 1 2.83 0l.06.06a1.65 1.65 0 0 0 1.82.33H9a1.65 1.65 0 0 0 1 1.51V3a2 2 0 0 1 2-2 2 2 0 0 1 2 2v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 0 1 2.83 0 2 2 0 0 1 0 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82V9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 2 2 2 2 0 0 1-2 2h-.09a1.65 1.65 0 0 0-1.51 1z"/>
      </svg>`,
      help: `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <circle cx="12" cy="12" r="10"/>
        <path d="M9.09 9a3 3 0 0 1 5.83 1c0 2-3 3-3 3"/>
        <line x1="12" y1="17" x2="12.01" y2="17"/>
      </svg>`,
      logout: `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/>
        <polyline points="16,17 21,12 16,7"/>
        <line x1="21" y1="12" x2="9" y2="12"/>
      </svg>`
    };
    return icons[iconType] || '';
  }*/
}