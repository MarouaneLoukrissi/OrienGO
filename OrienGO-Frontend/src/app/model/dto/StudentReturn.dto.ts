import { AccountPrivacy } from '../enum/AccountPrivacy.enum';
import { EducationLevel } from '../enum/EducationLevel.enum';
import { GenderType } from '../enum/GenderType.enum';
import { MessagePermission } from '../enum/MessagePermission.enum';
import { VisibilityStatus } from '../enum/VisibilityStatus.enum';
import { LocationDTO } from './Location.dto';

export interface StudentReturnDTO {
  id: number;

  firstName: string;

  lastName: string;

  age: number;

  gender: GenderType;

  phoneNumber: string;

  email: string;

  school: string;

  fieldOfStudy: string;

  educationLevel: EducationLevel;

  location: LocationDTO;

  // Account status fields
  enabled: boolean;

  tokenExpired: boolean | null;

  lastSeen: string | null;       // ISO date string from backend
  lastLoginAt: string | null;    // ISO date string
  deletedAt: string | null;      // ISO date string

  isDeleted: boolean | null;

  suspended: boolean;

  suspensionReason: string | null;

  suspendedUntil: string | null; // ISO date string

  profileVisibility: VisibilityStatus;

  messagePermission: MessagePermission;

  accountPrivacy: AccountPrivacy;

  createdAt: string;   // ISO date string
  updatedAt: string;   // ISO date string
}
