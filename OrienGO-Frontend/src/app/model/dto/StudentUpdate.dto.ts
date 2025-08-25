import { EducationLevel } from "../enum/EducationLevel.enum";
import { GenderType } from "../enum/GenderType.enum";
import { LocationDTO } from "./Location.dto";

export interface StudentUpdateDTO {
  firstName: string;
  lastName: string;
  age: number;
  gender: GenderType;
  phoneNumber: string;
  school?: string;
  fieldOfStudy?: string;
  educationLevel: EducationLevel;
  location?: LocationDTO;
}
