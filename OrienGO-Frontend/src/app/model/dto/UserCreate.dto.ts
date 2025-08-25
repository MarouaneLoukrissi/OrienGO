import { GenderType } from "../enum/GenderType.enum";

export interface UserCreateDTO {
  firstName: string;
  lastName: string;
  age: number;
  gender: GenderType;
  phoneNumber: string;
  email: string;
  password: string;
  // confirmPassword?: string; // uncomment if you add this in backend
}
