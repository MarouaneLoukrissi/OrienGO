import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { AuthService } from '../../../Service/auth.service';

// Enums pour les options des dropdowns
enum EducationLevel {
  HIGH_SCHOOL = 'HIGH_SCHOOL',
  BACHELOR = 'BACHELOR',
  MASTER = 'MASTER',
  PHD = 'PHD',
  OTHER = 'OTHER'
}

enum MessagePermission {
  ALL = 'ALL',
  CONTACTS_ONLY = 'CONTACTS_ONLY',
  NONE = 'NONE'
}

enum AccountPrivacy {
  PUBLIC = 'PUBLIC',
  PRIVATE = 'PRIVATE',
  FRIENDS_ONLY = 'FRIENDS_ONLY'
}

enum VisibilityStatus {
  VISIBLE = 'VISIBLE',
  HIDDEN = 'HIDDEN'
}

enum Department {
  TECH = 'TECH',
  OPERATIONS = 'OPERATIONS',
  HR = 'HR',
  FINANCE = 'FINANCE',
  OTHERS = 'OTHERS'
}

// Interface pour les admins
interface Admin {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  password?: string;
  confirmPassword?: string;
  role: 'admin' | 'superAdmin';
  age?: number;
  gender?: 'male' | 'female' | 'other';
  phoneNumber?: string;
  messagePermission?: MessagePermission;
  accountPrivacy?: AccountPrivacy;
  profileVisibility?: VisibilityStatus;
  department?: Department;
  createdAt: string;
  status: 'active' | 'inactive';
}

@Component({
  selector: 'app-manage-admins',
  standalone: false,
  templateUrl: './manage-admins.component.html',
  styleUrl: './manage-admins.component.css'
})
export class ManageAdminsComponent implements OnInit {
  // Propriétés pour les modals
  showAddAdminModal = false;
  showViewAdminModal = false;
  showEditAdminModal = false;
  selectedAdmin: Admin | null = null;
  editingAdmin: Admin | null = null;

  // Formulaires
  addAdminForm: FormGroup;
  editAdminForm: FormGroup;

  // Listes d'options
  educationLevels = Object.values(EducationLevel);
  messagePermissions = Object.values(MessagePermission);
  accountPrivacies = Object.values(AccountPrivacy);
  visibilityStatuses = Object.values(VisibilityStatus);
  departments = Object.values(Department);

  // Propriétés pour la recherche et le tri
  searchTerm = '';
  selectedRole = '';
  selectedStatus = '';
  selectedDate = '';
  sortField = '';
  sortDirection: 'asc' | 'desc' = 'asc';

