import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { GenderType } from '../../../model/enum/GenderType.enum';
import { EducationLevel } from '../../../model/enum/EducationLevel.enum';
import { AccountPrivacy } from '../../../model/enum/AccountPrivacy.enum';
import { MessagePermission } from '../../../model/enum/MessagePermission.enum';
import { VisibilityStatus } from '../../../model/enum/VisibilityStatus.enum';
import { TestType } from '../../../model/enum/TestType.enum';
import { StudentService } from '../../../Service/student.service';
import { TestService } from '../../../Service/test.service';
import { StudentResponseDTO } from '../../../model/dto/StudentResponse.dto';
import { StudentCreateDTO } from '../../../model/dto/StudentCreate.dto';
import { TestResultAverageDTO } from '../../../model/dto/TestResultAverage.dto';
import { MediaService } from '../../../Service/media.service';
import { NotificationService } from '../../../Service/notification.service';
import { MediaType } from '../../../model/enum/MediaType.enum';
import { StudentUpdateDTO } from '../../../model/dto/StudentUpdate.dto';
import { Category } from '../../../model/enum/Category.enum';
import { TestCountDTO } from '../../../model/dto/TestCount.dto';
import { TestResultService } from '../../../Service/testResult.service';

// Interfaces
export interface UserProfile {
  id: string;
  name: string;
  firstName: string;
  lastName: string;
  age: number;
  gender: GenderType;
  school: string;
  academicYear: string;
  location: string;
  memberSince: string;
  relations: string;
  email: string;
  phone: string;
  class: string;
  profilePicture: string;
  coverPhoto?: string;
  fieldOfStudy: string;
  educationLevel: EducationLevel;
  accountPrivacy: AccountPrivacy;
  messagePermission: MessagePermission;
  profileVisibility: VisibilityStatus;
  createdAt: string;
  updatedAt: string;
}

export interface ProfileStat {
  value: string;
  label: string;
  bgColor: string;
  iconColor: string;
  iconPath: string;
}

export interface DominantProfile {
  type: string;
  description: string;
  matchPercentage: number;
}

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css',
})
export class ProfileComponent implements OnInit {
  userProfile!: UserProfile;
  isOwner: boolean = false;
  isLoading: boolean = true;
  error: string | null = null;
  currentUserId : number = 1;

  // Test counts
  testFullCount: number = 0;
  testExpressCount: number = 0;

  // Dominant profile data
  dominantProfileData: TestResultAverageDTO | null = null;

  // Modal states
  showEditModal: boolean = false;
  isUpdating: boolean = false;
  editForm!: FormGroup;

  // Gender options for dropdown
  genderOptions = [
    { value: GenderType.MALE, label: 'Male' },
    { value: GenderType.FEMALE, label: 'Female' },
    { value: GenderType.OTHER, label: 'Other' }
  ];

  // Education levels for dropdown
  educationLevels = [
    { value: EducationLevel.MIDDLE_SCHOOL, label: 'Middle School Student' },
    { value: EducationLevel.HIGH_SCHOOL, label: 'High School Student' },
    { value: EducationLevel.POST_SECONDARY, label: 'Post-Secondary Student (e.g., BTS/DUT/Preparatory)' },
    { value: EducationLevel.UNIVERSITY, label: 'University Student' },
    { value: EducationLevel.GRADUATE, label: 'Graduate Student (Master\'s, Doctorate)' },
    { value: EducationLevel.OTHER, label: 'Other Student' }
  ];

  dominantProfile: DominantProfile = {
    type: 'Loading...',
    description: 'Fetching your profile data...',
    matchPercentage: 0
  };

  stats: ProfileStat[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private studentService: StudentService,
    private testService: TestService,
    private mediaService: MediaService,
    private notificationService: NotificationService,
    private formBuilder: FormBuilder,
    private cdRef: ChangeDetectorRef,
    private testResultService: TestResultService
  ) {
    this.initializeEditForm();
  }

  private initializeEditForm() {
    this.editForm = this.formBuilder.group({
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      age: ['', [Validators.required, Validators.min(13), Validators.max(100)]],
      gender: [GenderType.MALE, Validators.required],
      phoneNumber: ['', [Validators.required]],
      school: [''],
      fieldOfStudy: [''],
      educationLevel: [EducationLevel.HIGH_SCHOOL, Validators.required],
      city: [''],
      country: ['']
    });
  }

