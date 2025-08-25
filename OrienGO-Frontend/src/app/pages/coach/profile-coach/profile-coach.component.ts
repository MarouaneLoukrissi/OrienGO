import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { finalize, forkJoin } from 'rxjs';
import { GenderType } from '../../../model/enum/GenderType.enum';
import { CoachSpecialization } from '../../../model/enum/CoachSpecialization.enum';
import { CoachResponseDTO } from '../../../model/dto/CoachResponse.dto';
import { CoachService } from '../../../Service/Coach.service';
import { CoachStudentConnectionService } from '../../../Service/CoachStudentConnection.service';
import { MediaResponseDTO, MediaService } from '../../../Service/media.service';
import { MessagePermission } from '../../../model/enum/MessagePermission.enum';
import { CoachUpdateProfileDTO } from '../../../model/dto/CoachUpdateProfile.dto';
import { MediaType } from '../../../model/enum/MediaType.enum';
import { NotificationService } from '../../../Service/notification.service';
import { TestResultAverageDTO } from '../../../model/dto/TestResultAverage.dto';
import { Category } from '../../../model/enum/Category.enum';

interface StatCard {
  value: string;
  label: string;
  iconPath: string;
  bgColor: string;
  iconColor: string;
}

interface GenderOption {
  value: GenderType;
  label: string;
}

interface SpecializationOption {
  value: CoachSpecialization;
  label: string;
}

interface DominantProfileData {
  profileType: string;
  description: string;
  percentage: number;
}

@Component({
  selector: 'app-profile-coach',
  templateUrl: './profile-coach.component.html',
  styleUrls: ['./profile-coach.component.css']
})
export class ProfileCoachComponent implements OnInit {
  @ViewChild('profilePictureInput') profilePictureInput!: ElementRef;
  @ViewChild('coverPhotoInput') coverPhotoInput!: ElementRef;
  Math = Math;
  
  // Component State
  coachProfile: CoachResponseDTO | null = null;
  isLoading = true;
  error: string | null = null;
  isOwner = false;
  showEditModal = false;
  showExpertiseModal = false;
  isUpdating = false;
  isUploadingProfilePicture = false;
  isUploadingCoverPhoto = false;
  currentUserId : number = 3;

  // Dynamic data properties
  studentsCoached = 0;
  coachRating = 0;
  dominantProfile: DominantProfileData | null = null;
  isLoadingStats = true;
  isLoadingDominantProfile = true;

  // Forms
  editForm: FormGroup;
  expertiseForm: FormGroup;

  // Options
  genderOptions: GenderOption[] = [
    { value: GenderType.MALE, label: 'Male' },
    { value: GenderType.FEMALE, label: 'Female' },
    { value: GenderType.OTHER, label: 'Other' },
  ];

  specializationOptions: SpecializationOption[] = [
    { value: CoachSpecialization.CAREER_COUNSELING, label: 'Career Counseling' },
    { value: CoachSpecialization.ACADEMIC_GUIDANCE, label: 'Academic Guidance' },
    { value: CoachSpecialization.STUDY_ABROAD, label: 'Study Abroad Guidance' },
    { value: CoachSpecialization.SCHOLARSHIP_PREPARATION, label: 'Scholarship & Grants Preparation' },
    { value: CoachSpecialization.EXAM_PREPARATION, label: 'Exam & Test Preparation' },
    { value: CoachSpecialization.INTERNSHIP_PREPARATION, label: 'Internship Preparation' },
    { value: CoachSpecialization.JOB_SEARCH, label: 'Job Search & Applications' },
    { value: CoachSpecialization.INTERVIEW_PREPARATION, label: 'Interview Preparation' },
    { value: CoachSpecialization.CV_RESUME_BUILDING, label: 'CV & Resume Building' },
    { value: CoachSpecialization.PERSONAL_DEVELOPMENT, label: 'Personal Development' },
    { value: CoachSpecialization.GOAL_SETTING, label: 'Goal Setting & Planning' },
    { value: CoachSpecialization.SKILL_DEVELOPMENT, label: 'Skill Development' },
    { value: CoachSpecialization.ENTREPRENEURSHIP, label: 'Entrepreneurship & Startup Guidance' },
    { value: CoachSpecialization.LIFE_COACHING, label: 'Life Coaching' }
  ];

