import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subject, forkJoin, of } from 'rxjs';
import { takeUntil, catchError, switchMap, map } from 'rxjs/operators';
import { CoachStudentConnectionService } from '../../../Service/CoachStudentConnection.service';
import { MediaResponseDTO, MediaService } from '../../../Service/media.service';
import { AuthService } from '../../../Service/auth.service';
import { ConnectionStatus } from '../../../model/enum/ConnectionStatus.enum';
import { CoachStudentConnectionResponseDTO } from '../../../model/dto/CoachStudentConnectionResponse.dto';
import { StudentResponseDTO } from '../../../model/dto/StudentResponse.dto';
import { CoachStudentConnectionUpdateDTO } from '../../../model/dto/CoachStudentConnectionUpdate.dto';
import { CoachStudentConnectionCreateDTO } from '../../../model/dto/CoachStudentConnectionCreate.dto';
import { RequestInitiator } from '../../../model/enum/RequestInitiator.enum';
import { StudentService } from '../../../Service/student.service';

export interface StudentNetwork {
  id: number;
  connectionId: number;
  name: string;
  email: string;
  connectedDate: string;
  profileImage: string;
  coachId: number;
  studentId: number;
  status: string;
  requestedBy: string;
}

@Component({
  selector: 'app-networks',
  templateUrl: './network-coach.component.html',
  styleUrl: './network-coach.component.css'
})
export class NetworkCoachComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();

  activeTab: 'network' | 'sent-requests' | 'received-requests' = 'network';
  sortBy = 'recent';
  searchQuery = '';
  newStudentCode = '';

  // Dynamic data from backend
  acceptedConnections: StudentNetwork[] = [];
  sentRequests: StudentNetwork[] = []; // Requests I sent (waiting for student response)
  receivedRequests: StudentNetwork[] = []; // Requests I received from students

  // Pagination properties
  currentPage = 1;
  pageSize = 5;
  totalPages = 0;
  totalItems = 0;
  paginatedConnections: StudentNetwork[] = [];

  // Loading states
  isLoading = false;
  isLoadingConnections = false;
  isSearchingStudent = false;
  isSendingRequest = false;

  // Current user
  currentUserId: number | null = null;

  // Error handling
  errorMessage = '';
  successMessage = '';

  constructor(
    private connectionService: CoachStudentConnectionService,
    private studentService: StudentService,
    private mediaService: MediaService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.initializeComponent();
    this.loadConnections();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private initializeComponent(): void {
    // Get current coach ID from auth service
    this.currentUserId = 6; // This should come from AuthService in real implementation
  }

  loadConnections(): void {
    if (!this.currentUserId) return;

    this.isLoadingConnections = true;
    this.errorMessage = '';

    // Get ALL connections for this coach - both accepted and pending
    this.connectionService.getAll().pipe(
      takeUntil(this.destroy$),
      map(response => {
        const allConnections = response.data || [];

        // Filter connections that involve this coach
        const coachConnections = allConnections.filter(conn =>
          conn.coachId === this.currentUserId
        );

        // Separate connections by status and initiator
        const acceptedConnections = coachConnections.filter(conn =>
          conn.status === ConnectionStatus.ACCEPTED
        );

        const pendingConnections = coachConnections.filter(conn =>
          conn.status === ConnectionStatus.PENDING
        );

        // For pending connections, separate by who initiated
        const sentRequests = pendingConnections.filter(conn =>
          conn.requestedBy === RequestInitiator.COACH
        );

        const receivedRequests = pendingConnections.filter(conn =>
          conn.requestedBy === RequestInitiator.STUDENT
        );

        console.log('All connections:', allConnections);
        console.log('Coach connections:', coachConnections);
        console.log('Accepted connections:', acceptedConnections);
        console.log('Sent requests:', sentRequests);
        console.log('Received requests:', receivedRequests);

        return {
          acceptedConnections,
          sentRequests,
          receivedRequests
        };
      }),
      switchMap(({ acceptedConnections, sentRequests, receivedRequests }) => {
        // Get student details for all connections
        const allConnections = [...acceptedConnections, ...sentRequests, ...receivedRequests];
        const studentIds = [...new Set(allConnections.map(conn => conn.studentId))];

        if (studentIds.length === 0) {
          return of({
            acceptedConnections,
            sentRequests,
            receivedRequests,
            students: [],
            media: []
          });
        }

        const studentRequests = studentIds.map(id =>
          this.studentService.getStudentById(id).pipe(
            catchError(err => {
              console.error(`Error fetching student ${id}:`, err);
              return of({ data: null });
            })
          )
        );

        const mediaRequests = studentIds.map(id =>
          this.mediaService.getLatestMediaByUserId(id).pipe(
            catchError(err => {
              console.error(`Error fetching media for student ${id}:`, err);
              return of({ data: [] });
            })
          )
        );

        return forkJoin({
          students: forkJoin(studentRequests),
          media: forkJoin(mediaRequests)
        }).pipe(
          map(({ students, media }) => ({
            acceptedConnections,
            sentRequests,
            receivedRequests,
            students: students.map(response => response.data).filter((s): s is StudentResponseDTO => s !== null),
            media: media.flatMap(response => response.data || [])
          }))
        );
      }),
      catchError(error => {
        console.error('Error loading connections:', error);
        this.errorMessage = 'Failed to load connections. Please try again.';
        return of({
          acceptedConnections: [],
          sentRequests: [],
          receivedRequests: [],
          students: [],
          media: []
        });
      })
    ).subscribe(({ acceptedConnections, sentRequests, receivedRequests, students, media }) => {
      console.log('Final mapping - Accepted:', acceptedConnections.length, 'Sent:', sentRequests.length, 'Received:', receivedRequests.length);

      this.acceptedConnections = this.mapConnectionsToStudentNetwork(acceptedConnections, students, media);
      this.sentRequests = this.mapConnectionsToStudentNetwork(sentRequests, students, media);
      this.receivedRequests = this.mapConnectionsToStudentNetwork(receivedRequests, students, media);

      console.log('Mapped connections - Accepted:', this.acceptedConnections.length, 'Sent:', this.sentRequests.length, 'Received:', this.receivedRequests.length);

      this.updatePagination();
      this.isLoadingConnections = false;
    });
  }

  private mapConnectionsToStudentNetwork(
    connections: CoachStudentConnectionResponseDTO[],
    students: StudentResponseDTO[],
    media: MediaResponseDTO[]
  ): StudentNetwork[] {
    return connections.map(connection => {
      const student = students.find(s => s.id === connection.studentId);
      const studentMedia = media.find(m => m.userId === connection.studentId);

      return {
        id: connection.id,
        connectionId: connection.id,
        name: student ? `${student.firstName} ${student.lastName}` : 'Unknown Student',
        email: student?.email || 'No email',
        connectedDate: this.formatDate(connection.requestedAt),
        profileImage: studentMedia?.url || 'https://www.shutterstock.com/image-vector/blank-avatar-photo-place-holder-600nw-1095249842.jpg',
        coachId: connection.coachId,
        studentId: connection.studentId,
        status: connection.status,
        requestedBy: connection.requestedBy
      };
    });
  }

  private formatDate(dateString: string): string {
    try {
      const date = new Date(dateString);
      return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      });
    } catch {
      return dateString;
    }
  }

  getFilteredConnections(): StudentNetwork[] {
    let currentConnections: StudentNetwork[] = [];

    switch (this.activeTab) {
      case 'network':
        currentConnections = this.acceptedConnections;
        break;
      case 'sent-requests':
        currentConnections = this.sentRequests;
        break;
      case 'received-requests':
        currentConnections = this.receivedRequests;
        break;
    }

    let filtered = currentConnections.filter(student =>
      student.name.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
      student.email.toLowerCase().includes(this.searchQuery.toLowerCase())
    );

    // Sort implementation
    switch (this.sortBy) {
      case 'name':
        filtered.sort((a, b) => a.name.localeCompare(b.name));
        break;
      case 'oldest':
        filtered.sort((a, b) => new Date(a.connectedDate).getTime() - new Date(b.connectedDate).getTime());
        break;
      case 'recent':
      default:
        filtered.sort((a, b) => new Date(b.connectedDate).getTime() - new Date(a.connectedDate).getTime());
        break;
    }

    // Update pagination info
    this.totalItems = filtered.length;
    this.totalPages = Math.ceil(this.totalItems / this.pageSize);

    // Ensure current page is valid
    if (this.currentPage > this.totalPages && this.totalPages > 0) {
      this.currentPage = 1;
    }

    // Return paginated results
    const startIndex = (this.currentPage - 1) * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    this.paginatedConnections = filtered.slice(startIndex, endIndex);

    return this.paginatedConnections;
  }

  private updatePagination(): void {
    this.currentPage = 1; // Reset to first page when data changes
    this.getFilteredConnections(); // Update pagination calculations
  }

  // Pagination methods
  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
      this.getFilteredConnections();
    }
  }

  goToPreviousPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.getFilteredConnections();
    }
  }

  goToNextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.getFilteredConnections();
    }
  }

  getPageNumbers(): number[] {
    const maxVisiblePages = 5;
    const pages: number[] = [];

    if (this.totalPages <= maxVisiblePages) {
      for (let i = 1; i <= this.totalPages; i++) {
        pages.push(i);
      }
    } else {
      const startPage = Math.max(1, this.currentPage - Math.floor(maxVisiblePages / 2));
      const endPage = Math.min(this.totalPages, startPage + maxVisiblePages - 1);

      for (let i = startPage; i <= endPage; i++) {
        pages.push(i);
      }
    }

    return pages;
  }

  getPaginationInfo(): string {
    if (this.totalItems === 0) {
      return 'No connections found';
    }

    const startItem = (this.currentPage - 1) * this.pageSize + 1;
    const endItem = Math.min(this.currentPage * this.pageSize, this.totalItems);

    return `Showing ${startItem}-${endItem} of ${this.totalItems} connections`;
  }

  onSearchChange(): void {
    this.currentPage = 1;
    this.getFilteredConnections();
  }

  onSortChange(): void {
    this.currentPage = 1;
    this.getFilteredConnections();
  }

  getCurrentTabCount(): number {
    switch (this.activeTab) {
      case 'network':
        return this.acceptedConnections.length;
      case 'sent-requests':
        return this.sentRequests.length;
      case 'received-requests':
        return this.receivedRequests.length;
      default:
        return 0;
    }
  }

  getCurrentTabLabel(): string {
    switch (this.activeTab) {
      case 'network':
        return 'COACH_NETWORKS.CONNECTIONS';
      case 'sent-requests':
        return 'COACH_NETWORKS.SENT_REQUESTS';
      case 'received-requests':
        return 'COACH_NETWORKS.RECEIVED_REQUESTS';
      default:
        return '';
    }
  }

  getEmptyStateMessage(): string {
    switch (this.activeTab) {
      case 'network':
        return 'COACH_NETWORKS.EMPTY_NETWORK';
      case 'sent-requests':
        return 'COACH_NETWORKS.EMPTY_SENT_REQUESTS';
      case 'received-requests':
        return 'COACH_NETWORKS.EMPTY_RECEIVED_REQUESTS';
      default:
        return 'COACH_NETWORKS.EMPTY_STATE';
    }
  }

  sendMessage(student: StudentNetwork): void {
    console.log('Sending message to:', student);
    // TODO: Implement message functionality
    this.showSuccessMessage(`Message functionality will be implemented for ${student.name}`);
  }

  acceptConnection(student: StudentNetwork): void {
    if (!this.currentUserId) return;

    this.isLoading = true;
    const updateData: CoachStudentConnectionUpdateDTO = {
      coachId: this.currentUserId,
      studentId: student.studentId,
      status: ConnectionStatus.ACCEPTED,
      respondedAt: new Date().toISOString(),
      requestedBy: RequestInitiator.STUDENT // Keep original initiator
    };

    this.connectionService.update(student.connectionId, updateData)
      .pipe(
        takeUntil(this.destroy$),
        catchError(error => {
          console.error('Error accepting connection:', error);
          this.showErrorMessage('Failed to accept connection. Please try again.');
          return of(null);
        })
      )
      .subscribe(response => {
        this.isLoading = false;
        if (response && response.data) {
          this.showSuccessMessage(`Connection with ${student.name} accepted successfully!`);
          this.loadConnections(); // Refresh the lists
        }
      });
  }

  declineConnection(student: StudentNetwork): void {
    if (!confirm(`Are you sure you want to decline the connection request from ${student.name}?`)) {
      return;
    }

    this.isLoading = true;

    this.connectionService.delete(student.connectionId)
      .pipe(
        takeUntil(this.destroy$),
        catchError(error => {
          console.error('Error declining connection:', error);
          this.showErrorMessage('Failed to decline connection. Please try again.');
          return of(null);
        })
      )
      .subscribe(response => {
        this.isLoading = false;
        if (response) {
          this.showSuccessMessage(`Connection request from ${student.name} has been declined.`);
          this.receivedRequests = this.receivedRequests.filter(s => s.id !== student.id);
          this.updatePagination();
        }
      });
  }

  cancelRequest(student: StudentNetwork): void {
    if (!confirm(`Are you sure you want to cancel your connection request to ${student.name}?`)) {
      return;
    }

    this.isLoading = true;

    this.connectionService.delete(student.connectionId)
      .pipe(
        takeUntil(this.destroy$),
        catchError(error => {
          console.error('Error cancelling request:', error);
          this.showErrorMessage('Failed to cancel request. Please try again.');
          return of(null);
        })
      )
      .subscribe(response => {
        this.isLoading = false;
        if (response) {
          this.showSuccessMessage(`Connection request to ${student.name} has been cancelled.`);
          this.sentRequests = this.sentRequests.filter(s => s.id !== student.id);
          this.updatePagination();
        }
      });
  }

  removeConnection(student: StudentNetwork): void {
    if (!confirm(`Are you sure you want to remove ${student.name} from your network?`)) {
      return;
    }

    this.isLoading = true;

    this.connectionService.delete(student.connectionId)
      .pipe(
        takeUntil(this.destroy$),
        catchError(error => {
          console.error('Error removing connection:', error);
          this.showErrorMessage('Failed to remove connection. Please try again.');
          return of(null);
        })
      )
      .subscribe(response => {
        this.isLoading = false;
        if (response) {
          this.showSuccessMessage(`${student.name} has been removed from your network.`);
          this.acceptedConnections = this.acceptedConnections.filter(s => s.id !== student.id);
          this.updatePagination();
        }
      });
  }

  sendRequest(): void {
    if (!this.newStudentCode.trim() || !this.currentUserId) return;

    this.isSendingRequest = true;
    this.errorMessage = '';

    // First, search for student by email
    this.studentService.getStudentByEmail(this.newStudentCode.trim())
      .pipe(
        takeUntil(this.destroy$),
        switchMap(studentResponse => {
          if (!studentResponse.data) {
            throw new Error('Student not found with this email address.');
          }

          const student = studentResponse.data;

          // Check if connection already exists
          return this.connectionService.getAll().pipe(
            map(allConnections => {
              const existingConnection = allConnections.data?.find(conn =>
                conn.coachId === this.currentUserId && conn.studentId === student.id
              );

              if (existingConnection) {
                throw new Error('Connection already exists with this student.');
              }

              return student;
            })
          );
        }),
        switchMap(student => {
          // Create new connection request
          const createData: CoachStudentConnectionCreateDTO = {
            coachId: this.currentUserId!,
            studentId: student.id,
            requestedBy: RequestInitiator.COACH
          };

          return this.connectionService.create(createData);
        }),
        catchError(error => {
          console.error('Error sending request:', error);

          let errorMsg = 'This user is already in your network';

          if (error.status === 404) {
            errorMsg = 'No student found with this email address.';
          } else if (error.status === 400) {
            errorMsg = 'Invalid request. Please check the email address and try again.';
          } else if (error.status === 500) {
            errorMsg = 'The server encountered an error. Please try again later.';
          } else if (!navigator.onLine) {
            errorMsg = 'You appear to be offline. Please check your internet connection.';
          }

          this.showErrorMessage(errorMsg);
          return of(null);
        })
      )
      .subscribe(response => {
        this.isSendingRequest = false;
        if (response && response.data) {
          this.showSuccessMessage('Connection request sent successfully!');
          this.newStudentCode = '';
          this.loadConnections(); // Refresh to show new pending request
        }
      });
  }

  refreshConnections(): void {
    this.loadConnections();
  }

  onTabChange(tab: 'network' | 'sent-requests' | 'received-requests'): void {
    this.activeTab = tab;
    this.searchQuery = ''; // Clear search when switching tabs
    this.currentPage = 1; // Reset pagination when switching tabs
    this.getFilteredConnections();
  }

  private showSuccessMessage(message: string): void {
    this.successMessage = message;
    this.errorMessage = '';
    setTimeout(() => {
      this.successMessage = '';
    }, 5000);
  }

  private showErrorMessage(message: string): void {
    this.errorMessage = message;
    this.successMessage = '';
    setTimeout(() => {
      this.errorMessage = '';
    }, 5000);
  }
}
