import { GenderType } from "../enum/GenderType.enum";
import { LocationDTO } from "./Location.dto";

export interface CoachCreateDTO {
  firstName: string;
  lastName: string;
  age: number;
  gender: GenderType;
  phoneNumber?: string;  // optional, since backend allows null if not set
  email: string;
  password: string;
  location?: LocationDTO
}