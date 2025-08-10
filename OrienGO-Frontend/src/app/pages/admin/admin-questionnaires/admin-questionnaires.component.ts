import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';

// Enum pour les catégories RIASEC
enum Category {
  REALISTIC = 'REALISTIC',
  INVESTIGATIVE = 'INVESTIGATIVE',
  ARTISTIC = 'ARTISTIC',
  SOCIAL = 'SOCIAL',
  ENTERPRISING = 'ENTERPRISING',
  CONVENTIONAL = 'CONVENTIONAL'
}

// Interface pour les options de réponse
interface AnswerOption {
  id: number;
  text: string;
  score: number;
}

// Interface correspondant exactement à l'entité backend
interface Question {
  id: number;
  category: Category;
  text: string;
  answerOptions: AnswerOption[];
  softDeleted: boolean;
}

@Component({
  selector: 'app-admin-questionnaires',
  standalone: false,
  templateUrl: './admin-questionnaires.component.html',
  styleUrl: './admin-questionnaires.component.css'
})
export class AdminQuestionnairesComponent implements OnInit {
  // Propriétés pour la recherche et le tri
  searchTerm: string = '';
  selectedCategory: string = '';
  sortField: string = '';
  sortDirection: 'asc' | 'desc' = 'asc';

  // Liste originale des questions (pour la recherche)
  originalQuestions: Question[] = [
    {
      id: 1,
      category: Category.REALISTIC,
      text: "J'aime travailler avec des outils et des machines",
      answerOptions: [
        { id: 1, text: "Pas du tout d'accord", score: 1 },
        { id: 2, text: "Pas d'accord", score: 2 },
        { id: 3, text: "Neutre", score: 3 },
        { id: 4, text: "D'accord", score: 4 },
        { id: 5, text: "Tout à fait d'accord", score: 5 }
      ],
      softDeleted: false
    },
    {
      id: 2,
      category: Category.INVESTIGATIVE,
      text: "J'aime résoudre des problèmes complexes",
      answerOptions: [
        { id: 6, text: "Pas du tout d'accord", score: 1 },
        { id: 7, text: "Pas d'accord", score: 2 },
        { id: 8, text: "Neutre", score: 3 },
        { id: 9, text: "D'accord", score: 4 },
        { id: 10, text: "Tout à fait d'accord", score: 5 }
      ],
      softDeleted: false
    },
    {
      id: 3,
      category: Category.ARTISTIC,
      text: "J'aime créer des choses originales",
      answerOptions: [
        { id: 11, text: "Pas du tout d'accord", score: 1 },
        { id: 12, text: "Pas d'accord", score: 2 },
        { id: 13, text: "Neutre", score: 3 },
        { id: 14, text: "D'accord", score: 4 },
        { id: 15, text: "Tout à fait d'accord", score: 5 }
      ],
      softDeleted: false
    },
    {
      id: 4,
      category: Category.SOCIAL,
      text: "J'aime aider les autres",
      answerOptions: [
        { id: 16, text: "Pas du tout d'accord", score: 1 },
        { id: 17, text: "Pas d'accord", score: 2 },
        { id: 18, text: "Neutre", score: 3 },
        { id: 19, text: "D'accord", score: 4 },
        { id: 20, text: "Tout à fait d'accord", score: 5 }
      ],
      softDeleted: false
    },
    {
      id: 5,
      category: Category.ENTERPRISING,
      text: "J'aime diriger et prendre des décisions",
      answerOptions: [
        { id: 21, text: "Pas du tout d'accord", score: 1 },
        { id: 22, text: "Pas d'accord", score: 2 },
        { id: 23, text: "Neutre", score: 3 },
        { id: 24, text: "D'accord", score: 4 },
        { id: 25, text: "Tout à fait d'accord", score: 5 }
      ],
      softDeleted: false
    },
    {
      id: 6,
      category: Category.CONVENTIONAL,
      text: "J'aime organiser et structurer les informations",
      answerOptions: [
        { id: 26, text: "Pas du tout d'accord", score: 1 },
        { id: 27, text: "Pas d'accord", score: 2 },
        { id: 28, text: "Neutre", score: 3 },
        { id: 29, text: "D'accord", score: 4 },
        { id: 30, text: "Tout à fait d'accord", score: 5 }
      ],
      softDeleted: false
    }
  ];

