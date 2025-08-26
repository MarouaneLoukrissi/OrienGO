import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { AuthService } from '../../../Service/auth.service';
import { AdminService } from '../../../Service/admin.service';
import { AdminReturnDTO } from '../../../model/dto/AdminReturn.dto';
import { AdminDTO } from '../../../model/dto/Admin.dto';
import { AdminModifyDTO } from '../../../model/dto/AdminModify.dto';
import { AdminLevel } from '../../../model/enum/AdminLevel.dto';
import { Department } from '../../../model/enum/Department.enum';
import { GenderType } from '../../../model/enum/GenderType.enum';

@Component({
  selector: 'app-manage-admins',
  standalone: false,
  templateUrl: './manage-admins.component.html',
  styleUrl: './manage-admins.component.css'
})
export class ManageAdminsComponent implements OnInit {
  // Loading states
  loading = false;
  loadingAction = false;

  // View modes
  viewMode: 'active' | 'deleted' = 'active';

  // Modals
  showAddAdminModal = false;
  showViewAdminModal = false;
  showEditAdminModal = false;
  showDeleteConfirmModal = false;
  selectedAdmin: AdminReturnDTO | null = null;
  editingAdmin: AdminReturnDTO | null = null;

  // Delete modal state
  deleteType: 'soft' | 'hard' = 'soft';

  // Forms
  addAdminForm!: FormGroup;
  editAdminForm!: FormGroup;

  // Enums for dropdowns
  adminLevels = Object.values(AdminLevel);
  departments = Object.values(Department);
  genderTypes = Object.values(GenderType);

  // Search and filter properties
  searchTerm = '';
  selectedRole = '';
  selectedStatus = '';
  selectedDate = '';
  sortField = '';
  sortDirection: 'asc' | 'desc' = 'asc';

  // Admin lists
  originalAdmins: AdminReturnDTO[] = [];
  admins: AdminReturnDTO[] = [];

  // Pagination properties
  currentPage = 1;
  pageSize = 5;
  totalPages = 0;
  paginatedAdmins: AdminReturnDTO[] = [];

  // Error handling
  errorMessage = '';
  successMessage = '';

  constructor(
    private fb: FormBuilder,
    private translateService: TranslateService,
    private authService: AuthService,
    private adminService: AdminService
  ) {
    this.initializeForms();
  }

  ngOnInit(): void {
    this.loadAdmins();
    this.setupFormValidation();
  }