  // Category to display name mapping
  private categoryDisplayMap = {
    [Category.REALISTIC]: { name: 'Realistic', description: 'Practical and action-oriented' },
    [Category.INVESTIGATIVE]: { name: 'Investigative', description: 'Analytical and research-oriented' },
    [Category.ARTISTIC]: { name: 'Artistic', description: 'Creative and expressive' },
    [Category.SOCIAL]: { name: 'Social', description: 'People-oriented and helpful' },
    [Category.ENTERPRISING]: { name: 'Enterprising', description: 'Leadership and business-oriented' },
    [Category.CONVENTIONAL]: { name: 'Conventional', description: 'Detail-oriented and organized' }
  };

  // Stats
  stats: StatCard[] = [];

  // Profile photos
  profilePicture: string | null = null;
  profileLoaded = false;
  coverPhoto: string | null = null;

  constructor(
    private coachService: CoachService,
    private coachStudentConnectionService: CoachStudentConnectionService,
    private mediaService: MediaService,
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder,
    private notificationService: NotificationService
  ) {
    this.editForm = this.createEditForm();
    this.expertiseForm = this.createExpertiseForm();
  }

  ngOnInit(): void {
    const profileIdParam = this.route.snapshot.paramMap.get('id');
    const profileId = profileIdParam ? parseInt(profileIdParam) : this.currentUserId;      
    this.isOwner = profileId === this.currentUserId;
    this.loadCoachProfile();
  }