  // Liste des questions (filtrée et triée)
  questions: Question[] = [];

  // Propriétés pour les modales
  showAddQuestionModal: boolean = false;
  showViewQuestionModal: boolean = false;
  showEditQuestionModal: boolean = false;
  selectedQuestion: Question | null = null;
  editingQuestion: Question | null = null;

  // Formulaires
  addQuestionForm: FormGroup;
  editQuestionForm: FormGroup;

  // Arrays pour les enums
  categories = Object.values(Category);

  constructor(
    private fb: FormBuilder,
    private translate: TranslateService
  ) {
    this.addQuestionForm = this.fb.group({
      category: ['', Validators.required],
      text: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(1000)]],
      answerOptions: this.fb.array([])
    });

    this.editQuestionForm = this.fb.group({
      category: ['', Validators.required],
      text: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(1000)]],
      answerOptions: this.fb.array([])
    });
  }

  ngOnInit(): void {
    this.initializeQuestions();
  }

  // Initialisation des questions
  initializeQuestions(): void {
    this.questions = [...this.originalQuestions];
  }

  // Méthodes de recherche et filtrage
  onSearchChange(): void {
    this.applyFilters();
  }

  onCategoryChange(): void {
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
        question.category === this.selectedCategory
      );
    }

    // Tri
    if (this.sortField) {
      filteredQuestions.sort((a, b) => {
        let aValue: any = a[this.sortField as keyof Question];
        let bValue: any = b[this.sortField as keyof Question];

        if (typeof aValue === 'string') {
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

    this.questions = filteredQuestions;
  }

  resetFilters(): void {
    this.searchTerm = '';
    this.selectedCategory = '';
    this.sortField = '';
    this.sortDirection = 'asc';
    this.questions = [...this.originalQuestions];
  }

  // Méthodes pour les modales
  openAddQuestionModal(): void {
    this.showAddQuestionModal = true;
    this.addQuestionForm.reset();
  }

  closeAddQuestionModal(): void {
    this.showAddQuestionModal = false;
  }

  openViewQuestionModal(question: Question): void {
    this.selectedQuestion = question;
    this.showViewQuestionModal = true;
  }

  closeViewQuestionModal(): void {
    this.showViewQuestionModal = false;
    this.selectedQuestion = null;
  }

  openEditQuestionModal(question: Question): void {
    this.editingQuestion = question;
    this.showEditQuestionModal = true;
    this.editQuestionForm.patchValue({
      category: question.category,
      text: question.text
    });
  }

  closeEditQuestionModal(): void {
    this.showEditQuestionModal = false;
    this.editingQuestion = null;
  }

  // Méthodes CRUD
  onSubmit(): void {
    if (this.addQuestionForm.valid) {
      const formValue = this.addQuestionForm.value;
      const newQuestion: Question = {
        id: this.questions.length + 1,
        category: formValue.category,
        text: formValue.text,
        answerOptions: [
          { id: this.questions.length * 5 + 1, text: "Pas du tout d'accord", score: 1 },
          { id: this.questions.length * 5 + 2, text: "Pas d'accord", score: 2 },
          { id: this.questions.length * 5 + 3, text: "Neutre", score: 3 },
          { id: this.questions.length * 5 + 4, text: "D'accord", score: 4 },
          { id: this.questions.length * 5 + 5, text: "Tout à fait d'accord", score: 5 }
        ],
        softDeleted: false
      };

      this.originalQuestions.push(newQuestion);
      this.applyFilters();
      this.closeAddQuestionModal();
    }
  }

  onEditSubmit(): void {
    if (this.editQuestionForm.valid && this.editingQuestion) {
      const formValue = this.editQuestionForm.value;
      const index = this.originalQuestions.findIndex(q => q.id === this.editingQuestion!.id);
      
      if (index !== -1) {
        this.originalQuestions[index] = {
          ...this.originalQuestions[index],
          category: formValue.category,
          text: formValue.text
        };
        this.applyFilters();
        this.closeEditQuestionModal();
      }
    }
  }

  deleteQuestion(question: Question): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer cette question ?')) {
      this.originalQuestions = this.originalQuestions.filter(q => q.id !== question.id);
      this.applyFilters();
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
}