  async ngOnInit() {
    try {
      this.isLoading = true;
      this.error = null;

      const profileIdParam = this.route.snapshot.paramMap.get('id');
      const profileId = profileIdParam ? parseInt(profileIdParam) : this.studentService.getCurrentUserId();
      this.currentUserId = this.studentService.getCurrentUserId();
      
      this.isOwner = profileId === this.currentUserId;

      // Load student profile from API
      this.studentService.getStudentById(profileId).subscribe({
        next: async (response) => {
          if (response.status && response.data) {
            this.userProfile = this.mapStudentToUserProfile(response.data);
            // Load media files after profile
            await this.loadUserMedia(profileId);
            // Load test counts
            await this.loadTestCounts(profileId);
            // Load dominant profile
            await this.loadDominantProfile(profileId);
            this.updateStatsBasedOnOwnership();
            this.populateEditForm(response.data);
            this.isLoading = false;
          } else {
            this.error = response.message || 'Failed to load profile';
            this.isLoading = false;
          }
        },
        error: (err) => {
          console.error('Error loading student profile:', err);
          this.error = 'Failed to load profile. Please try again.';
          this.isLoading = false;
        }
      });

    } catch (error) {
      console.error('Error in ngOnInit:', error);
      this.error = 'An unexpected error occurred';
      this.isLoading = false;
    }
  }

  private async loadTestCounts(userId: number) {
    try {
      const response = await this.testService.getTestCountByStudentId(userId).toPromise();
      if (response?.status && response.data) {
        // Process the test counts
        response.data.forEach((testCount: TestCountDTO) => {
          switch (testCount.testType) {
            case TestType.COMPLETE:
              this.testFullCount = testCount.count;
              break;
            case TestType.FAST:
              this.testExpressCount = testCount.count;
              break;
          }
        });
      }
    } catch (err) {
      console.error('Error loading test counts:', err);
      // Set default values if API call fails
      this.testFullCount = 0;
      this.testExpressCount = 0;
    }
  }

  private async loadDominantProfile(userId: number) {
    try {
      const response = await this.testResultService.getDominantProfileByStudentId(userId).toPromise();
      if (response?.status && response.data) {
        this.dominantProfileData = response.data;
        this.updateDominantProfileFromAPI();
      } else {
        // Set default values if no data available
        this.dominantProfile = {
          type: 'No Data',
          description: 'Complete a test to see your dominant profile',
          matchPercentage: 0
        };
      }
    } catch (err) {
      console.error('Error loading dominant profile:', err);
      // Set default values if API call fails
      
    }
    // Get the category description based on the dominant profile
    const categoryDescriptions = {
      [Category.REALISTIC]: 'Practical and action-oriented',
      [Category.INVESTIGATIVE]: 'Analytical and scientific',
      [Category.ARTISTIC]: 'Creative and expressive',
      [Category.SOCIAL]: 'People-oriented and helpful',
      [Category.ENTERPRISING]: 'Leadership and business-minded',
      [Category.CONVENTIONAL]: 'Organized and detail-oriented'
    };

    // Get the display name for the category
    const categoryDisplayNames = {
      [Category.REALISTIC]: 'Realistic',
      [Category.INVESTIGATIVE]: 'Investigative',
      [Category.ARTISTIC]: 'Artistic',
      [Category.SOCIAL]: 'Social',
      [Category.ENTERPRISING]: 'Enterprising',
      [Category.CONVENTIONAL]: 'Conventional'
    };

    const dominantCategory = this.dominantProfileData!!.dominantProfile;
    const description = categoryDescriptions[dominantCategory] || 'Profile type';
    const displayName = categoryDisplayNames[dominantCategory] || dominantCategory;

    this.dominantProfile = {
      type: displayName,
      description: description,
      matchPercentage: Math.round(this.dominantProfileData!!.percentage * 10) / 10
    };
  }
  updateDominantProfileFromAPI() {
    throw new Error('Method not implemented.');
  }

