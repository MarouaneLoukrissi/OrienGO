import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { UserService } from '../../../Service/user.service';

// Enums pour les options des dropdowns
enum EducationLevel {
  MIDDLE_SCHOOL = 'MIDDLE_SCHOOL',
  HIGH_SCHOOL = 'HIGH_SCHOOL',
  POST_SECONDARY = 'POST_SECONDARY',
  UNIVERSITY = 'UNIVERSITY',
  GRADUATE = 'GRADUATE',
  OTHER = 'OTHER'
}

enum MessagePermission {
  NETWORK = 'NETWORK',
  ALL = 'ALL',
  NO_ONE = 'NO_ONE'
}

enum AccountPrivacy {
  PRIVATE = 'PRIVATE',
  PUBLIC = 'PUBLIC'
}

enum VisibilityStatus {
  PUBLIC = 'PUBLIC',
  NETWORK_ONLY = 'NETWORK_ONLY',
  PRIVATE = 'PRIVATE'
}

// Interface pour les utilisateurs
interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  password?: string;
  confirmPassword?: string;
  role: 'student' | 'coach';
  age?: number;
  gender?: 'male' | 'female' | 'other';
  phoneNumber?: string;
  school?: string;
  fieldOfStudy?: string;
  educationLevel?: string;
  city?: string;
  region?: string;
  country?: string;
  address?: string;
  messagePermission?: MessagePermission;
  accountPrivacy?: AccountPrivacy;
  profileVisibility?: VisibilityStatus;
  createdAt: string;
  status: 'active' | 'inactive';
}

@Component({
  selector: 'app-manage-users',
  templateUrl: './manage-users.component.html',
  styleUrl: './manage-users.component.css'
})
export class ManageUsersComponent implements OnInit {
  // Propriétés pour les modals
  showAddUserModal = false;
  showViewUserModal = false;
  showEditUserModal = false;
  selectedUser: User | null = null;
  editingUser: User | null = null;

  // Formulaires
  addUserForm: FormGroup;
  editUserForm: FormGroup;

  // Listes d'options
  educationLevels = Object.values(EducationLevel);
  messagePermissions = Object.values(MessagePermission);
  accountPrivacies = Object.values(AccountPrivacy);
  visibilityStatuses = Object.values(VisibilityStatus);

  // Propriétés pour la recherche et le tri
  searchTerm: string = '';
  selectedRole: string = '';
  selectedStatus: string = '';
  selectedDate: string = '';
  sortField: string = '';
  sortDirection: 'asc' | 'desc' = 'asc';

