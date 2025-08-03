import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

// Interfaces
export interface UserProfile {
  id: string;
  name: string;
  location: string;
  memberSince: string;
  relations: string;
  email: string;
  phone: string;
  profilePicture: string;
  coverPhoto?: string;
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

// Profile Service
class ProfileService {
  getCurrentUserId(): string {
    return 'current-user-123';
  }

  async getProfile(id: string): Promise<UserProfile> {
    return {
      id: id,
      name: 'Alex Johnson',
      location: 'Paris, France',
      memberSince: 'Membre depuis janvier 2024',
      relations: '500',
      email: 'Student@example.com',
      phone: '+33 6 12 34 56 78',
      profilePicture: 'https://images.pexels.com/photos/91227/pexels-photo-91227.jpeg?auto=compress&cs=tinysrgb&w=150'
    };
  }

  async updateProfilePicture(userId: string, imageData: string): Promise<void> {
    console.log('Updating profile picture for user:', userId);
  }

  async updateCoverPhoto(userId: string, imageData: string): Promise<void> {
    console.log('Updating cover photo for user:', userId);
  }
}

@Component({
  selector: 'app-profile',
  templateUrl: './profile-coach.component.html',
  styleUrl: './profile-coach.component.css',
  providers: [ProfileService]
})
export class ProfileCoachComponent implements OnInit {
  userProfile!: UserProfile;
  isOwner: boolean = false;

  dominantProfile: DominantProfile = {
    type: 'Realistic',
    description: 'Practical and action-oriented',
    matchPercentage: 44
  };

  stats: ProfileStat[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private profileService: ProfileService
  ) {}

  async ngOnInit() {
    const profileId = this.route.snapshot.paramMap.get('id') || 'current-user-123';
    const currentUserId = this.profileService.getCurrentUserId();
    this.isOwner = profileId === currentUserId;

    this.userProfile = await this.profileService.getProfile(profileId);
    this.updateStatsBasedOnOwnership();
  }

  private updateStatsBasedOnOwnership() {
    if (this.isOwner) {
      this.stats = [
        {
          value: '3',
          label: 'Full tests access',
          bgColor: 'bg-blue-100',
          iconColor: 'text-blue-600',
          iconPath: 'M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z'
        },
        {
          value: '2',
          label: 'Fast tests access',
          bgColor: 'bg-pink-100',
          iconColor: 'text-pink-600',
          iconPath: 'M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z'
        },
        {
          value: 'Jun 2024',
          label: 'Member since',
          bgColor: 'bg-green-100',
          iconColor: 'text-green-600',
          iconPath: 'M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z'
        },
      ];
    } else {
      this.stats = [
        {
          value: '3',
          label: 'Tests full access',
          bgColor: 'bg-blue-100',
          iconColor: 'text-blue-600',
          iconPath: 'M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z'
        },
        {
          value: '2',
          label: 'Tests Express access',
          bgColor: 'bg-pink-100',
          iconColor: 'text-pink-600',
          iconPath: 'M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z'
        },
        {
          value: 'Jun 2024',
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
    if (file) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.userProfile.profilePicture = e.target.result;
      };
      reader.readAsDataURL(file);

      if (this.isOwner) {
        await this.profileService.updateProfilePicture(this.userProfile.id, file);
      }
    }
  }

  async onCoverPhotoChange(event: any) {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.userProfile.coverPhoto = e.target.result;
      };
      reader.readAsDataURL(file);

      if (this.isOwner) {
        await this.profileService.updateCoverPhoto(this.userProfile.id, file);
      }
    }
  }

  onModifyProfile() {
    console.log('Modify profile clicked');
  }
}