  // Initialize forms
  initializeForms(): void {
    this.addAdminForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      lastName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      confirmPassword: ['', [Validators.required]],
      age: [null, [Validators.required, Validators.min(18), Validators.max(100)]],
      gender: [null, [Validators.required]],
      phoneNumber: ['', [Validators.pattern(/^\+?[0-9\s\-()]+$/)]],
      adminLevel: [AdminLevel.STANDARD_ADMIN, [Validators.required]],
      department: [Department.TECH, [Validators.required]],
      enabled: [true],
      suspended: [false],
      suspensionReason: [''],
      suspendedUntil: [''],
      roles: [['ADMIN']]
    }, { validators: this.passwordMatchValidator });

    this.editAdminForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      lastName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      age: [null, [Validators.required, Validators.min(18), Validators.max(100)]],
      gender: [null, [Validators.required]],
      phoneNumber: ['', [Validators.pattern(/^\+?[0-9\s\-\(\)]+$/)]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      confirmPassword: ['', [Validators.required]],
      adminLevel: [AdminLevel.STANDARD_ADMIN, [Validators.required]],
      department: [Department.TECH, [Validators.required]],
      enabled: [true],
      tokenExpired: [false],
      suspended: [false],
      suspensionReason: [''],
      suspendedUntil: [''],
      roles: [['ADMIN']]
    }, { validators: this.passwordMatchValidator });
  }

  // Setup form validation
  setupFormValidation(): void {
    this.addAdminForm.get('adminLevel')?.valueChanges.subscribe(level => {
      this.updateRolesBasedOnLevel(level, this.addAdminForm);
    });

    this.editAdminForm.get('adminLevel')?.valueChanges.subscribe(level => {
      this.updateRolesBasedOnLevel(level, this.editAdminForm);
    });

    this.addAdminForm.get('suspended')?.valueChanges.subscribe(suspended => {
      this.updateSuspensionValidation(suspended, this.addAdminForm);
    });

    this.editAdminForm.get('suspended')?.valueChanges.subscribe(suspended => {
      this.updateSuspensionValidation(suspended, this.editAdminForm);
    });
  }

  // Update roles based on admin level
  updateRolesBasedOnLevel(level: AdminLevel, form: FormGroup): void {
    const rolesControl = form.get('roles');
    if (level === AdminLevel.MANAGER) {
      rolesControl?.setValue(['ADMIN', 'MANAGER']);
    } else {
      rolesControl?.setValue(['ADMIN']);
    }
  }

  // Update suspension validation
  updateSuspensionValidation(suspended: boolean, form: FormGroup): void {
    const suspensionReasonControl = form.get('suspensionReason');
    if (suspended) {
      suspensionReasonControl?.setValidators([Validators.required]);
    } else {
      suspensionReasonControl?.clearValidators();
      suspensionReasonControl?.setValue('');
    }
    suspensionReasonControl?.updateValueAndValidity();
  }

  // Load admins from backend
  loadAdmins(): void {
    this.loading = true;
    this.errorMessage = '';
    this.clearMessages();

    // Always load all admins and filter locally for better consistency
    // This ensures we have complete data and can switch views without additional API calls
    const loadActiveAdmins = this.adminService.getActiveAdmins();
    const loadDeletedAdmins = this.adminService.getDeletedAdmins();
    
    // Use Promise.all to load both active and deleted admins
    Promise.all([
      loadActiveAdmins.toPromise(),
      loadDeletedAdmins.toPromise()
    ]).then(([activeResponse, deletedResponse]) => {
      const activeAdmins = (activeResponse?.status === 200) ? (activeResponse.data || []) : [];
      const deletedAdmins = (deletedResponse?.status === 200) ? (deletedResponse.data || []) : [];
      
      // Mark active admins as not deleted
      activeAdmins.forEach((admin: AdminReturnDTO) => {
        admin.isDeleted = false;
      });
      
      // Mark deleted admins as deleted
      deletedAdmins.forEach((admin: AdminReturnDTO) => {
        admin.isDeleted = true;
      });
      
      // Combine both lists
      this.originalAdmins = [...activeAdmins, ...deletedAdmins];
      this.currentPage = 1;
      this.applyFilters();
      this.loading = false;
    }).catch((error) => {
      // Fallback to original method if Promise.all fails
      const loadMethod = this.viewMode === 'active' 
        ? this.adminService.getActiveAdmins() 
        : this.adminService.getDeletedAdmins();

      loadMethod.subscribe({
        next: (response) => {
          if (response.status === 200) {
            this.originalAdmins = response.data || [];
            // Set isDeleted flag based on current view mode
            this.originalAdmins.forEach(admin => {
              admin.isDeleted = this.viewMode === 'deleted';
            });
            this.currentPage = 1;
            this.applyFilters();
          } else {
            this.errorMessage = response.message;
          }
          this.loading = false;
        },
        error: (error) => {
          this.errorMessage = error.error?.message || 'Failed to load admins';
          this.loading = false;
        }
      });
    });
  }

  // Switch between active and deleted admins
  switchViewMode(mode: 'active' | 'deleted'): void {
    this.viewMode = mode;
    this.resetFilters();
    this.loadAdmins();
  }

  // Modal methods
  openAddAdminModal(): void {
    this.showAddAdminModal = true;
    this.addAdminForm.reset({
      enabled: true,
      suspended: false,
      adminLevel: AdminLevel.STANDARD_ADMIN,
      department: Department.TECH,
      roles: ['ADMIN']
    });
  }

  closeAddAdminModal(): void {
    this.showAddAdminModal = false;
    this.addAdminForm.reset();
    this.clearMessages();
  }

  openViewAdminModal(admin: AdminReturnDTO): void {
    this.selectedAdmin = admin;
    this.showViewAdminModal = true;
  }

  closeViewAdminModal(): void {
    this.showViewAdminModal = false;
    this.selectedAdmin = null;
  }

  openEditAdminModal(admin: AdminReturnDTO): void {
    this.editingAdmin = admin;
    this.editAdminForm.patchValue({
      firstName: admin.firstName,
      lastName: admin.lastName,
      age: admin.age,
      gender: admin.gender,
      phoneNumber: admin.phoneNumber,
      password: '', // Don't pre-fill password
      confirmPassword: '', // Don't pre-fill confirm password
      adminLevel: admin.adminLevel,
      department: admin.department,
      enabled: admin.enabled,
      tokenExpired: admin.tokenExpired,
      suspended: admin.suspended,
      suspensionReason: admin.suspensionReason || '',
      suspendedUntil: admin.suspendedUntil || ''
    });
    this.showEditAdminModal = true;
    this.closeViewAdminModal();
  }

  closeEditAdminModal(): void {
    this.showEditAdminModal = false;
    this.editingAdmin = null;
    this.clearMessages();
  }

  openDeleteConfirmModal(admin: AdminReturnDTO, type: 'soft' | 'hard' = 'soft'): void {
    this.selectedAdmin = admin;
    this.deleteType = type;
    this.showDeleteConfirmModal = true;
  }

  closeDeleteConfirmModal(): void {
    this.showDeleteConfirmModal = false;
    this.selectedAdmin = null;
    this.deleteType = 'soft';
  }

  // CRUD Operations
  onSubmit(): void {
    if (this.addAdminForm.valid) {
      this.loadingAction = true;
      this.clearMessages();

      const formValue = this.addAdminForm.value;
      const currentUser = this.authService.getCurrentUser();
      
      const adminDTO: AdminDTO = {
        firstName: formValue.firstName,
        lastName: formValue.lastName,
        age: formValue.age,
        gender: formValue.gender,
        phoneNumber: formValue.phoneNumber,
        email: formValue.email,
        password: formValue.password,
        enabled: formValue.enabled,
        suspended: formValue.suspended,
        suspensionReason: formValue.suspensionReason,
        suspendedUntil: formValue.suspendedUntil,
        adminLevel: formValue.adminLevel,
        createdById: Number(currentUser.id),
        department: formValue.department
      };

      this.adminService.createAdminAllFields(adminDTO).subscribe({
        next: (response) => {
          if (response.status === 201 || response.status === 200) {
            this.successMessage = 'Admin created successfully';
            this.loadAdmins();
          } else {
            this.errorMessage = response.message;
          }
          this.loadingAction = false;
          this.closeAddAdminModal();
        },
        error: (error) => {
          this.errorMessage = error.error?.message || 'Failed to create admin';
          this.loadingAction = false;
        }
      });
    } else {
      this.markFormGroupTouched(this.addAdminForm);
    }
  }

  onEditSubmit(): void {
    if (this.editAdminForm.valid && this.editingAdmin) {
      this.loadingAction = true;
      this.clearMessages();

      const formValue = this.editAdminForm.value;

      const adminModifyDTO: AdminModifyDTO = {
        firstName: formValue.firstName,
        lastName: formValue.lastName,
        age: formValue.age,
        gender: formValue.gender,
        phoneNumber: formValue.phoneNumber,
        password: formValue.password,
        enabled: formValue.enabled,
        tokenExpired: formValue.tokenExpired,
        suspended: formValue.suspended,
        suspensionReason: formValue.suspensionReason,
        suspendedUntil: formValue.suspendedUntil,
        adminLevel: formValue.adminLevel,
        department: formValue.department
      };

      this.adminService.updateAdminAllFields(this.editingAdmin.id, adminModifyDTO).subscribe({
        next: (response) => {
          if (response.status === 200) {
            this.successMessage = 'Admin updated successfully';
            this.loadAdmins();
          } else {
            this.errorMessage = response.message;
          }
          this.loadingAction = false;
          this.closeEditAdminModal();
        },
        error: (error) => {
          this.errorMessage = error.error?.message || 'Failed to update admin';
          this.loadingAction = false;
        }
      });
    } else {
      this.markFormGroupTouched(this.editAdminForm);
    }
  }

  confirmDelete(): void {
    if (!this.selectedAdmin) return;

    this.loadingAction = true;
    this.clearMessages();

    const deleteMethod = this.deleteType === 'hard' 
      ? this.adminService.hardDeleteAdmin(this.selectedAdmin.id)
      : this.adminService.softDeleteAdmin(this.selectedAdmin.id);

    deleteMethod.subscribe({
      next: (response) => {
        if (response.status === 200) {
          this.successMessage = `Admin ${this.deleteType === 'hard' ? 'permanently' : 'temporarily'} deleted successfully`;
          // Immediately update the local data to reflect the change
          if (this.selectedAdmin) {
            if (this.deleteType === 'soft') {
              // For soft delete, mark as deleted and remove from active view
              const adminIndex = this.originalAdmins.findIndex(admin => admin.id === this.selectedAdmin!.id);
              if (adminIndex !== -1) {
                this.originalAdmins[adminIndex].isDeleted = true;
                this.originalAdmins[adminIndex].deletedAt = new Date().toISOString();
              }
            } else {
              // For hard delete, remove completely from the list
              this.originalAdmins = this.originalAdmins.filter(admin => admin.id !== this.selectedAdmin!.id);
            }
            
            // Reapply filters to update the view immediately
            this.applyFilters();
          }
          
          this.closeDeleteConfirmModal();
          
          // Also reload data in background to ensure sync with backend
          setTimeout(() => {
            this.loadAdmins();
          },100);
        } else {
          this.errorMessage = response.message;
          this.closeDeleteConfirmModal();
        }
        this.loadingAction = false;
      },
      error: (error) => {
        this.errorMessage = error.error?.message || 'Failed to delete admin';
        this.loadingAction = false;
        this.closeDeleteConfirmModal();
      }
    });
  }

  restoreAdmin(admin: AdminReturnDTO): void {
    this.loadingAction = true;
    this.clearMessages();

    this.adminService.restoreAdmin(admin.id).subscribe({
      next: (response) => {
        if (response.status === 200) {
          this.successMessage = 'Admin restored successfully';
          
          // Immediately update the local data
          const adminIndex = this.originalAdmins.findIndex(a => a.id === admin.id);
          if (adminIndex !== -1) {
            this.originalAdmins[adminIndex].isDeleted = false;
            this.originalAdmins[adminIndex].deletedAt = undefined;
          }
          
          // Reapply filters to update the view immediately
          this.applyFilters();
          
          // Also reload data in background to ensure sync with backend
          setTimeout(() => {
            this.loadAdmins();
          }, 100);
        } else {
          this.errorMessage = response.message;
        }
        this.loadingAction = false;
      },
      error: (error) => {
        this.errorMessage = error.error?.message || 'Failed to restore admin';
        this.loadingAction = false;
      }
    });
  }

  // Pagination methods
  updatePagination(): void {
    this.totalPages = Math.ceil(this.admins.length / this.pageSize);
    
    // Ensure current page is valid
    if (this.currentPage > this.totalPages && this.totalPages > 0) {
      this.currentPage = this.totalPages;
    }
    if (this.currentPage < 1) {
      this.currentPage = 1;
    }

    // Calculate start and end indexes
    const startIndex = (this.currentPage - 1) * this.pageSize;
    const endIndex = startIndex + this.pageSize;

    // Get paginated admins
    this.paginatedAdmins = this.admins.slice(startIndex, endIndex);
  }

  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
      this.updatePagination();
    }
  }

  goToNextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.updatePagination();
    }
  }

  goToPreviousPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.updatePagination();
    }
  }

  getPageNumbers(): number[] {
    const pages: number[] = [];
    const maxPagesToShow = 5;
    
    if (this.totalPages <= maxPagesToShow) {
      for (let i = 1; i <= this.totalPages; i++) {
        pages.push(i);
      }
    } else {
      const half = Math.floor(maxPagesToShow / 2);
      let start = Math.max(1, this.currentPage - half);
      let end = Math.min(this.totalPages, start + maxPagesToShow - 1);
      
      if (end - start < maxPagesToShow - 1) {
        start = Math.max(1, end - maxPagesToShow + 1);
      }
      
      for (let i = start; i <= end; i++) {
        pages.push(i);
      }
    }
    
    return pages;
  }

  getStartIndex(): number {
    return (this.currentPage - 1) * this.pageSize;
  }

  getEndIndex(): number {
    return Math.min(this.getStartIndex() + this.pageSize, this.admins.length);
  }

  // Utility methods
  passwordMatchValidator(form: FormGroup): { [key: string]: boolean } | null {
    const password = form.get('password');
    const confirmPassword = form.get('confirmPassword');

    if (password && confirmPassword && password.value !== confirmPassword.value) {
      return { passwordMismatch: true };
    }
    return null;
  }

  markFormGroupTouched(form: FormGroup): void {
    Object.keys(form.controls).forEach(key => {
      const control = form.get(key);
      control?.markAsTouched();
    });
  }

  clearMessages(): void {
    this.errorMessage = '';
    this.successMessage = '';
  }

  getErrorMessage(controlName: string, form: FormGroup): string {
    const control = form.get(controlName);
    if (control && control.errors && control.touched) {
      if (control.errors['required']) {
        return this.translateService.instant('ADMIN_ADMINS.MODAL.REQUIRED_FIELD');
      }
      if (control.errors['email']) {
        return this.translateService.instant('ADMIN_ADMINS.MODAL.INVALID_EMAIL');
      }
      if (control.errors['minlength']) {
        return this.translateService.instant('ADMIN_ADMINS.MODAL.MIN_LENGTH', { min: control.errors['minlength'].requiredLength });
      }
      if (control.errors['maxlength']) {
        return this.translateService.instant('ADMIN_ADMINS.MODAL.MAX_LENGTH', { max: control.errors['maxlength'].requiredLength });
      }
      if (control.errors['min']) {
        return this.translateService.instant('ADMIN_ADMINS.MODAL.MIN_VALUE', { min: control.errors['min'].min });
      }
      if (control.errors['max']) {
        return this.translateService.instant('ADMIN_ADMINS.MODAL.MAX_VALUE', { max: control.errors['max'].max });
      }
      if (control.errors['pattern']) {
        return this.translateService.instant('ADMIN_ADMINS.MODAL.INVALID_PATTERN');
      }
    }
    return '';
  }

  // Helper methods for labels
  getAdminLevelLabel(value: AdminLevel): string {
    switch (value) {
      case AdminLevel.MANAGER:
        return 'Manager';
      case AdminLevel.STANDARD_ADMIN:
        return 'Standard Admin';
      default:
        return value;
    }
  }

  getDepartmentLabel(value: Department): string {
    try {
      return this.translateService.instant(`ADMIN_ADMINS.MODAL.DEPARTMENT.${value}`);
    } catch (error) {
      return value;
    }
  }

  getGenderLabel(value: GenderType): string {
    try {
      return this.translateService.instant(`ADMIN_ADMINS.MODAL.GENDER_SELECTION.${value}`);
    } catch (error) {
      return value;
    }
  }

  // Permission checks
  getAvailableAdminLevels(): AdminLevel[] {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser.role === 'superAdmin') {
      return Object.values(AdminLevel);
    }
    return [AdminLevel.STANDARD_ADMIN];
  }

  isCurrentUserSuperAdmin(): boolean {
    return this.authService.getCurrentUser().role === 'superAdmin';
  }

  // Search and filter methods
  onSearchChange(): void {
    this.currentPage = 1;
    this.applyFilters();
  }

  onRoleChange(): void {
    this.currentPage = 1;
    this.applyFilters();
  }

  onStatusChange(): void {
    this.currentPage = 1;
    this.applyFilters();
  }

  onDateChange(): void {
    this.currentPage = 1;
    this.applyFilters();
  }

  onSort(field: string): void {
    if (this.sortField === field) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortField = field;
      this.sortDirection = 'asc';
    }
    this.currentPage = 1;
    this.applyFilters();
  }

  applyFilters(): void {
    let filteredAdmins = [...this.originalAdmins];

    // Filter based on view mode first
    if (this.viewMode === 'active') {
      filteredAdmins = filteredAdmins.filter(admin => !admin.isDeleted);
    } else {
      filteredAdmins = filteredAdmins.filter(admin => admin.isDeleted);
    }

    // Search filter
    if (this.searchTerm.trim()) {
      const searchLower = this.searchTerm.toLowerCase();
      filteredAdmins = filteredAdmins.filter(admin =>
        admin.firstName.toLowerCase().includes(searchLower) ||
        admin.lastName.toLowerCase().includes(searchLower) ||
        admin.email.toLowerCase().includes(searchLower)
      );
    }

    // Role filter
    if (this.selectedRole) {
      filteredAdmins = filteredAdmins.filter(admin => 
        admin.adminLevel === this.selectedRole
      );
    }

    // Status filter
    if (this.selectedStatus) {
      if (this.selectedStatus === 'active') {
        filteredAdmins = filteredAdmins.filter(admin => admin.enabled && !admin.suspended);
      } else if (this.selectedStatus === 'inactive') {
        filteredAdmins = filteredAdmins.filter(admin => !admin.enabled || admin.suspended);
      }
    }

    // Date filter
    if (this.selectedDate) {
      filteredAdmins = filteredAdmins.filter(admin => {
        const adminDate = new Date(admin.createdAt || '').toDateString();
        const filterDate = new Date(this.selectedDate).toDateString();
        return adminDate === filterDate;
      });
    }

    // Sort
    if (this.sortField) {
      filteredAdmins.sort((a, b) => {
        let aValue: any;
        let bValue: any;

        switch (this.sortField) {
          case 'name':
            aValue = `${a.firstName} ${a.lastName}`.toLowerCase();
            bValue = `${b.firstName} ${b.lastName}`.toLowerCase();
            break;
          case 'email':
            aValue = a.email.toLowerCase();
            bValue = b.email.toLowerCase();
            break;
          case 'adminLevel':
            aValue = a.adminLevel;
            bValue = b.adminLevel;
            break;
          case 'status':
            aValue = a.enabled && !a.suspended ? 'active' : 'inactive';
            bValue = b.enabled && !b.suspended ? 'active' : 'inactive';
            break;
          case 'createdAt':
            aValue = new Date(a.createdAt || '').getTime();
            bValue = new Date(b.createdAt || '').getTime();
            break;
          default:
            return 0;
        }

        if (aValue < bValue) {
          return this.sortDirection === 'asc' ? -1 : 1;
        }
        if (aValue > bValue) {
          return this.sortDirection === 'asc' ? 1 : -1;
        }
        return 0;
      });
    }

    this.admins = filteredAdmins;
    this.updatePagination();
  }

  resetFilters(): void {
    this.searchTerm = '';
    this.selectedRole = '';
    this.selectedStatus = '';
    this.selectedDate = '';
    this.sortField = '';
    this.sortDirection = 'asc';
    this.currentPage = 1;
    this.applyFilters();
  }

  // Helper methods for status checking
  isAdminActive(admin: AdminReturnDTO): boolean {
    return admin.enabled && !admin.suspended && !admin.isDeleted;
  }

  getStatusLabel(admin: AdminReturnDTO): string {
    if (admin.isDeleted) return 'Deleted';
    if (admin.suspended) return 'Suspended';
    if (!admin.enabled) return 'Disabled';
    return 'Active';
  }

  getStatusClass(admin: AdminReturnDTO): string {
    if (admin.isDeleted) return 'bg-gray-100 text-gray-800';
    if (admin.suspended) return 'bg-yellow-100 text-yellow-800';
    if (!admin.enabled) return 'bg-red-100 text-red-800';
    return 'bg-green-100 text-green-800';
  }
}