  // Liste originale des utilisateurs (pour la recherche)
  originalUsers: User[] = [
    {
      id: 1,
      firstName: 'Badr',
      lastName: 'Icame',
      email: 'icamebadr@gmail.com',
      role: 'student',
      age: 20,
      gender: 'male',
      phoneNumber: '+212-6-12-34-56-78',
      school: 'Université Hassan II',
      fieldOfStudy: 'Informatique',
      educationLevel: 'University',
      city: 'beni mellal',
      region: 'Bnei Mellal khenifra',
      country: 'Maroc',
      address: '123 Rue Mohammed V',
      messagePermission: MessagePermission.ALL,
      accountPrivacy: AccountPrivacy.PUBLIC,
      profileVisibility: VisibilityStatus.PUBLIC,
      createdAt: '01/07/23',
      status: 'active'
    },
    {
      id: 2,
      firstName: 'Akram',
      lastName: 'Abou',
      email: 'abdou@gmail.ma',
      role: 'coach',
      age: 35,
      gender: 'male',
      phoneNumber: '+212-6-98-76-54-32',
      messagePermission: MessagePermission.NETWORK,
      accountPrivacy: AccountPrivacy.PRIVATE,
      profileVisibility: VisibilityStatus.NETWORK_ONLY,
      createdAt: '09/02/25',
      status: 'inactive'
    },
    {
      id: 3,
      firstName: 'Sarah',
      lastName: 'Benali',
      email: 'sarah.benali@email.com',
      role: 'student',
      age: 18,
      gender: 'female',
      phoneNumber: '+212-6-11-22-33-44',
      school: 'Lycée Mohammed V',
      fieldOfStudy: 'Sciences Physiques',
      educationLevel: 'High School',
      city: 'Rabat',
      region: 'Rabat-Salé-Kénitra',
      country: 'Maroc',
      address: '45 Avenue Hassan II',
      messagePermission: MessagePermission.NETWORK,
      accountPrivacy: AccountPrivacy.PUBLIC,
      profileVisibility: VisibilityStatus.NETWORK_ONLY,
      createdAt: '15/03/24',
      status: 'active'
    },
    {
      id: 4,
      firstName: 'Ahmed',
      lastName: 'El Mansouri',
      email: 'ahmed.mansouri@email.com',
      role: 'coach',
      age: 42,
      gender: 'male',
      phoneNumber: '+212-6-55-66-77-88',
      messagePermission: MessagePermission.ALL,
      accountPrivacy: AccountPrivacy.PUBLIC,
      profileVisibility: VisibilityStatus.PUBLIC,
      createdAt: '20/01/24',
      status: 'active'
    },
    {
      id: 5,
      firstName: 'Fatima',
      lastName: 'Zahra',
      email: 'fatima.zahra@email.com',
      role: 'student',
      age: 22,
      gender: 'female',
      phoneNumber: '+212-6-99-88-77-66',
      school: 'École Nationale de Commerce et de Gestion',
      fieldOfStudy: 'Gestion des Entreprises',
      educationLevel: 'University',
      city: 'Fès',
      region: 'Fès-Meknès',
      country: 'Maroc',
      address: '78 Rue des Fleurs',
      messagePermission: MessagePermission.ALL,
      accountPrivacy: AccountPrivacy.PRIVATE,
      profileVisibility: VisibilityStatus.PRIVATE,
      createdAt: '10/05/23',
      status: 'active'
    },
    {
      id: 6,
      firstName: 'Youssef',
      lastName: 'Alaoui',
      email: 'youssef.alaoui@email.com',
      role: 'student',
      age: 19,
      gender: 'male',
      phoneNumber: '+212-6-44-33-22-11',
      school: 'Institut Supérieur de Technologie',
      fieldOfStudy: 'Génie Civil',
      educationLevel: 'Post-Secondary',
      city: 'Marrakech',
      region: 'Marrakech-Safi',
      country: 'Maroc',
      address: '12 Place Jemaa el-Fna',
      messagePermission: MessagePermission.NETWORK,
      accountPrivacy: AccountPrivacy.PUBLIC,
      profileVisibility: VisibilityStatus.NETWORK_ONLY,
      createdAt: '05/09/23',
      status: 'active'
    },
    {
      id: 7,
      firstName: 'Amina',
      lastName: 'Bouazza',
      email: 'amina.bouazza@email.com',
      role: 'coach',
      age: 38,
      gender: 'female',
      phoneNumber: '+212-6-77-88-99-00',
      messagePermission: MessagePermission.ALL,
      accountPrivacy: AccountPrivacy.PRIVATE,
      profileVisibility: VisibilityStatus.NETWORK_ONLY,
      createdAt: '12/11/23',
      status: 'active'
    },
    {
      id: 8,
      firstName: 'Hassan',
      lastName: 'Tazi',
      email: 'hassan.tazi@email.com',
      role: 'student',
      age: 25,
      gender: 'male',
      phoneNumber: '+212-6-33-44-55-66',
      school: 'Université Ibn Zohr',
      fieldOfStudy: 'Médecine',
      educationLevel: 'Graduate',
      city: 'Agadir',
      region: 'Souss-Massa',
      country: 'Maroc',
      address: '90 Boulevard Mohammed V',
      messagePermission: MessagePermission.ALL,
      accountPrivacy: AccountPrivacy.PUBLIC,
      profileVisibility: VisibilityStatus.PUBLIC,
      createdAt: '08/12/22',
      status: 'inactive'
    }
  ];

  // Liste des utilisateurs (filtrée et triée)
  users: User[] = [];

