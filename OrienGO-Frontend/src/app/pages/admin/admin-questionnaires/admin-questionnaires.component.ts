import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { Subject, takeUntil, finalize } from 'rxjs';

import { Category } from '../../../model/enum/Category.enum';
import { QuestionResponseDTO } from '../../../model/dto/QuestionResponse.dto';
import { QuestionWithAnswersDTO } from '../../../model/dto/QuestionWithAnswers.dto';
import { AnswerOptionFilteredDTO } from '../../../model/dto/AnswerOptionFiltered.dto';
import { AuthService, User } from '../../../Service/auth.service';
import { QuestionService } from '../../../Service/question.service';

@Component({
  selector: 'app-admin-questionnaires',
  standalone: false,
  templateUrl: './admin-questionnaires.component.html',
  styleUrl: './admin-questionnaires.component.css'
})
export class AdminQuestionnairesComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();

  Math = Math
  // Loading states
  isLoading = false;
  isSubmitting = false;

  // Propriétés pour la recherche et le tri
  searchTerm: string = '';
  selectedCategory: string = '';
  selectedStatus: string = 'all'; // Add status filter back
  sortField: string = '';
  sortDirection: 'asc' | 'desc' = 'asc';

  // Pagination properties
  currentPage = 1;
  pageSize = 5;
  totalPages = 1;
  totalItems = 0;

  // Liste des questions
  originalQuestions: QuestionResponseDTO[] = [];
  filteredQuestions: QuestionResponseDTO[] = [];
  questions: QuestionResponseDTO[] = [];

  // Propriétés pour les modales
  showAddQuestionModal: boolean = false;
  showViewQuestionModal: boolean = false;
  showEditQuestionModal: boolean = false;
  showDeleteConfirmModal: boolean = false;
  showRestoreConfirmModal: boolean = false;
  selectedQuestion: QuestionResponseDTO | null = null;
  editingQuestion: QuestionResponseDTO | null = null;
  deletingQuestion: QuestionResponseDTO | null = null;
  restoringQuestion: QuestionResponseDTO | null = null;

  // Formulaires
  addQuestionForm: FormGroup;
  editQuestionForm: FormGroup;

  // Arrays pour les enums
  categories = Object.values(Category);
  
  // Current user
  currentUser: User;
  
  constructor(
    private fb: FormBuilder,
    private translate: TranslateService,
    private questionService: QuestionService,
    private authService: AuthService
  ) {
    this.currentUser = this.authService.getCurrentUser();
    
    this.addQuestionForm = this.fb.group({
      category: [null, Validators.required],
      text: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(1000)]],
      answerOptions: this.fb.array([])
    });

    this.editQuestionForm = this.fb.group({
      category: [null, Validators.required],
      text: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(1000)]],
      answerOptions: this.fb.array([])
    });

    this.initializeAnswerOptions(this.addQuestionForm);
    this.initializeAnswerOptions(this.editQuestionForm);
  }

  ngOnInit(): void {
    this.loadQuestions();
    
    // Subscribe to user changes
    this.authService.currentUser$
      .pipe(takeUntil(this.destroy$))
      .subscribe(user => {
        this.currentUser = user;
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // Initialize 5 answer options
  initializeAnswerOptions(form: FormGroup): void {
    const answerOptionsArray = form.get('answerOptions') as FormArray;
    answerOptionsArray.clear();

    const defaultOptions = [
      'Très en désaccord',
      'En désaccord', 
      'Neutre',
      'D\'accord',
      'Tout à fait d\'accord'
    ];

    for (let i = 0; i < 5; i++) {
      answerOptionsArray.push(this.fb.group({
        optionIndex: [i + 1],
        text: [defaultOptions[i], [Validators.required, Validators.maxLength(200)]]
      }));
    }
  }

  // Get answer options form array
  getAnswerOptions(form: FormGroup): FormArray {
    return form.get('answerOptions') as FormArray;
  }

  // Load questions from API
  loadQuestions(): void {
    this.isLoading = true;
    this.questionService.getAllQuestions()
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.isLoading = false)
      )
      .subscribe({
        next: (response) => {
          if (response.data) {
            // Ensure softDeleted property is properly set for all questions
            this.originalQuestions = response.data.map(question => ({
              ...question,
              softDeleted: question.softDeleted === true // Ensure boolean value
            }));
            this.applyFilters();
            console.log('Loaded questions:', this.originalQuestions); // Debug log
          }
        },
        error: (error) => {
          console.error('Error loading questions:', error);
          // Handle error - show toast/notification
        }
      });
  }

  // Get current user role
  getCurrentUserRole(): string {
    return this.currentUser.role;
  }

  // Check if user can perform hard delete
  canHardDelete(): boolean {
    return this.currentUser.role === 'superAdmin';
  }

  // Check if user can perform soft delete
  canSoftDelete(): boolean {
    return this.currentUser.role === 'superAdmin' || this.currentUser.role === 'admin';
  }

  // Check if user can restore questions
  canRestore(): boolean {
    return this.currentUser.role === 'superAdmin' || this.currentUser.role === 'admin';
  }

  // Check if question is soft deleted
  isQuestionSoftDeleted(question: QuestionResponseDTO): boolean {
    return question.softDeleted === true;
  }

  // Check if question is active
  isQuestionActive(question: QuestionResponseDTO): boolean {
    return question.softDeleted !== true;
  }

  // Pagination methods
  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages && page !== this.currentPage) {
      this.currentPage = page;
      this.updateDisplayedQuestions();
    }
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.updateDisplayedQuestions();
    }
  }

  previousPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.updateDisplayedQuestions();
    }
  }

  getPaginationArray(): number[] {
    const pages = [];
    const start = Math.max(1, this.currentPage - 2);
    const end = Math.min(this.totalPages, this.currentPage + 2);
    
    for (let i = start; i <= end; i++) {
      pages.push(i);
    }
    return pages;
  }

  updateDisplayedQuestions(): void {
    const startIndex = (this.currentPage - 1) * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    this.questions = this.filteredQuestions.slice(startIndex, endIndex);
  }

  // Méthodes de recherche et filtrage
  onSearchChange(): void {
    this.currentPage = 1;
    this.applyFilters();
  }

  onCategoryChange(): void {
    this.currentPage = 1;
    this.applyFilters();
  }

  onStatusChange(): void {
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
    let filteredQuestions = [...this.originalQuestions];

    // Filtrage par recherche
    if (this.searchTerm.trim()) {
      filteredQuestions = filteredQuestions.filter(question =>
        question.text.toLowerCase().includes(this.searchTerm.toLowerCase())
      );
    }

    // Filtrage par catégorie
    if (this.selectedCategory) {
      filteredQuestions = filteredQuestions.filter(question =>
        (question as any).category === this.selectedCategory
      );
    }

    // Filtrage par statut
    if (this.selectedStatus && this.selectedStatus !== 'all') {
      if (this.selectedStatus === 'active') {
        filteredQuestions = filteredQuestions.filter(question => 
          this.isQuestionActive(question)
        );
      } else if (this.selectedStatus === 'deleted') {
        filteredQuestions = filteredQuestions.filter(question => 
          this.isQuestionSoftDeleted(question)
        );
      }
    }

    // Tri
    if (this.sortField) {
      filteredQuestions.sort((a, b) => {
        let aValue: any = a[this.sortField as keyof QuestionResponseDTO];
        let bValue: any = b[this.sortField as keyof QuestionResponseDTO];

        // Handle softDeleted field specifically
        if (this.sortField === 'softDeleted') {
          aValue = a.softDeleted === true ? 1 : 0;
          bValue = b.softDeleted === true ? 1 : 0;
        } else if (typeof aValue === 'string') {
          aValue = aValue.toLowerCase();
          bValue = bValue.toLowerCase();
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

    this.filteredQuestions = filteredQuestions;
    this.totalItems = filteredQuestions.length;
    this.totalPages = Math.ceil(this.totalItems / this.pageSize);
    
    // Adjust current page if necessary
    if (this.currentPage > this.totalPages && this.totalPages > 0) {
      this.currentPage = this.totalPages;
    } else if (this.totalPages === 0) {
      this.currentPage = 1;
    }

    this.updateDisplayedQuestions();
  }

  resetFilters(): void {
    this.searchTerm = '';
    this.selectedCategory = '';
    this.selectedStatus = 'all';
    this.sortField = '';
    this.sortDirection = 'asc';
    this.currentPage = 1;
    this.applyFilters();
  }

  // Méthodes pour les modales
  openAddQuestionModal(): void {
    this.showAddQuestionModal = true;
    this.addQuestionForm.reset();
    this.initializeAnswerOptions(this.addQuestionForm);
  }

  closeAddQuestionModal(): void {
    this.showAddQuestionModal = false;
  }

  openViewQuestionModal(question: QuestionResponseDTO): void {
    this.selectedQuestion = question;
    this.showViewQuestionModal = true;
  }

  closeViewQuestionModal(): void {
    this.showViewQuestionModal = false;
    this.selectedQuestion = null;
  }

  openEditQuestionModal(question: QuestionResponseDTO): void {
    this.editingQuestion = question;
    this.showEditQuestionModal = true;
    
    // Populate form with current question data
    this.editQuestionForm.patchValue({
      category: (question as any).category,
      text: question.text
    });

    // Populate answer options
    const answerOptionsArray = this.getAnswerOptions(this.editQuestionForm);
    answerOptionsArray.clear();
    
    question.answerOptions.forEach((option, index) => {
      answerOptionsArray.push(this.fb.group({
        optionIndex: [option.optionIndex],
        text: [option.text, [Validators.required, Validators.maxLength(200)]]
      }));
    });
  }

  closeEditQuestionModal(): void {
    this.showEditQuestionModal = false;
    this.editingQuestion = null;
  }

  // Delete confirmation modal
  openDeleteConfirmModal(question: QuestionResponseDTO): void {
    this.deletingQuestion = question;
    this.showDeleteConfirmModal = true;
  }

  closeDeleteConfirmModal(): void {
    this.showDeleteConfirmModal = false;
    this.deletingQuestion = null;
  }

  // Restore confirmation modal
  openRestoreConfirmModal(question: QuestionResponseDTO): void {
    this.restoringQuestion = question;
    this.showRestoreConfirmModal = true;
  }

  closeRestoreConfirmModal(): void {
    this.showRestoreConfirmModal = false;
    this.restoringQuestion = null;
  }

  // Méthodes CRUD
  onSubmit(): void {
    if (this.addQuestionForm.valid && !this.isSubmitting) {
      this.isSubmitting = true;
      const formValue = this.addQuestionForm.value;
      
      const questionData: QuestionWithAnswersDTO = {
        category: formValue.category,
        text: formValue.text,
        answerOptions: formValue.answerOptions.map((option: any) => ({
          optionIndex: option.optionIndex,
          text: option.text
        } as AnswerOptionFilteredDTO))
      };

      this.questionService.createQuestionWithAnswers(questionData)
        .pipe(
          takeUntil(this.destroy$),
          finalize(() => this.isSubmitting = false)
        )
        .subscribe({
          next: (response) => {
            if (response.data) {
              this.loadQuestions(); // Refresh the list
              this.closeAddQuestionModal();
              // Show success message
            }
          },
          error: (error) => {
            console.error('Error creating question:', error);
            // Handle error - show toast/notification
          }
        });
    }
  }

  onEditSubmit(): void {
    if (this.editQuestionForm.valid && this.editingQuestion && !this.isSubmitting) {
      this.isSubmitting = true;
      const formValue = this.editQuestionForm.value;
      
      const questionData: QuestionWithAnswersDTO = {
        category: formValue.category,
        text: formValue.text,
        answerOptions: formValue.answerOptions.map((option: any) => ({
          optionIndex: option.optionIndex,
          text: option.text
        } as AnswerOptionFilteredDTO))
      };

      this.questionService.updateQuestionWithAnswers(this.editingQuestion.id, questionData)
        .pipe(
          takeUntil(this.destroy$),
          finalize(() => this.isSubmitting = false)
        )
        .subscribe({
          next: (response) => {
            if (response.data) {
              this.loadQuestions(); // Refresh the list
              this.closeEditQuestionModal();
              // Show success message
            }
          },
          error: (error) => {
            console.error('Error updating question:', error);
            // Handle error - show toast/notification
          }
        });
    }
  }

  // Soft delete
  softDeleteQuestion(question: QuestionResponseDTO): void {
    if (!this.isSubmitting) {
      this.isSubmitting = true;
      this.questionService.softDeleteQuestion(question.id)
        .pipe(
          takeUntil(this.destroy$),
          finalize(() => this.isSubmitting = false)
        )
        .subscribe({
          next: () => {
            this.loadQuestions(); // Refresh the list
            this.closeDeleteConfirmModal();
            console.log('Question soft deleted successfully');
            // Show success message
          },
          error: (error) => {
            console.error('Error soft deleting question:', error);
            // Handle error - show toast/notification
          }
        });
    }
  }

  // Hard delete
  hardDeleteQuestion(question: QuestionResponseDTO): void {
    if (!this.isSubmitting) {
      this.isSubmitting = true;
      this.questionService.hardDeleteQuestion(question.id)
        .pipe(
          takeUntil(this.destroy$),
          finalize(() => this.isSubmitting = false)
        )
        .subscribe({
          next: () => {
            this.loadQuestions(); // Refresh the list
            this.closeDeleteConfirmModal();
            console.log('Question hard deleted successfully');
            // Show success message
          },
          error: (error) => {
            console.error('Error hard deleting question:', error);
            // Handle error - show toast/notification
          }
        });
    }
  }

  // Restore question
  restoreQuestion(question: QuestionResponseDTO): void {
    if (!this.isSubmitting) {
      this.isSubmitting = true;
      this.questionService.restoreQuestion(question.id)
        .pipe(
          takeUntil(this.destroy$),
          finalize(() => this.isSubmitting = false)
        )
        .subscribe({
          next: (response) => {
            if (response.data) {
              console.log('Question restored successfully:', response.data);
            }
            this.loadQuestions(); // Refresh the list
            this.closeRestoreConfirmModal();
            // Show success message
          },
          error: (error) => {
            console.error('Error restoring question:', error);
            // Handle error - show toast/notification
          }
        });
    }
  }

  // Méthodes utilitaires
  getCategoryLabel(category: Category): string {
    try {
      return this.translate.instant(`ADMIN_QUESTIONS.CATEGORY.${category}`);
    } catch {
      return category;
    }
  }

  getErrorMessage(field: string, form: FormGroup): string {
    const control = form.get(field);
    if (control?.errors && control.touched) {
      if (control.errors['required']) {
        return this.translate.instant('ADMIN_QUESTIONS.ERRORS.REQUIRED_FIELD');
      }
      if (control.errors['minlength']) {
        return this.translate.instant('ADMIN_QUESTIONS.ERRORS.MIN_LENGTH', { min: control.errors['minlength'].requiredLength });
      }
      if (control.errors['maxlength']) {
        return this.translate.instant('ADMIN_QUESTIONS.ERRORS.MAX_LENGTH', { max: control.errors['maxlength'].requiredLength });
      }
    }
    return '';
  }

  getAnswerOptionErrorMessage(index: number, form: FormGroup): string {
    const control = form.get(`answerOptions.${index}.text`);
    if (control?.errors && control.touched) {
      if (control.errors['required']) {
        return this.translate.instant('ADMIN_QUESTIONS.ERRORS.REQUIRED_FIELD');
      }
      if (control.errors['maxlength']) {
        return this.translate.instant('ADMIN_QUESTIONS.ERRORS.MAX_LENGTH', { max: control.errors['maxlength'].requiredLength });
      }
    }
    return '';
  }

  // Method to change user role for testing
  changeUserRole(role: 'superAdmin' | 'admin' | 'student' | 'coach'): void {
    this.authService.updateUserRole(role);
  }
}