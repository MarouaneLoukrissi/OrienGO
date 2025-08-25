import { AccountPrivacy } from "../enum/AccountPrivacy.enum";
import { EducationLevel } from "../enum/EducationLevel.enum";
import { GenderType } from "../enum/GenderType.enum";
import { MessagePermission } from "../enum/MessagePermission.enum";
import { VisibilityStatus } from "../enum/VisibilityStatus.enum";
import { LocationDTO } from "./Location.dto";

export interface StudentCreateDTO {
  firstName: string;
  lastName: string;
  age: number;
  gender: GenderType;
  phoneNumber: string;
  email: string;
  password: string;
  // confirmPassword?: string; // if you enable it in backend

  school?: string;
  fieldOfStudy?: string;
  educationLevel: EducationLevel;
  messagePermission: MessagePermission;
  accountPrivacy: AccountPrivacy;
  profileVisibility: VisibilityStatus;
  location?: LocationDTO;
}