  private populateEditForm(student: StudentResponseDTO) {
    this.editForm.patchValue({
      firstName: student.firstName,
      lastName: student.lastName,
      age: student.age,
      gender: student.gender,
      phoneNumber: student.phoneNumber,
      school: student.school || '',
      fieldOfStudy: student.fieldOfStudy || '',
      educationLevel: student.educationLevel,
      city: student.location?.city || '',
      country: student.location?.country || ''
    });
  }
  
  private async loadUserMedia(userId: number) {
    try {
      // 1. Get all media metadata for the user
      const allMedia: any = await this.mediaService.getLatestMediaByUserId(userId).toPromise(); // Returns list of MediaResponseDTO
      
      if (!allMedia?.data?.length) return;

      // 2. Filter latest media by type
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
          this.userProfile.profilePicture = URL.createObjectURL(blob);
        }
      }

      if (coverPhotoMedia) {
        const blob = await this.mediaService.getMediaFileById(coverPhotoMedia.id).toPromise();
        if (blob) {
          this.userProfile.coverPhoto = URL.createObjectURL(blob);
        }
      }

    } catch (err) {
      console.error('Error loading user media:', err);
      this.notificationService.showError('Failed to load media files.');
    }
  }

  private mapStudentToUserProfile(student: StudentResponseDTO): UserProfile {
    const location = student.location 
      ? `${student.location.city}, ${student.location.country}`
      : 'Location not specified';

    const memberSince = new Date(student.createdAt).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long'
    });

    const academicYear = this.getAcademicYearDisplay(student.educationLevel);
    const genderDisplay = this.getGenderDisplay(student.gender);
    
    return {
      id: student.id.toString(),
      name: `${student.firstName} ${student.lastName}`,
      firstName: student.firstName,
      lastName: student.lastName,
      age: student.age,
      gender: student.gender,
      school: student.school || 'School not specified',
      academicYear: academicYear,
      location: location,
      memberSince: `Member since ${memberSince}`,
      relations: '500', // This would come from a separate API call for connections
      email: student.email,
      phone: student.phoneNumber,
      class: student.fieldOfStudy || 'Field not specified',
      profilePicture: this.userProfile?.profilePicture || 'https://www.shutterstock.com/image-vector/blank-avatar-photo-place-holder-600nw-1095249842.jpg',//https://images.pexels.com/photos/91227/pexels-photo-91227.jpeg?auto=compress&cs=tinysrgb&w=150', // Default image
      coverPhoto: this.userProfile?.coverPhoto, // Would need to be added to DTO if needed
      fieldOfStudy: student.fieldOfStudy || 'Field not specified',
      educationLevel: student.educationLevel,
      accountPrivacy: student.accountPrivacy,
      messagePermission: student.messagePermission,
      profileVisibility: student.profileVisibility,
      createdAt: student.createdAt,
      updatedAt: student.updatedAt
    };
  }

  private getAcademicYearDisplay(educationLevel: EducationLevel): string {
    switch (educationLevel) {
      case EducationLevel.MIDDLE_SCHOOL:
        return 'Middle School Student';
      case EducationLevel.HIGH_SCHOOL:
        return 'High School Student';
      case EducationLevel.POST_SECONDARY:
        return 'Post-Secondary Student (e.g., BTS/DUT/Preparatory)';
      case EducationLevel.UNIVERSITY:
        return 'University Student';
      case EducationLevel.GRADUATE:
        return 'Graduate Student (Master\'s, Doctorate)';
      case EducationLevel.OTHER:
        return 'Other Student';
      default:
        return 'Student';
    }
  }

  getGenderDisplay(gender: GenderType): string {
    switch (gender) {
      case GenderType.MALE:
        return 'Male';
      case GenderType.FEMALE:
        return 'Female';
      case GenderType.OTHER:
        return 'Other';
      default:
        return 'Not specified';
    }
  }

  private getAccountPrivacyDisplay(privacy: AccountPrivacy): string {
    switch (privacy) {
      case AccountPrivacy.PUBLIC:
        return 'Public';
      case AccountPrivacy.PRIVATE:
        return 'Private';
      default:
        return 'Not specified';
    }
  }

  private getMessagePermissionDisplay(permission: MessagePermission): string {
    switch (permission) {
      case MessagePermission.ALL:
        return 'Everyone can message';
      case MessagePermission.NETWORK:
        return 'Friends only';
      case MessagePermission.NO_ONE:
        return 'No messages';
      default:
        return 'Not specified';
    }
  }

  private updateStatsBasedOnOwnership() {
    const joinDate = new Date(this.userProfile.createdAt);
    const memberSinceShort = joinDate.toLocaleDateString('en-US', { 
      month: 'short', 
      year: 'numeric' 
    });

    if (this.isOwner) {
      this.stats = [
        {
          value: this.testFullCount.toString(),
          label: 'Tests full',
          bgColor: 'bg-blue-100',
          iconColor: 'text-blue-600',
          iconPath: 'M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z'
        },
        {
          value: this.testExpressCount.toString(),
          label: 'Tests Express',
          bgColor: 'bg-pink-100',
          iconColor: 'text-pink-600',
          iconPath: 'M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z'
        },
        {
          value: '2',
          label: 'Favorite Jobs',
          bgColor: 'bg-green-100',
          iconColor: 'text-green-600',
          iconPath: 'M16 7V6.2C16 5.0799 16 4.51984 15.782 4.09202C15.5903 3.71569 15.2843 3.40973 14.908 3.21799C14.4802 3 13.9201 3 12.8 3H11.2C10.0799 3 9.51984 3 9.09202 3.21799C8.71569 3.40973 8.40973 3.71569 8.21799 4.09202C8 4.51984 8 5.0799 8 6.2V7M3.02721 10.0263C3.38776 10.3719 7.28572 14 12 14C16.7143 14 20.6122 10.3719 20.9728 10.0263M3.02721 10.0263C3 10.493 3 11.0665 3 11.8V16.2C3 17.8802 3 18.7202 3.32698 19.362C3.6146 19.9265 4.07354 20.3854 4.63803 20.673C5.27976 21 6.11984 21 7.8 21H16.2C17.8802 21 18.7202 21 19.362 20.673C19.9265 20.3854 20.3854 19.9265 20.673 19.362C21 18.7202 21 17.8802 21 16.2V11.8C21 11.0665 21 10.493 20.9728 10.0263M3.02721 10.0263C3.06233 9.4241 3.14276 8.99959 3.32698 8.63803C3.6146 8.07354 4.07354 7.6146 4.63803 7.32698C5.27976 7 6.11984 7 7.8 7H16.2C17.8802 7 18.7202 7 19.362 7.32698C19.9265 7.6146 20.3854 8.07354 20.673 8.63803C20.8572 8.99959 20.9377 9.4241 20.9728 10.0263'
        },
        {
          value: '2',
          label: 'Training Courses Taken',
          bgColor: 'bg-purple-100',
          iconColor: 'text-purple-600',
          iconPath: 'M12 10.4V20M12 10.4C12 8.15979 12 7.03969 11.564 6.18404C11.1805 5.43139 10.5686 4.81947 9.81596 4.43597C8.96031 4 7.84021 4 5.6 4H4.6C4.03995 4 3.75992 4 3.54601 4.10899C3.35785 4.20487 3.20487 4.35785 3.10899 4.54601C3 4.75992 3 5.03995 3 5.6V16.4C3 16.9601 3 17.2401 3.10899 17.454C3.20487 17.6422 3.35785 17.7951 3.54601 17.891C3.75992 18 4.03995 18 4.6 18H7.54668C8.08687 18 8.35696 18 8.61814 18.0466C8.84995 18.0879 9.0761 18.1563 9.29191 18.2506C9.53504 18.3567 9.75977 18.5065 10.2092 18.8062L12 20M12 10.4C12 8.15979 12 7.03969 12.436 6.18404C12.8195 5.43139 13.4314 4.81947 14.184 4.43597C15.0397 4 16.1598 4 18.4 4H19.4C19.9601 4 20.2401 4 20.454 4.10899C20.6422 4.20487 20.7951 4.35785 20.891 4.54601C21 4.75992 21 5.03995 21 5.6V16.4C21 16.9601 21 17.2401 20.891 17.454C20.7951 17.6422 20.6422 17.7951 20.454 17.891C20.2401 18 19.9601 18 19.4 18H16.4533C15.9131 18 15.643 18 15.3819 18.0466C15.15 18.0879 14.9239 18.1563 14.7081 18.2506C14.465 18.3567 14.2402 18.5065 13.7908 18.8062L12 20'
        }
      ];
    } else {
      this.stats = [
        {
          value: this.testFullCount.toString(),
          label: 'Tests full',
          bgColor: 'bg-blue-100',
          iconColor: 'text-blue-600',
          iconPath: 'M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z'
        },
        {
          value: this.testExpressCount.toString(),
          label: 'Tests Express',
          bgColor: 'bg-pink-100',
          iconColor: 'text-pink-600',
          iconPath: 'M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z'
        },
        {
          value: memberSinceShort,
          label: 'Member since',
          bgColor: 'bg-green-100',
          iconColor: 'text-green-600',
          iconPath: 'M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z'
        },
        {
          value: '2 days',
          label: 'Last Activity',
          bgColor: 'bg-purple-100',
          iconColor: 'text-purple-600',
          iconPath: 'M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z'
        }
      ];
    }
  }

  async onProfilePictureChange(event: any) {
    const file = event.target.files[0];
    if (!file) return;

    const maxSizeMB = 10;
    if (file.size > maxSizeMB * 1024 * 1024) {
      this.notificationService.showError(`File is too large. Maximum size is ${maxSizeMB} MB.`);
      return;
    }

    // --- Show preview immediately ---
    const reader = new FileReader();
    reader.onload = (e: any) => {
      this.userProfile.profilePicture = e.target.result;
    };
    reader.readAsDataURL(file);

    // --- Upload to server if owner ---
    if (!this.isOwner) return;

    try {
      // Ensure file type is allowed (JPEG, JPG, PNG)
      const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png'];
      if (!allowedTypes.includes(file.type)) {
        this.notificationService.showError('Invalid file type. Allowed types: JPEG, JPG, PNG.');
        return;
      }

      const extension = file.name.split('.').pop(); // preserve original extension
      const fileName = `Profile_Photo_${this.currentUserId}_${new Date().toISOString().split('T')[0]}.${extension}`;

      const formData = new FormData();
      formData.append('media', new File([file], fileName, { type: file.type }));
      formData.append('userId', this.currentUserId.toString());
      formData.append('type', MediaType.PROFILE_PHOTO);

      // Convert Observable to Promise and await it
      const res: any = await this.mediaService.createMedia(formData).toPromise();
      this.notificationService.showSuccess(res.message);

    } catch (err: any) {
      const serverMessage = err.error?.message || 'Unknown error occurred';
      this.notificationService.showError(serverMessage);
      console.error('Failed to upload profile photo:', err);
    }
  }

  async onCoverPhotoChange(event: any) {
    const file = event.target.files[0];
    if (!file) return;

    // --- Check file size (max 10 MB) ---
    const maxSizeMB = 10;
    if (file.size > maxSizeMB * 1024 * 1024) {
      this.notificationService.showError(`File is too large. Maximum size is ${maxSizeMB} MB.`);
      return;
    }

    // --- Show preview immediately ---
    const reader = new FileReader();
    reader.onload = (e: any) => {
      this.userProfile.coverPhoto = e.target.result;
    };
    reader.readAsDataURL(file);

    // --- Upload to server if owner ---
    if (!this.isOwner) return;

    try {
      // Ensure file type is allowed (JPEG, JPG, PNG)
      const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png'];
      if (!allowedTypes.includes(file.type)) {
        this.notificationService.showError('Invalid file type. Allowed types: JPEG, JPG, PNG.');
        return;
      }

      const extension = file.name.split('.').pop(); // preserve original extension
      const fileName = `Cover_Photo_${this.currentUserId}_${new Date().toISOString().split('T')[0]}.${extension}`;

      const formData = new FormData();
      formData.append('media', new File([file], fileName, { type: file.type }));
      formData.append('userId', this.currentUserId.toString());
      formData.append('type', MediaType.COVER_PHOTO);

      // Convert Observable to Promise and await it
      const res: any = await this.mediaService.createMedia(formData).toPromise();
      this.notificationService.showSuccess(res.message);

    } catch (err: any) {
      const serverMessage = err.error?.message || 'Unknown error occurred';
      this.notificationService.showError(serverMessage);
      console.error('Failed to upload cover photo:', err);
    }
  }

  onModifyProfile() {
    this.showEditModal = true;
  }

  closeEditModal() {
    this.showEditModal = false;
  }

  async onShareProfile() {
    if (!this.userProfile) return;

    const baseUrl = `${window.location.origin}/student/profile`;
    const profileId = this.userProfile.id.toString();

    let profileUrl: string;

    if (Number(this.userProfile.id) === this.currentUserId) {
      // ✅ Case 1 & 2: My own profile
      // Always enforce `/student/profile/{id}` (no duplicates)
      profileUrl = `${baseUrl}/${profileId}`;
    } else {
      // ✅ Case 3: Another user's profile
      profileUrl = `${baseUrl}/${profileId}`;
    }

    // Check if Web Share API is supported
    if (navigator.share && navigator.canShare()) {
      try {
        await navigator.share({
          title: `${this.userProfile.name}'s Profile`,
          text: `Check out ${this.userProfile.name}'s profile on our platform!`,
          url: profileUrl
        });
        this.notificationService.showSuccess('Profile shared successfully!');
      } catch (err) {
        // If sharing was cancelled or failed, fallback to clipboard
        if ((err as any).name !== 'AbortError') {
          this.copyToClipboard(profileUrl);
        }
      }
    } else {
      // Fallback to copying to clipboard
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

  async onSaveProfile() {
    if (this.editForm.invalid) {
      this.markFormGroupTouched();
      return;
    }

    this.isUpdating = true;

    try {
      const formValue = this.editForm.value;
      const updateData: StudentUpdateDTO = {
        firstName: formValue.firstName,
        lastName: formValue.lastName,
        age: formValue.age,
        gender: formValue.gender,
        phoneNumber: formValue.phoneNumber,
        school: formValue.school,
        fieldOfStudy: formValue.fieldOfStudy,
        educationLevel: formValue.educationLevel,
        location: {
          city: formValue.city,
          country: formValue.country
        }
      };

      const response = await this.studentService.updateProfile(this.currentUserId, updateData).toPromise();
      
      if (response?.status && response.data) {
        // Update local profile data
        this.userProfile = this.mapStudentToUserProfile(response.data);
        this.updateStatsBasedOnOwnership(); // <-- recalc after update
        this.notificationService.showSuccess('Profile updated successfully!');
        this.closeEditModal();
        this.cdRef.detectChanges(); // 👈 force UI refresh
      } else {
        this.notificationService.showError(response?.message || 'Failed to update profile');
      }
    } catch (err: any) {
      console.error('Error updating profile:', err);
      const errorMessage = err.error?.message || 'Failed to update profile. Please try again.';
      this.notificationService.showError(errorMessage);
    } finally {
      this.isUpdating = false;
    }
  }

  private markFormGroupTouched() {
    Object.keys(this.editForm.controls).forEach(key => {
      const control = this.editForm.get(key);
      control?.markAsTouched();
    });
  }

  getFieldError(fieldName: string): string {
    const control = this.editForm.get(fieldName);
    if (!control || !control.touched || !control.errors) return '';

    if (control.errors['required']) return `${fieldName} is required`;
    if (control.errors['minlength']) return `${fieldName} is too short`;
    if (control.errors['min']) return `${fieldName} must be at least ${control.errors['min'].min}`;
    if (control.errors['max']) return `${fieldName} must be at most ${control.errors['max'].max}`;
    
    return 'Invalid input';
  }

  // Utility methods to check permissions based on privacy settings
  canViewProfile(): boolean {
    if (this.isOwner) return true;
    
    switch (this.userProfile?.accountPrivacy) {
      case AccountPrivacy.PUBLIC:
        return true;
      case AccountPrivacy.PRIVATE:
        return false; // Would need friend relationship check
      default:
        return false;
    }
  }

  canSendMessage(): boolean {
    if (this.isOwner) return false;
    
    switch (this.userProfile?.messagePermission) {
      case MessagePermission.ALL:
        return true;
      case MessagePermission.NETWORK:
        return false; // Would need friend relationship check
      case MessagePermission.NO_ONE:
        return false;
      default:
        return false;
    }
  }
}