  // Liste originale des admins (pour la recherche)
  originalAdmins: Admin[] = [
    {
      id: 1,
      firstName: 'Ikram',
      lastName: 'Tifard',
      email: 'ikra@gmail.com',
      role: 'admin',
      age: 28,
      gender: 'female',
      phoneNumber: '+212-6-12-34-56-78',
      messagePermission: MessagePermission.ALL,
      accountPrivacy: AccountPrivacy.PUBLIC,
      profileVisibility: VisibilityStatus.VISIBLE,
      department: Department.HR,
      createdAt: '01/07/23',
      status: 'active'
    },
    {
      id: 2,
      firstName: 'Akram',
      lastName: 'Abou',
      email: 'Abdou@gmail.ma',
      role: 'admin',
      age: 32,
      gender: 'male',
      phoneNumber: '+212-6-98-76-54-32',
      messagePermission: MessagePermission.CONTACTS_ONLY,
      accountPrivacy: AccountPrivacy.PRIVATE,
      profileVisibility: VisibilityStatus.VISIBLE,
      department: Department.FINANCE,
      createdAt: '09/02/25',
      status: 'inactive'
    },
    {
      id: 3,
      firstName: 'Karim',
      lastName: 'Benjelloun',
      email: 'karim.benjelloun@company.com',
      role: 'superAdmin',
      age: 45,
      gender: 'male',
      phoneNumber: '+212-6-11-22-33-44',
      messagePermission: MessagePermission.ALL,
      accountPrivacy: AccountPrivacy.PUBLIC,
      profileVisibility: VisibilityStatus.VISIBLE,
      department: Department.TECH,
      createdAt: '15/03/20',
      status: 'active'
    },
    {
      id: 4,
      firstName: 'Nadia',
      lastName: 'El Fassi',
      email: 'nadia.elfassi@company.com',
      role: 'admin',
      age: 35,
      gender: 'female',
      phoneNumber: '+212-6-55-66-77-88',
      messagePermission: MessagePermission.CONTACTS_ONLY,
      accountPrivacy: AccountPrivacy.FRIENDS_ONLY,
      profileVisibility: VisibilityStatus.VISIBLE,
      department: Department.OPERATIONS,
      createdAt: '20/01/22',
      status: 'active'
    },
    {
      id: 5,
      firstName: 'Omar',
      lastName: 'Alaoui',
      email: 'omar.alaoui@company.com',
      role: 'admin',
      age: 29,
      gender: 'male',
      phoneNumber: '+212-6-99-88-77-66',
      messagePermission: MessagePermission.ALL,
      accountPrivacy: AccountPrivacy.PRIVATE,
      profileVisibility: VisibilityStatus.HIDDEN,
      department: Department.TECH,
      createdAt: '10/05/21',
      status: 'active'
    },
    {
      id: 6,
      firstName: 'Leila',
      lastName: 'Bouazza',
      email: 'leila.bouazza@company.com',
      role: 'admin',
      age: 31,
      gender: 'female',
      phoneNumber: '+212-6-44-33-22-11',
      messagePermission: MessagePermission.NONE,
      accountPrivacy: AccountPrivacy.PRIVATE,
      profileVisibility: VisibilityStatus.VISIBLE,
      department: Department.HR,
      createdAt: '05/09/23',
      status: 'active'
    },
    {
      id: 7,
      firstName: 'Youssef',
      lastName: 'Tazi',
      email: 'youssef.tazi@company.com',
      role: 'superAdmin',
      age: 52,
      gender: 'male',
      phoneNumber: '+212-6-77-88-99-00',
      messagePermission: MessagePermission.ALL,
      accountPrivacy: AccountPrivacy.PUBLIC,
      profileVisibility: VisibilityStatus.VISIBLE,
      department: Department.FINANCE,
      createdAt: '12/11/19',
      status: 'active'
    },
    {
      id: 8,
      firstName: 'Amina',
      lastName: 'Mansouri',
      email: 'amina.mansouri@company.com',
      role: 'admin',
      age: 27,
      gender: 'female',
      phoneNumber: '+212-6-33-44-55-66',
      messagePermission: MessagePermission.CONTACTS_ONLY,
      accountPrivacy: AccountPrivacy.FRIENDS_ONLY,
      profileVisibility: VisibilityStatus.VISIBLE,
      department: Department.OTHERS,
      createdAt: '08/12/23',
      status: 'inactive'
    }
  ];

  // Liste des admins (filtrée et triée)
  admins: Admin[] = [];

