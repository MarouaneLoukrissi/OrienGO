import { CoachSpecialization } from "../enum/CoachSpecialization.enum";
import { GenderType } from "../enum/GenderType.enum";
import { LocationDTO } from "./Location.dto";

export interface CoachUpdateProfileDTO {
  firstName: string;
  lastName: string;
  age: number;
  gender: GenderType;
  phoneNumber?: string;
  specialization?: CoachSpecialization
  expertise?: string; // e.g., "Career Development, Personal Growth, Goal Setting"
  services?: string; // e.g., "Available for 1-on-1 sessions"
  availability?: string; // e.g., "Monday - Friday: 9 AM - 6 PM; Weekend: By appointment"
  location?: LocationDTO; // if you have LocationDTO in TS
}