  private createEditForm(): FormGroup {
    return this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      age: ['', [Validators.required, Validators.min(18), Validators.max(100)]],
      gender: ['', [Validators.required]],
      phoneNumber: [''],
      specialization: [''],
      // Location fields
      city: [''],
      region: [''],
      country: [''],
      address: ['']
    });
  }

  private createExpertiseForm(): FormGroup {
    return this.fb.group({
      specialization: [''],
      expertise: [''],
      services: [''],
      availability: ['']
    });
  }

  private loadCoachProfile(): void {
    this.isLoading = true;
    this.error = null;

    // Get coach ID from route params
    const coachIdParam = this.route.snapshot.paramMap.get('id');
    const coachId = coachIdParam ? +coachIdParam : this.currentUserId;
    
    if (!coachId) {
      this.error = 'Coach ID not found';
      this.isLoading = false;
      return;
    }

    this.coachService.getCoachById(+coachId).subscribe({
      next: (response) => {
        if (response.data) {
          this.coachProfile = {
            ...response.data,
            specialization: CoachSpecialization[response.data.specialization as keyof typeof CoachSpecialization]
          };
          this.isOwner = this.coachProfile.id === this.currentUserId;
          this.setupProfileData();
          this.loadProfileMedia();
          this.loadDynamicStats();
          this.loadDominantProfile();
          this.coachRating = response.data.rate || 0; // Uncomment when rating service is available
        } else {
          this.error = 'Coach profile not found';
        }
        this.isLoading = false;
      },
      error: (err) => {
        this.error = err.error?.message || 'Failed to load coach profile';
        this.isLoading = false;
      }
    });
  }

  private loadDynamicStats(): void {
    if (!this.coachProfile) return;

    this.isLoadingStats = true;
    
    // Load students coached count and coach rating in parallel
    forkJoin({
      studentsCount: this.coachStudentConnectionService.countByCoachIdAndStatusAndRequestedBy(
        this.coachProfile.id, 'ACCEPTED'
      ),
      // Add rating service call here when available
      // rating: this.ratingService.getCoachRating(this.coachProfile.id)
    }).subscribe({
      next: (results) => {
        this.studentsCoached = results.studentsCount.data || 0;
        // this.coachRating = results.rating.data || 0; // Uncomment when rating service is available
        // this.coachRating = 0; // Keep as fallback until rating service is available
        this.setupStats();
        this.isLoadingStats = false;
      },
      error: (err) => {
        console.error('Error loading coach stats:', err);
        // Set default values on error
        this.studentsCoached = 0;
        //this.coachRating = 0;
        this.setupStats();
        this.isLoadingStats = false;
      }
    });
  }

  private loadDominantProfile(): void {
    if (!this.coachProfile) return;

    this.isLoadingDominantProfile = true;
    
    this.coachStudentConnectionService.getCoacheesDominantProfile(
      this.coachProfile.id, 'ACCEPTED'
    ).pipe(finalize(() => this.isLoadingDominantProfile = false)) // <-- ensures cleanup
    .subscribe({
      next: (response) => {
        const categoryInfo = this.categoryDisplayMap[response.data.dominantProfile];
        if (categoryInfo) {
          this.dominantProfile = {
            profileType: categoryInfo.name,
            description: categoryInfo.description,
            percentage: Math.round(response.data.percentage * 10) / 10
          };
        }
      },
      error: (err) => {
        console.error('Error loading dominant profile:', err);
        this.dominantProfile = null;
        this.isLoadingDominantProfile = false;
      }
    });
  }

  private setupProfileData(): void {
    if (!this.coachProfile) return;

    // Set up form data
    this.editForm.patchValue({
      firstName: this.coachProfile.firstName,
      lastName: this.coachProfile.lastName,
      age: this.coachProfile.age,
      gender: this.coachProfile.gender,
      phoneNumber: this.coachProfile.phoneNumber || '',
      specialization: this.coachProfile.specialization || '',
      city: this.coachProfile.location?.city || '',
      region: this.coachProfile.location?.region || '',
      country: this.coachProfile.location?.country || '',
      address: this.coachProfile.location?.address || ''
    });

    this.expertiseForm.patchValue({
      specialization: this.coachProfile.specialization || '',
      expertise: this.coachProfile.expertise || '',
      services: this.coachProfile.services || '',
      availability: this.coachProfile.availability || ''
    });

    // Determine if user is owner (you'll need to implement this logic)
    this.isOwner = this.checkIfOwner();
  }

  private setupStats(): void {
    if (!this.coachProfile) return;

    // Calculate member since
    const memberSinceDate = this.coachProfile.createdAt 
      ? new Date(this.coachProfile.createdAt)
      : new Date();

    const memberSinceMonth = memberSinceDate.toLocaleDateString('en-US', { month: 'short' });
    const memberSinceYear = memberSinceDate.getFullYear();

    // Calculate last activity
    const lastSeenDate = this.coachProfile.lastSeen ? new Date(this.coachProfile.lastSeen) : null;
    const lastActivityDays = lastSeenDate 
      ? Math.floor((Date.now() - lastSeenDate.getTime()) / (1000 * 60 * 60 * 24)) 
      : 0;

    this.stats = [
      {
        value: this.coachRating.toFixed(1),
        label: 'Rating',
        iconPath: 'M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z',
        bgColor: 'bg-yellow-100',
        iconColor: 'text-yellow-500'
      },
      {
        value: this.studentsCoached > 0 ? `${this.studentsCoached}${this.studentsCoached >= 100 ? '+' : ''}` : '0',
        label: 'Students Coached',
        iconPath: 'M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z',
        bgColor: 'bg-blue-100',
        iconColor: 'text-blue-500'
      },
      {
        value: `${memberSinceMonth} ${memberSinceYear}`,
        label: 'Member Since',
        iconPath: 'M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z',
        bgColor: 'bg-green-100',
        iconColor: 'text-green-500'
      },
      {
        value: lastActivityDays === 0 ? 'Today' : `${lastActivityDays} day${lastActivityDays === 1 ? '' : 's'}`,
        label: 'Last Activity',
        iconPath: 'M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z',
        bgColor: 'bg-purple-100',
        iconColor: 'text-purple-500'
      }
    ];
  }

  private async loadProfileMedia(): Promise<void> {
    if (!this.coachProfile) return;

    try {
      // 1. Get all media metadata for the user
      const allMedia: any = await this.mediaService.getLatestMediaByUserId(this.coachProfile.id).toPromise();
      
      if (!allMedia?.data?.length) return;

      // 2. Filter latest media by type and sort by creation date
      const profilePhotoMedia = allMedia.data
        .filter((m: any) => m.type === MediaType.PROFILE_PHOTO)
        .sort((a: any, b: any) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())[0];


      const coverPhotoMedia = allMedia.data
        .filter((m: any) => m.type === MediaType.COVER_PHOTO)
        .sort((a: any, b: any) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())[0];

      // 3. Fetch the actual media files if they exist
      if (profilePhotoMedia) {
        const blob = await this.mediaService.getMediaFileById(profilePhotoMedia.id).toPromise();
        if (blob) {
          // Clean up any existing object URL to prevent memory leaks
          if (this.profilePicture && this.profilePicture.startsWith('blob:')) {
            URL.revokeObjectURL(this.profilePicture);
          }
          this.profilePicture = URL.createObjectURL(blob);
        } else {
          this.profilePicture = null; // will show default later
        }
      } else {
        this.profilePicture = null; // no media
      }

      if (coverPhotoMedia) {
        const blob = await this.mediaService.getMediaFileById(coverPhotoMedia.id).toPromise();
        if (blob) {
          // Clean up any existing object URL to prevent memory leaks
          if (this.coverPhoto && this.coverPhoto.startsWith('blob:')) {
            URL.revokeObjectURL(this.coverPhoto);
          }
          this.coverPhoto = URL.createObjectURL(blob);
        }
      }

    } catch (err) {
      this.notificationService.showError('Error loading profile media. Please try again.');
      this.profilePicture = null;
      this.notificationService.showError('Failed to load media files.');
      // Fallback defaults on error
      if (!this.profilePicture) {
        this.profilePicture = 'https://www.shutterstock.com/image-vector/blank-avatar-photo-place-holder-600nw-1095249842.jpg';
      }
    } finally {
      this.profileLoaded = true; // mark that loading finished
    }
  }

  private checkIfOwner(): boolean {
    // Implement your logic to check if the current user is the owner
    // This might involve checking the current user's ID against the coach's ID
    // For now, returning true as placeholder
    return this.isOwner;
  }

  // Profile field helpers
  get displayablePersonalFields() {
    if (!this.coachProfile) return [];
    
    const fields = [];
    
    // Always show these required fields
    fields.push(
      { key: 'name', label: 'Full Name', value: this.getFullName(), icon: 'user' },
      { key: 'age', label: 'Age', value: `${this.coachProfile.age} years old`, icon: 'calendar' },
      { key: 'gender', label: 'Gender', value: this.getGenderDisplay(this.coachProfile.gender), icon: 'user' },
      { key: 'email', label: 'Email', value: this.coachProfile.email, icon: 'email' }
    );

    // Conditionally show phone if it exists
    if (this.coachProfile.phoneNumber) {
      fields.push({ key: 'phone', label: 'Phone', value: this.coachProfile.phoneNumber, icon: 'phone' });
    }

    // Conditionally show location if it exists
    if (this.coachProfile.location?.city && this.coachProfile.location?.country) {
      fields.push({ 
        key: 'location', 
        label: 'Location', 
        value: `${this.coachProfile.location.city}, ${this.coachProfile.location.country}`, 
        icon: 'location' 
      });
    }

    // Conditionally show specialization if it exists
    if (this.coachProfile.specialization) {
      fields.push({ 
        key: 'specialization', 
        label: 'Specialization', 
        value: this.getSpecializationLabel(this.coachProfile.specialization), 
        icon: 'specialization' 
      });
    }

    return fields;
  }

  getSpecializationLabel(value: string): string {
    const option = this.specializationOptions.find(spec => spec.value === value);
    return option ? option.label : value;
  }
  
  get hasExpertiseData(): boolean {
    return !!(this.coachProfile?.expertise || this.coachProfile?.services || this.coachProfile?.availability);
  }

  get expertiseList(): string[] {
    if (!this.coachProfile?.expertise) return [];
    return this.coachProfile.expertise.split(',').map(item => item.trim()).filter(item => item);
  }

  getFullName(): string {
    if (!this.coachProfile) return '';
    return `${this.coachProfile.firstName} ${this.coachProfile.lastName}`;
  }

  getLocationDisplay(): string {
    if (!this.coachProfile?.location) return '';
    const { city, country } = this.coachProfile.location;
    if (city && country) {
      return `${city}, ${country}`;
    }
    return city || country || '';
  }

  getGenderDisplay(gender: GenderType): string {
    const option = this.genderOptions.find(g => g.value === gender);
    return option ? option.label : gender;
  }

  canSendMessage(): boolean {
    return this.coachProfile?.messagePermission === MessagePermission.ALL ||
           this.coachProfile?.messagePermission === MessagePermission.NETWORK;
  }

  // Modal handlers
  onModifyProfile(): void {
    this.showEditModal = true;
  }

  closeEditModal(): void {
    this.showEditModal = false;
    this.editForm.patchValue({
      firstName: this.coachProfile?.firstName,
      lastName: this.coachProfile?.lastName,
      age: this.coachProfile?.age,
      gender: this.coachProfile?.gender,
      phoneNumber: this.coachProfile?.phoneNumber || '',
      specialization: this.coachProfile?.specialization || '',
      city: this.coachProfile?.location?.city || '',
      region: this.coachProfile?.location?.region || '',
      country: this.coachProfile?.location?.country || '',
      address: this.coachProfile?.location?.address || ''
    });
  }

  onManageExpertise(): void {
    this.showExpertiseModal = true;
  }

  closeExpertiseModal(): void {
    this.showExpertiseModal = false;
    this.expertiseForm.patchValue({
      specialization: this.coachProfile?.specialization || '',
      expertise: this.coachProfile?.expertise || '',
      services: this.coachProfile?.services || '',
      availability: this.coachProfile?.availability || ''
    });
  }

  // Form submission handlers
  onSaveProfile(): void {
    if (this.editForm.invalid || !this.coachProfile) return;

    this.isUpdating = true;
    const formValue = this.editForm.value;

    const updateData: CoachUpdateProfileDTO = {
      firstName: formValue.firstName,
      lastName: formValue.lastName,
      age: formValue.age,
      gender: formValue.gender,
      phoneNumber: formValue.phoneNumber || undefined,
      specialization: formValue.specialization || undefined,
      location: {
        city: formValue.city || undefined,
        region: formValue.region || undefined,
        country: formValue.country || undefined,
        address: formValue.address || undefined
      }
    };

    this.coachService.updateProfileCoach(this.coachProfile.id, updateData).subscribe({
      next: (response) => {
        if (response.data) {
          this.coachProfile = response.data;
          this.setupProfileData();
          this.closeEditModal();
        }
        this.isUpdating = false;
      },
      error: (err) => {
        this.notificationService.showError('Error updating profile. Please try again.');
        this.isUpdating = false;
      }
    });
  }

  onSaveExpertise(): void {
    if (!this.coachProfile) return;

    this.isUpdating = true;
    const formValue = this.expertiseForm.value;

    const updateData: CoachUpdateProfileDTO = {
      firstName: this.coachProfile.firstName,
      lastName: this.coachProfile.lastName,
      age: this.coachProfile.age,
      gender: this.coachProfile.gender,
      phoneNumber: this.coachProfile.phoneNumber,
      specialization: formValue.specialization || undefined,
      expertise: formValue.expertise || undefined,
      services: formValue.services || undefined,
      availability: formValue.availability || undefined
    };

    this.coachService.updateProfileCoach(this.coachProfile.id, updateData).subscribe({
      next: (response) => {
        if (response.data) {
          this.coachProfile = response.data;
          this.setupProfileData();
          this.closeExpertiseModal();
        }
        this.isUpdating = false;
      },
      error: (err) => {
        this.notificationService.showError('Error updating expertise. Please try again.');
        this.isUpdating = false;
      }
    });
  }

  // Photo upload handlers
  onProfilePictureChange(event: any): void {
    const file = event.target.files[0];
    if (!file || !this.coachProfile) return;

    // Check file size (max 10 MB)
    const maxSizeMB = 10;
    if (file.size > maxSizeMB * 1024 * 1024) {
      this.notificationService.showError(`File is too large. Maximum size is ${maxSizeMB} MB.`);
      return;
    }

    // Ensure file type is allowed (JPEG, JPG, PNG)
    const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png'];
    if (!allowedTypes.includes(file.type)) {
      this.notificationService.showError('Invalid file type. Allowed types: JPEG, JPG, PNG.');
      return;
    }

    // Show preview immediately
    const reader = new FileReader();
    reader.onload = (e: any) => {
      // Clean up any existing object URL
      if (this.profilePicture && this.profilePicture.startsWith('blob:')) {
        URL.revokeObjectURL(this.profilePicture);
      }
      this.profilePicture = e.target.result;
    };
    reader.readAsDataURL(file);

    // Upload to server
    this.isUploadingProfilePicture = true;
    
    const extension = file.name.split('.').pop(); // preserve original extension
    const fileName = `Profile_Photo_${this.coachProfile.id}_${new Date().toISOString().split('T')[0]}.${extension}`;
    
    const formData = new FormData();
    formData.append('media', new File([file], fileName, { type: file.type }));
    formData.append('userId', this.coachProfile.id.toString());
    formData.append('type', MediaType.PROFILE_PHOTO);

    this.mediaService.createMedia(formData).subscribe({
      next: (response) => {
        if (response.data) {
          // Reload profile media to get the new image
          this.loadProfileMedia();
        }
        this.isUploadingProfilePicture = false;
      },
      error: (err) => {
        this.notificationService.showError('Error uploading profile picture. Please try again.');
        this.isUploadingProfilePicture = false;
      }
    });
  }

  onCoverPhotoChange(event: any): void {
    const file = event.target.files[0];
    if (!file || !this.coachProfile) return;

    // Check file size (max 10 MB)
    const maxSizeMB = 10;
    if (file.size > maxSizeMB * 1024 * 1024) {
      this.notificationService.showError(`File is too large. Maximum size is ${maxSizeMB} MB.`);
      return;
    }

    // Ensure file type is allowed (JPEG, JPG, PNG)
    const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png'];
    if (!allowedTypes.includes(file.type)) {
      this.notificationService.showError('Invalid file type. Allowed types: JPEG, JPG, PNG.');
      return;
    }

    // Show preview immediately
    const reader = new FileReader();
    reader.onload = (e: any) => {
      // Clean up any existing object URL
      if (this.coverPhoto && this.coverPhoto.startsWith('blob:')) {
        URL.revokeObjectURL(this.coverPhoto);
      }
      this.coverPhoto = e.target.result;
    };
    reader.readAsDataURL(file);

    // Upload to server
    this.isUploadingCoverPhoto = true;
    
    const extension = file.name.split('.').pop(); // preserve original extension
    const fileName = `Cover_Photo_${this.coachProfile.id}_${new Date().toISOString().split('T')[0]}.${extension}`;
    
    const formData = new FormData();
    formData.append('media', new File([file], fileName, { type: file.type }));
    formData.append('userId', this.coachProfile.id.toString());
    formData.append('type', MediaType.COVER_PHOTO);

    this.mediaService.createMedia(formData).subscribe({
      next: (response) => {
        if (response.data) {
          // Reload profile media to get the new image
          this.loadProfileMedia();
        }
        this.isUploadingCoverPhoto = false;
      },
      error: (err) => {
        this.notificationService.showError('Error uploading cover photo. Please try again.');
        this.isUploadingCoverPhoto = false;
      }
    });
  }

  // Utility methods
  async onShareProfile() {
    if (!this.coachProfile) return;

    const currentUrl = window.location.href;
    const profileId = this.coachProfile.id.toString();

    // Ensure the URL ends with the profile owner's id
    const profileUrl = currentUrl.endsWith(`/${profileId}`)
      ? currentUrl
      : `${currentUrl}/${profileId}`;
    
    if (navigator.share && navigator.canShare()) {
      try {
        await navigator.share({
          title: `${this.coachProfile.firstName} ${this.coachProfile.lastName}'s Profile`,
          text: `Check out ${this.coachProfile.firstName} ${this.coachProfile.lastName}'s profile on our platform!`,
          url: profileUrl
        });
        this.notificationService.showSuccess('Profile shared successfully!');
      } catch (err) {
        if ((err as any).name !== 'AbortError') {
          this.copyToClipboard(profileUrl);
        }
      }
    } else {
      this.copyToClipboard(profileUrl);
    }
  }

  private copyToClipboard(text: string) {
    if (navigator.clipboard && window.isSecureContext) {
      navigator.clipboard.writeText(text).then(() => {
        this.notificationService.showSuccess('Profile link copied to clipboard!');
      }).catch(() => {
        this.fallbackCopyToClipboard(text);
      });
    } else {
      this.fallbackCopyToClipboard(text);
    }
  }

  private fallbackCopyToClipboard(text: string) {
    const textArea = document.createElement('textarea');
    textArea.value = text;
    textArea.style.position = 'fixed';
    textArea.style.left = '-999999px';
    textArea.style.top = '-999999px';
    document.body.appendChild(textArea);
    textArea.focus();
    textArea.select();
    
    try {
      document.execCommand('copy');
      this.notificationService.showSuccess('Profile link copied to clipboard!');
    } catch (err) {
      this.notificationService.showError('Failed to copy link. Please copy manually: ' + text);
    } finally {
      document.body.removeChild(textArea);
    }
  }

  getFieldError(fieldName: string): string {
    const field = this.editForm.get(fieldName);
    if (field?.errors) {
      if (field.errors['required']) return `${fieldName} is required`;
      if (field.errors['minlength']) return `${fieldName} is too short`;
      if (field.errors['min']) return `${fieldName} must be at least ${field.errors['min'].min}`;
      if (field.errors['max']) return `${fieldName} must be at most ${field.errors['max'].max}`;
    }
    return '';
  }

  getIconSvg(iconType: string): string {
    const icons: { [key: string]: string } = {
      user: 'M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z',
      calendar: 'M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z',
      email: 'M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z',
      phone: 'M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z',
      location: 'M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z M15 11a3 3 0 11-6 0 3 3 0 016 0z',
      specialization: 'M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z'
    };
    return icons[iconType] || icons['user'];
  }

  // Clean up object URLs on component destruction to prevent memory leaks
  ngOnDestroy(): void {
    if (this.profilePicture && this.profilePicture.startsWith('blob:')) {
      URL.revokeObjectURL(this.profilePicture);
    }
    if (this.coverPhoto && this.coverPhoto.startsWith('blob:')) {
      URL.revokeObjectURL(this.coverPhoto);
    }
  }
}