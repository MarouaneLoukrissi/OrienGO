import { AccountPrivacy } from "../enum/AccountPrivacy.enum";
import { CoachSpecialization } from "../enum/CoachSpecialization.enum";
import { GenderType } from "../enum/GenderType.enum";
import { MessagePermission } from "../enum/MessagePermission.enum";
import { VisibilityStatus } from "../enum/VisibilityStatus.enum";
import { LocationDTO } from "./Location.dto";

export interface CoachModifyDTO {
  // ==============================
  // User fields
  // ==============================
  firstName: string;
  lastName: string;
  age: number;
  gender: GenderType;                // enum
  phoneNumber: string;
  email: string;
  password: string;

  enabled: boolean;
  suspended: boolean;
  suspensionReason?: string;         // optional (can be null/empty)
  suspendedUntil?: string;           // LocalDateTime → string (ISO)

  // ==============================
  // Coach-specific fields
  // ==============================
  profileVisibility: VisibilityStatus;     // enum
  location: LocationDTO;                   // nested DTO
  messagePermission: MessagePermission;    // enum
  accountPrivacy: AccountPrivacy;          // enum
  specialization: CoachSpecialization;     // enum

  rate?: number;
  expertise?: string;
  services?: string;
  availability?: string;
}
