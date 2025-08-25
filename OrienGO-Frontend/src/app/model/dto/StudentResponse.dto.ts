import { AccountPrivacy } from "../enum/AccountPrivacy.enum";
import { EducationLevel } from "../enum/EducationLevel.enum";
import { MessagePermission } from "../enum/MessagePermission.enum";
import { VisibilityStatus } from "../enum/VisibilityStatus.enum";
import { GenderType } from "../enum/GenderType.enum";
import { LocationDTO } from "./Location.dto";

export interface StudentResponseDTO {
  id: number;

  firstName: string;
  lastName: string;
  age: number;
  gender: GenderType;

  phoneNumber: string;
  email: string;

  school?: string;
  fieldOfStudy?: string;
  educationLevel: EducationLevel;

  location?: LocationDTO;

  profileVisibility: VisibilityStatus;
  messagePermission: MessagePermission;
  accountPrivacy: AccountPrivacy;

  createdAt: string;  // LocalDateTime → ISO string
  updatedAt: string;
}
