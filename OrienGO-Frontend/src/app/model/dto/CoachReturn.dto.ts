import { AccountPrivacy } from "../enum/AccountPrivacy.enum";
import { CoachSpecialization } from "../enum/CoachSpecialization.enum";
import { GenderType } from "../enum/GenderType.enum";
import { MessagePermission } from "../enum/MessagePermission.enum";
import { VisibilityStatus } from "../enum/VisibilityStatus.enum";
import { LocationDTO } from "./Location.dto";

export interface CoachReturnDTO {
  // ==============================
  // User (parent class) fields
  // ==============================
  id: number;

  firstName: string;
  lastName: string;
  age: number;
  gender: GenderType;        // enum
  phoneNumber: string;
  email: string;
  enabled: boolean;
  suspended: boolean;
  suspensionReason: string;
  suspendedUntil: string;    // ISO string (Java LocalDateTime → string)
  tokenExpired: boolean;

  // ==============================
  // Coach-specific fields
  // ==============================
  profileVisibility: VisibilityStatus; // enum
  location: LocationDTO;               // another DTO
  messagePermission: MessagePermission; // enum
  accountPrivacy: AccountPrivacy;      // enum
  specialization: CoachSpecialization; // enum
  rate: number;
  expertise: string;
  services: string;
  availability: string;

  // ==============================
  // Audit fields
  // ==============================
  createdAt: string;   // Java LocalDateTime → string
  updatedAt: string;   // Java LocalDateTime → string
}
