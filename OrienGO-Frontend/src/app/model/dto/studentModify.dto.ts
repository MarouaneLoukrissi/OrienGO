import { AccountPrivacy } from '../enum/AccountPrivacy.enum';
import { EducationLevel } from '../enum/EducationLevel.enum';
import { GenderType } from '../enum/GenderType.enum';
import { MessagePermission } from '../enum/MessagePermission.enum';
import { VisibilityStatus } from '../enum/VisibilityStatus.enum';
import { LocationDTO } from './Location.dto';

export interface StudentModifyDTO {
  firstName: string;

  lastName: string;

  age: number;

  gender: GenderType;

  phoneNumber?: string;   // optional (nullable in backend)

  password: string;

  // confirmPassword?: string; // keep if you use it in Angular forms

  school?: string;

  fieldOfStudy?: string;

  educationLevel: EducationLevel;

  messagePermission: MessagePermission;

  accountPrivacy: AccountPrivacy;

  profileVisibility: VisibilityStatus;

  location: LocationDTO;

  // Added fields
  enabled: boolean;

  suspended: boolean;

  suspensionReason?: string;

  suspendedUntil?: string; // LocalDateTime → ISO string
}
