// admin-response.dto.ts

import { AdminLevel } from "../enum/AdminLevel.dto";
import { Department } from "../enum/Department.enum";

export interface AdminResponseDTO {
  id: number;
  firstName: string;
  lastName: string;
  age: number;
  gender: string; // mapped from enum to string
  phoneNumber?: string;
  email: string;
  enabled: boolean;
  adminLevel: AdminLevel;
  department: Department;
  createdById: number;
  createdAt?: string;
  updatedAt?: string;
  lastSeen?: string;
}
