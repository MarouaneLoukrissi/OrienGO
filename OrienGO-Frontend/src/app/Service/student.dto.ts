import { LocationDTO } from "../model/dto/Location.dto";
import { AccountPrivacy } from "../model/enum/AccountPrivacy.enum";
import { EducationLevel } from "../model/enum/EducationLevel.enum";
import { GenderType } from "../model/enum/GenderType.enum";
import { MessagePermission } from "../model/enum/MessagePermission.enum";
import { VisibilityStatus } from "../model/enum/VisibilityStatus.enum";


export interface StudentDTO {
  firstName: string;

  lastName: string;

  age: number;

  gender: GenderType;

  phoneNumber?: string;   // optional, since validation allows empty

  email: string;

  password: string;

  // confirmPassword?: string; // uncomment if used in Angular forms

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

  suspendedUntil?: string;  // LocalDateTime → ISO string
}