  constructor(
    private fb: FormBuilder,
    private translateService: TranslateService,
    private authService: AuthService
  ) {
    // Initialiser le formulaire d'ajout
    this.addAdminForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      lastName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      confirmPassword: ['', [Validators.required]],
      role: ['admin', [Validators.required]],
      age: [null, [Validators.min(18), Validators.max(100)]],
      gender: [null],
      phoneNumber: ['', [Validators.pattern(/^\+?[0-9\s\-\(\)]+$/)]],
      messagePermission: [MessagePermission.ALL, [Validators.required]],
      accountPrivacy: [AccountPrivacy.PUBLIC, [Validators.required]],
      profileVisibility: [VisibilityStatus.VISIBLE, [Validators.required]],
      department: [Department.TECH, [Validators.required]]
    }, { validators: this.passwordMatchValidator });

    // Initialiser le formulaire d'édition
    this.editAdminForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      lastName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      email: ['', [Validators.required, Validators.email]],
      role: ['admin', [Validators.required]],
      age: [null, [Validators.min(18), Validators.max(100)]],
      gender: [''],
      phoneNumber: ['', [Validators.pattern(/^\+?[0-9\s\-\(\)]+$/)]],
      messagePermission: [MessagePermission.ALL, [Validators.required]],
      accountPrivacy: [AccountPrivacy.PUBLIC, [Validators.required]],
      profileVisibility: [VisibilityStatus.VISIBLE, [Validators.required]],
      department: [Department.TECH, [Validators.required]]
    });
  }

  ngOnInit(): void {
    // Initialiser la liste des admins
    this.initializeAdmins();

    // S'abonner aux changements de rôle pour mettre à jour la validation
    this.addAdminForm.get('role')?.valueChanges.subscribe(role => {
      this.updateFormValidation(role, this.addAdminForm);
    });

    // S'abonner aux changements de rôle pour le formulaire d'édition
    this.editAdminForm.get('role')?.valueChanges.subscribe(role => {
      this.updateFormValidation(role, this.editAdminForm);
    });
  }

  // Méthodes pour les modals
  openAddAdminModal(): void {
    this.showAddAdminModal = true;
    this.addAdminForm.reset({
      role: 'admin',
      messagePermission: MessagePermission.ALL,
      accountPrivacy: AccountPrivacy.PUBLIC,
      profileVisibility: VisibilityStatus.VISIBLE,
      department: Department.TECH
    });
    
    // Appliquer la validation immédiatement
    this.updateFormValidation('admin', this.addAdminForm);
    
    // S'assurer que les champs sont activés après un court délai
    setTimeout(() => {
      this.updateFormValidation('admin', this.addAdminForm);
    }, 50);
  }

  closeAddAdminModal(): void {
    this.showAddAdminModal = false;
    this.addAdminForm.reset();
  }

  onSubmit(): void {
    if (this.addAdminForm.valid) {
      const formValue = this.addAdminForm.value;
      const newAdmin: Admin = {
        id: this.originalAdmins.length + 1,
        firstName: formValue.firstName,
        lastName: formValue.lastName,
        email: formValue.email,
        role: formValue.role,
        age: formValue.age,
        gender: formValue.gender,
        phoneNumber: formValue.phoneNumber,
        messagePermission: formValue.messagePermission,
        accountPrivacy: formValue.accountPrivacy,
        profileVisibility: formValue.profileVisibility,
        department: formValue.department,
        createdAt: new Date().toLocaleDateString('fr-FR'),
        status: 'active'
      };
      
      this.originalAdmins.push(newAdmin);
      this.applyFilters(); // Mettre à jour la liste filtrée
      this.closeAddAdminModal();
    } else {
      this.markFormGroupTouched(this.addAdminForm);
    }
  }

  openViewAdminModal(admin: Admin): void {
    this.selectedAdmin = admin;
    this.showViewAdminModal = true;
  }

  closeViewAdminModal(): void {
    this.showViewAdminModal = false;
    this.selectedAdmin = null;
  }

  openEditAdminModal(admin: Admin): void {
    this.editingAdmin = admin;
    this.editAdminForm.patchValue({
      firstName: admin.firstName,
      lastName: admin.lastName,
      email: admin.email,
      role: admin.role,
      age: admin.age,
      gender: admin.gender,
      phoneNumber: admin.phoneNumber,
      messagePermission: admin.messagePermission,
      accountPrivacy: admin.accountPrivacy,
      profileVisibility: admin.profileVisibility,
      department: admin.department
    });
    this.showEditAdminModal = true;
    
    // Appliquer la validation immédiatement
    this.updateFormValidation(admin.role, this.editAdminForm);
    
    // S'assurer que les champs sont activés après un court délai
    setTimeout(() => {
      this.updateFormValidation(admin.role, this.editAdminForm);
    }, 50);
  }

  closeEditAdminModal(): void {
    this.showEditAdminModal = false;
    this.editingAdmin = null;
  }

  onEditSubmit(): void {
    if (this.editAdminForm.valid && this.editingAdmin) {
      const formValue = this.editAdminForm.value;
      const index = this.originalAdmins.findIndex(admin => admin.id === this.editingAdmin!.id);
      
      if (index !== -1) {
        this.originalAdmins[index] = {
          ...this.originalAdmins[index],
          firstName: formValue.firstName,
          lastName: formValue.lastName,
          email: formValue.email,
          role: formValue.role,
          age: formValue.age,
          gender: formValue.gender,
          phoneNumber: formValue.phoneNumber,
          messagePermission: formValue.messagePermission,
          accountPrivacy: formValue.accountPrivacy,
          profileVisibility: formValue.profileVisibility,
          department: formValue.department
        };
        this.applyFilters(); // Mettre à jour la liste filtrée
      }
      
      this.closeEditAdminModal();
    } else {
      this.markFormGroupTouched(this.editAdminForm);
    }
  }

  deleteAdmin(admin: Admin): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer cet admin ?')) {
      this.originalAdmins = this.originalAdmins.filter(a => a.id !== admin.id);
      this.applyFilters(); // Mettre à jour la liste filtrée
    }
  }

  // Méthode pour mettre à jour la validation des formulaires
  updateFormValidation(role: string, form: FormGroup): void {
    // Activer tous les champs pour admin et superAdmin
    if (role === 'admin' || role === 'superAdmin') {
      const commonFields = ['age', 'gender', 'phoneNumber', 'messagePermission', 'accountPrivacy', 'profileVisibility', 'department'];
      
      commonFields.forEach(field => {
        const control = form.get(field);
        if (control) {
          control.enable();
          if (field === 'age') {
            control.setValidators([Validators.min(18), Validators.max(100)]);
          } else if (field === 'phoneNumber') {
            control.setValidators([Validators.pattern(/^\+?[0-9\s\-\(\)]+$/)]);
          } else if (field === 'gender') {
            // Pas de validateur requis pour le genre
            control.clearValidators();
          } else {
            // Pour les dropdowns, s'assurer qu'ils ont des validateurs requis
            control.setValidators([Validators.required]);
          }
          control.updateValueAndValidity();
        }
      });
    } else {
      // Désactiver les champs pour les autres rôles
      const roleSpecificFields = ['age', 'gender', 'phoneNumber', 'messagePermission', 'accountPrivacy', 'profileVisibility', 'department'];
      
      roleSpecificFields.forEach(field => {
        const control = form.get(field);
        if (control) {
          control.disable();
          control.clearValidators();
          control.updateValueAndValidity();
        }
      });
    }
  }

  // Validateur personnalisé pour la confirmation du mot de passe
  passwordMatchValidator(form: FormGroup): { [key: string]: boolean } | null {
    const password = form.get('password');
    const confirmPassword = form.get('confirmPassword');
    
    if (password && confirmPassword && password.value !== confirmPassword.value) {
      return { passwordMismatch: true };
    }
    return null;
  }

  // Marquer tous les contrôles comme touchés pour afficher les erreurs
  markFormGroupTouched(form: FormGroup): void {
    Object.keys(form.controls).forEach(key => {
      const control = form.get(key);
      control?.markAsTouched();
    });
  }

  // Obtenir les messages d'erreur traduits
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

  // Méthodes helper pour les labels des enums
  getMessagePermissionLabel(value: string): string {
    try {
      return this.translateService.instant(`ADMIN_ADMINS.MODAL.MESSAGE_PERMISSION.${value}`);
    } catch (error) {
      return value; // Retourner la valeur brute si la traduction échoue
    }
  }

  getAccountPrivacyLabel(value: string): string {
    try {
      return this.translateService.instant(`ADMIN_ADMINS.MODAL.ACCOUNT_PRIVACY.${value}`);
    } catch (error) {
      return value; // Retourner la valeur brute si la traduction échoue
    }
  }

  getVisibilityStatusLabel(value: string): string {
    try {
      return this.translateService.instant(`ADMIN_ADMINS.MODAL.PROFILE_VISIBILITY.${value}`);
    } catch (error) {
      return value; // Retourner la valeur brute si la traduction échoue
    }
  }

  getDepartmentLabel(value: string): string {
    try {
      return this.translateService.instant(`ADMIN_ADMINS.MODAL.DEPARTMENT.${value}`);
    } catch (error) {
      return value; // Retourner la valeur brute si la traduction échoue
    }
  }

  // Méthodes helper pour les vérifications de rôle (formulaire d'ajout)
  isAdminRole(): boolean {
    return this.addAdminForm.get('role')?.value === 'admin';
  }

  isSuperAdminRole(): boolean {
    return this.addAdminForm.get('role')?.value === 'superAdmin';
  }

  isAdminOrSuperAdminRole(): boolean {
    const role = this.addAdminForm.get('role')?.value;
    return role === 'admin' || role === 'superAdmin';
  }

  // Méthodes helper pour les vérifications de rôle (formulaire d'édition)
  isAdminRoleEdit(): boolean {
    return this.editAdminForm.get('role')?.value === 'admin';
  }

  isSuperAdminRoleEdit(): boolean {
    return this.editAdminForm.get('role')?.value === 'superAdmin';
  }

  isAdminOrSuperAdminRoleEdit(): boolean {
    const role = this.editAdminForm.get('role')?.value;
    return role === 'admin' || role === 'superAdmin';
  }

  // Obtenir les rôles disponibles selon l'utilisateur actuel
  getAvailableRoles(): string[] {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser.role === 'superAdmin') {
      return ['admin', 'superAdmin'];
    }
    return ['admin']; // Les admins normaux ne peuvent ajouter que des admins
  }

  // Vérifier si l'utilisateur actuel est super admin
  isCurrentUserSuperAdmin(): boolean {
    return this.authService.getCurrentUser().role === 'superAdmin';
  }

  // Méthodes de recherche et de tri
  onSearchChange(): void {
    this.applyFilters();
  }

  onRoleChange(): void {
    this.applyFilters();
  }

  onStatusChange(): void {
    this.applyFilters();
  }

  onDateChange(): void {
    this.applyFilters();
  }

  onSort(field: string): void {
    if (this.sortField === field) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortField = field;
      this.sortDirection = 'asc';
    }
    this.applyFilters();
  }

  applyFilters(): void {
    let filteredAdmins = [...this.originalAdmins];

    // Filtre par recherche
    if (this.searchTerm.trim()) {
      const searchLower = this.searchTerm.toLowerCase();
      filteredAdmins = filteredAdmins.filter(admin =>
        admin.firstName.toLowerCase().includes(searchLower) ||
        admin.lastName.toLowerCase().includes(searchLower) ||
        admin.email.toLowerCase().includes(searchLower)
      );
    }

    // Filtre par rôle
    if (this.selectedRole) {
      filteredAdmins = filteredAdmins.filter(admin => admin.role === this.selectedRole);
    }

    // Filtre par statut
    if (this.selectedStatus) {
      filteredAdmins = filteredAdmins.filter(admin => admin.status === this.selectedStatus);
    }

    // Filtre par date
    if (this.selectedDate) {
      filteredAdmins = filteredAdmins.filter(admin => admin.createdAt.includes(this.selectedDate));
    }

    // Tri
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
          case 'role':
            aValue = a.role.toLowerCase();
            bValue = b.role.toLowerCase();
            break;
          case 'status':
            aValue = a.status.toLowerCase();
            bValue = b.status.toLowerCase();
            break;
          case 'createdAt':
            aValue = a.createdAt;
            bValue = b.createdAt;
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
  }

  resetFilters(): void {
    this.searchTerm = '';
    this.selectedRole = '';
    this.selectedStatus = '';
    this.selectedDate = '';
    this.sortField = '';
    this.sortDirection = 'asc';
    this.initializeAdmins();
  }

  initializeAdmins(): void {
    this.admins = [...this.originalAdmins];
  }
}
