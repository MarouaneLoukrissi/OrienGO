import { Component, OnInit, OnDestroy, Input, HostListener } from '@angular/core';
import { trigger, state, style, transition, animate } from '@angular/animations';
import { Subject, takeUntil, combineLatest } from 'rxjs';
import { User, MenuItem, AuthService } from '../../Service/auth.service';
import { MenuService } from '../../Service/menu.service';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';

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
    private authService: AuthService,
    private menuService: MenuService,
    private router: Router
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
      this.authService.currentUser$
    ]).pipe(
      takeUntil(this.destroy$)
    ).subscribe(([user]) => {
      this.currentUser = user;
      this.loadMenuItems();
      // Sync active item after menu items are loaded
      this.syncActiveItemFromUrl();
    });

    // Subscribe to router events to detect URL changes
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd),
      takeUntil(this.destroy$)
    ).subscribe((event: NavigationEnd) => {
      this.syncActiveItemFromUrl();
    });

    if (this.userRole) {
      this.authService.updateUserRole(this.userRole);
    }
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private syncActiveItemFromUrl() {
    if (!this.currentUser || this.menuItems.length === 0) return;

    const currentUrl = this.router.url;
    console.log('Current URL:', currentUrl); // Debug log

    // Handle special cases first
    if (currentUrl.includes('/test/express')) {
      this.setActiveItem('riasec-test');
      return;
    }

    if (currentUrl.includes('/profile/')) {
      if (this.currentUser.role === 'coach') {
        this.setActiveItem('coach-profile');
      } else {
        this.setActiveItem('profile');
      }
      return;
    }

    // Extract the last segment of the URL for matching
    const urlSegments = currentUrl.split('/').filter(segment => segment);
    console.log('URL segments:', urlSegments); // Debug log

    if (urlSegments.length >= 2) {
      const lastSegment = urlSegments[urlSegments.length - 1];
      console.log('Last segment:', lastSegment); // Debug log

      // Try to find a menu item that matches the URL pattern
      const matchingItem = this.menuItems.find(item => {
        if (!item.route) return false;

        // Remove leading slash from route for comparison
        const routeWithoutSlash = item.route.startsWith('/') ? item.route.substring(1) : item.route;

        // Check if the route matches the last segment or the full path
        return routeWithoutSlash === lastSegment ||
              currentUrl.includes(item.route) ||
              (item.route === '/dashboard' && (
                this.currentUser &&
                (lastSegment === this.currentUser.role ||
                currentUrl === `/${this.currentUser.role}` ||
                currentUrl === `/${this.currentUser.role}/`)
              ));
      });

      console.log('Matching item:', matchingItem); // Debug log

      if (matchingItem) {
        this.setActiveItem(matchingItem.id);
        return;
      }
    }

    // Default to dashboard if no match found or if we're on the role root
    const rolePattern = new RegExp(`^/${this.currentUser.role}/?$`);
    if (rolePattern.test(currentUrl)) {
      this.setActiveItem('dashboard');
    }
  }

  private setActiveItem(itemId: string) {
    this.activeItem = itemId;

    // Update the isActive state for all menu items
    this.menuItems = this.menuItems.map(item => ({
      ...item,
      isActive: item.id === itemId
    }));
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
        this.isExpanded = savedPreferences?.desktop?.isExpanded ?? true;
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
    this.setActiveItem(itemId);

    if (this.currentDeviceType === 'mobile') {
      this.closeMobileSidebar();
    }

    if (itemId === 'logout') {
      this.handleLogout();
      return;
    }

    const selectedItem = this.menuItems.find(item => item.id === itemId);
    if (selectedItem) {
      this.router.navigate(['/' + this.currentUser?.role + '/' + selectedItem.route]);
    }
  }

  private handleLogout() {
    try {
      localStorage.removeItem(this.STORAGE_KEY);
    } catch (error) {
      console.warn('Failed to clear sidebar preferences:', error);
    }
    this.router.navigate(['/home']);
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
    this.authService.updateUserRole(role);
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

  getRoleDisplayName(role: string): string {
    const roleNames = {
      'admin': 'Administrator',
      'superAdmin': 'Super Admin',
      'student': 'Student',
      'coach': 'Coach'
    };
    return roleNames[role as keyof typeof roleNames] || role;
  }

  getRoleColor(role: string): string {
    const roleColors = {
      'admin': 'bg-blue-500',
      'superAdmin': 'bg-purple-500',
      'student': 'bg-green-500',
      'coach': 'bg-orange-500'
    };
    return roleColors[role as keyof typeof roleColors] || 'bg-gray-500';
  }
}