  constructor(
    private fb: FormBuilder,
    private translateService: TranslateService,
    private userService: UserService
  ) {
    // Initialiser le formulaire d'ajout
    this.addUserForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      lastName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      confirmPassword: ['', [Validators.required]],
      role: ['student', [Validators.required]],
      age: [null, [Validators.min(13), Validators.max(100)]],
      gender: [''],
      phoneNumber: ['', [Validators.pattern(/^\+?[0-9\s\-\(\)]+$/)]],
      school: [''],
      fieldOfStudy: [''],
      educationLevel: [''],
      city: [''],
      region: [''],
      country: [''],
      address: [''],
      messagePermission: [''],
      accountPrivacy: [''],
      profileVisibility: ['']
    }, { validators: this.passwordMatchValidator });

    // Initialiser le formulaire d'édition
    this.editUserForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      lastName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      email: ['', [Validators.required, Validators.email]],
      role: ['student', [Validators.required]],
      age: [null, [Validators.min(13), Validators.max(100)]],
      gender: [''],
      phoneNumber: ['', [Validators.pattern(/^\+?[0-9\s\-\(\)]+$/)]],
      school: [''],
      fieldOfStudy: [''],
      educationLevel: [''],
      city: [''],
      region: [''],
      country: [''],
      address: [''],
      messagePermission: [''],
      accountPrivacy: [''],
      profileVisibility: ['']
    });
  }

  ngOnInit(): void {
    // S'abonner aux changements de rôle pour mettre à jour la validation
    this.addUserForm.get('role')?.valueChanges.subscribe(role => {
      this.updateFormValidation(role, this.addUserForm);
    });

    // S'abonner aux changements de rôle pour le formulaire d'édition
    this.editUserForm.get('role')?.valueChanges.subscribe(role => {
      this.updateFormValidation(role, this.editUserForm);
    });

    // Initialiser la liste des utilisateurs
    this.initializeUsers();
  }

  // Méthodes pour les modals
  openAddUserModal(): void {
    this.showAddUserModal = true;
    this.addUserForm.reset({
      role: 'student',
      educationLevel: '',
      messagePermission: '',
      accountPrivacy: '',
      profileVisibility: ''
    });
    
    // Appliquer la validation immédiatement
    this.updateFormValidation('student', this.addUserForm);
    
    // S'assurer que les champs sont activés après un court délai
    setTimeout(() => {
      this.updateFormValidation('student', this.addUserForm);
    }, 50);
  }

  closeAddUserModal(): void {
    this.showAddUserModal = false;
    this.addUserForm.reset();
  }

  onSubmit(): void {
    if (this.addUserForm.valid) {
      const formValue = this.addUserForm.value;
      const newUser: User = {
        id: this.users.length + 1,
        firstName: formValue.firstName,
        lastName: formValue.lastName,
        email: formValue.email,
        role: formValue.role,
        age: formValue.age,
        gender: formValue.gender,
        phoneNumber: formValue.phoneNumber,
        school: formValue.school,
        fieldOfStudy: formValue.fieldOfStudy,
        educationLevel: formValue.educationLevel,
        city: formValue.city,
        region: formValue.region,
        country: formValue.country,
        address: formValue.address,
        messagePermission: formValue.messagePermission,
        accountPrivacy: formValue.accountPrivacy,
        profileVisibility: formValue.profileVisibility,
        createdAt: new Date().toLocaleDateString('fr-FR'),
        status: 'active'
      };
      
      this.users.push(newUser);
      this.closeAddUserModal();
    } else {
      this.markFormGroupTouched(this.addUserForm);
    }
  }

  openViewUserModal(user: User): void {
    this.selectedUser = user;
    this.showViewUserModal = true;
  }

  closeViewUserModal(): void {
    this.showViewUserModal = false;
    this.selectedUser = null;
  }

  openEditUserModal(user: User): void {
    this.editingUser = user;
    this.editUserForm.patchValue({
      firstName: user.firstName,
      lastName: user.lastName,
      email: user.email,
      role: user.role,
      age: user.age,
      gender: user.gender,
      phoneNumber: user.phoneNumber,
      school: user.school,
      fieldOfStudy: user.fieldOfStudy,
      educationLevel: user.educationLevel,
      city: user.city,
      region: user.region,
      country: user.country,
      address: user.address,
      messagePermission: user.messagePermission,
      accountPrivacy: user.accountPrivacy,
      profileVisibility: user.profileVisibility
    });
    this.showEditUserModal = true;
    
    // Appliquer la validation immédiatement
    this.updateFormValidation(user.role, this.editUserForm);
    
    // S'assurer que les champs sont activés après un court délai
    setTimeout(() => {
      this.updateFormValidation(user.role, this.editUserForm);
    }, 50);
  }

  closeEditUserModal(): void {
    this.showEditUserModal = false;
    this.editingUser = null;
  }

  onEditSubmit(): void {
    if (this.editUserForm.valid && this.editingUser) {
      const formValue = this.editUserForm.value;
      const index = this.users.findIndex(user => user.id === this.editingUser!.id);
      
      if (index !== -1) {
        this.users[index] = {
          ...this.users[index],
          firstName: formValue.firstName,
          lastName: formValue.lastName,
          email: formValue.email,
          role: formValue.role,
          age: formValue.age,
          gender: formValue.gender,
          phoneNumber: formValue.phoneNumber,
          school: formValue.school,
          fieldOfStudy: formValue.fieldOfStudy,
          educationLevel: formValue.educationLevel,
          city: formValue.city,
          region: formValue.region,
          country: formValue.country,
          address: formValue.address,
          messagePermission: formValue.messagePermission,
          accountPrivacy: formValue.accountPrivacy,
          profileVisibility: formValue.profileVisibility
        };
      }
      
      this.closeEditUserModal();
    } else {
      this.markFormGroupTouched(this.editUserForm);
    }
  }

  deleteUser(user: User): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer cet utilisateur ?')) {
      this.users = this.users.filter(u => u.id !== user.id);
    }
  }

  // Méthode pour mettre à jour la validation des formulaires
  updateFormValidation(role: string, form: FormGroup): void {
    if (role === 'student') {
      // Activer tous les champs pour les étudiants
      const studentFields = ['age', 'gender', 'phoneNumber', 'school', 'fieldOfStudy', 'educationLevel', 'city', 'region', 'country', 'address', 'messagePermission', 'accountPrivacy', 'profileVisibility'];
      
      studentFields.forEach(field => {
        const control = form.get(field);
        if (control) {
          control.enable();
          if (field === 'age') {
            control.setValidators([Validators.min(13), Validators.max(100)]);
          } else if (field === 'phoneNumber') {
            control.setValidators([Validators.pattern(/^\+?[0-9\s\-\(\)]+$/)]);
          } else if (field === 'gender') {
            control.clearValidators();
          } else if (['school', 'fieldOfStudy', 'educationLevel', 'city', 'region', 'country', 'address'].includes(field)) {
            control.setValidators([Validators.required]);
          } else {
            control.setValidators([Validators.required]);
          }
          control.updateValueAndValidity();
        }
      });
    } else if (role === 'coach') {
      // Activer seulement les champs pour les coaches
      const coachFields = ['age', 'gender', 'phoneNumber', 'messagePermission', 'accountPrivacy', 'profileVisibility'];
      const studentOnlyFields = ['school', 'fieldOfStudy', 'educationLevel', 'city', 'region', 'country', 'address'];
      
      coachFields.forEach(field => {
        const control = form.get(field);
        if (control) {
          control.enable();
          if (field === 'age') {
            control.setValidators([Validators.min(18), Validators.max(100)]);
          } else if (field === 'phoneNumber') {
            control.setValidators([Validators.pattern(/^\+?[0-9\s\-\(\)]+$/)]);
          } else if (field === 'gender') {
            control.clearValidators();
          } else {
            control.setValidators([Validators.required]);
          }
          control.updateValueAndValidity();
        }
      });

      // Désactiver les champs spécifiques aux étudiants
      studentOnlyFields.forEach(field => {
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
        return this.translateService.instant('ADMIN_USERS.MODAL.REQUIRED_FIELD');
      }
      if (control.errors['email']) {
        return this.translateService.instant('ADMIN_USERS.MODAL.INVALID_EMAIL');
      }
      if (control.errors['minlength']) {
        return this.translateService.instant('ADMIN_USERS.MODAL.MIN_LENGTH', { min: control.errors['minlength'].requiredLength });
      }
      if (control.errors['maxlength']) {
        return this.translateService.instant('ADMIN_USERS.MODAL.MAX_LENGTH', { max: control.errors['maxlength'].requiredLength });
      }
      if (control.errors['min']) {
        return this.translateService.instant('ADMIN_USERS.MODAL.MIN_VALUE', { min: control.errors['min'].min });
      }
      if (control.errors['max']) {
        return this.translateService.instant('ADMIN_USERS.MODAL.MAX_VALUE', { max: control.errors['max'].max });
      }
      if (control.errors['pattern']) {
        return this.translateService.instant('ADMIN_USERS.MODAL.INVALID_PATTERN');
      }
    }
    return '';
  }

  // Méthodes helper pour les labels des enums
  getEducationLevelLabel(value: string): string {
    try {
      return this.translateService.instant(`ADMIN_USERS.MODAL.EDUCATION_LEVEL.${value}`);
    } catch (error) {
      return value;
    }
  }

  getMessagePermissionLabel(value: string): string {
    try {
      return this.translateService.instant(`ADMIN_USERS.MODAL.MESSAGE_PERMISSION.${value}`);
    } catch (error) {
      return value;
    }
  }

  getAccountPrivacyLabel(value: string): string {
    try {
      return this.translateService.instant(`ADMIN_USERS.MODAL.ACCOUNT_PRIVACY.${value}`);
    } catch (error) {
      return value;
    }
  }

  getVisibilityStatusLabel(value: string): string {
    try {
      return this.translateService.instant(`ADMIN_USERS.MODAL.PROFILE_VISIBILITY.${value}`);
    } catch (error) {
      return value;
    }
  }

  // Méthodes helper pour les vérifications de rôle (formulaire d'ajout)
  isStudentRole(): boolean {
    return this.addUserForm.get('role')?.value === 'student';
  }

  isCoachRole(): boolean {
    return this.addUserForm.get('role')?.value === 'coach';
  }

  // Méthodes helper pour les vérifications de rôle (formulaire d'édition)
  isStudentRoleEdit(): boolean {
    return this.editUserForm.get('role')?.value === 'student';
  }

  isCoachRoleEdit(): boolean {
    return this.editUserForm.get('role')?.value === 'coach';
  }

  // Obtenir les rôles disponibles selon l'utilisateur actuel
  getAvailableRoles(): string[] {
    const currentUser = this.userService.getCurrentUser();
    if (currentUser.role === 'superAdmin') {
      return ['student', 'coach'];
    }
    return ['student', 'coach']; // Les admins peuvent ajouter étudiants et coaches
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
    let filteredUsers = [...this.originalUsers];

    // Filtre par recherche (nom, prénom, email)
    if (this.searchTerm.trim()) {
      const searchLower = this.searchTerm.toLowerCase();
      filteredUsers = filteredUsers.filter(user =>
        user.firstName.toLowerCase().includes(searchLower) ||
        user.lastName.toLowerCase().includes(searchLower) ||
        user.email.toLowerCase().includes(searchLower)
      );
    }

    // Filtre par rôle
    if (this.selectedRole && this.selectedRole !== 'all') {
      filteredUsers = filteredUsers.filter(user => user.role === this.selectedRole);
    }

    // Filtre par statut
    if (this.selectedStatus && this.selectedStatus !== 'all') {
      filteredUsers = filteredUsers.filter(user => user.status === this.selectedStatus);
    }

    // Filtre par date
    if (this.selectedDate) {
      filteredUsers = filteredUsers.filter(user => user.createdAt === this.selectedDate);
    }

    // Tri
    if (this.sortField) {
      filteredUsers.sort((a, b) => {
        let aValue: any = a[this.sortField as keyof User];
        let bValue: any = b[this.sortField as keyof User];

        // Gestion des valeurs nulles/undefined
        if (aValue === null || aValue === undefined) aValue = '';
        if (bValue === null || bValue === undefined) bValue = '';

        // Conversion en string pour la comparaison
        aValue = String(aValue).toLowerCase();
        bValue = String(bValue).toLowerCase();

        if (this.sortDirection === 'asc') {
          return aValue.localeCompare(bValue);
        } else {
          return bValue.localeCompare(aValue);
        }
      });
    }

    this.users = filteredUsers;
  }

  // Initialiser la liste des utilisateurs
  initializeUsers(): void {
    this.users = [...this.originalUsers];
  }

  // Réinitialiser tous les filtres
  resetFilters(): void {
    this.searchTerm = '';
    this.selectedRole = '';
    this.selectedStatus = '';
    this.selectedDate = '';
    this.sortField = '';
    this.sortDirection = 'asc';
    this.initializeUsers();
  }
}
