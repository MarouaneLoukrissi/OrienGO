import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

// Interfaces
export interface UserProfile {
  id: string;
  name: string;
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
      school: 'Victor Hugo high school',
      academicYear: 'Final year of science',
      location: 'Paris, France',
      memberSince: 'Membre depuis janvier 2024',
      relations: '500',
      email: 'Student@example.com',
      phone: '+33 6 12 34 56 78',
      class: 'Terminale s',
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
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css',
  providers: [ProfileService]
})
export class ProfileComponent implements OnInit {
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
          label: 'Tests full',
          bgColor: 'bg-blue-100',
          iconColor: 'text-blue-600',
          iconPath: 'M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z'
        },
        {
          value: '2',
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
          value: '3',
          label: 'Tests full',
          bgColor: 'bg-blue-100',
          iconColor: 'text-blue-600',
          iconPath: 'M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z'
        },
        {
          value: '2',
          label: 'Tests Express',
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