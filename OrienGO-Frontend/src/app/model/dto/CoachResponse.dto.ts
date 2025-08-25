import { AccountPrivacy } from "../enum/AccountPrivacy.enum";
import { CoachSpecialization } from "../enum/CoachSpecialization.enum";
import { GenderType } from "../enum/GenderType.enum";
import { MessagePermission } from "../enum/MessagePermission.enum";
import { VisibilityStatus } from "../enum/VisibilityStatus.enum";
import { LocationDTO } from "./Location.dto";

export interface CoachResponseDTO {
  id: number;
  firstName: string;
  lastName: string;
  age: number;
  gender: GenderType;
  phoneNumber?: string;
  email: string;
  specialization?: CoachSpecialization

  profileVisibility: VisibilityStatus;
  messagePermission: MessagePermission;
  accountPrivacy: AccountPrivacy;

  expertise?: string; // e.g., "Career Development, Personal Growth, Goal Setting"
  services?: string; // e.g., "Available for 1-on-1 sessions"
  availability?: string; // e.g., "Monday - Friday: 9 AM - 6 PM; Weekend: By appointment"

  createdAt?: string; // ISO string from backend
  updatedAt?: string; // ISO string from backend

  location: LocationDTO;
  lastSeen?: string;
  rate?: number;
}