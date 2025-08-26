// admin-update.dto.ts

import { AdminLevel } from "../enum/AdminLevel.dto";
import { Department } from "../enum/Department.enum";
import { GenderType } from "../enum/GenderType.enum";

export interface AdminUpdateDTO {
  firstName: string;
  lastName: string;
  age: number;
  gender?: GenderType;
  phoneNumber?: string;
  password: string;
  adminLevel: AdminLevel;
  department: Department;
}
