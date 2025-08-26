// admin.dto.ts

import { AdminLevel } from "../enum/AdminLevel.dto";
import { Department } from "../enum/Department.enum";
import { GenderType } from "../enum/GenderType.enum";

export interface AdminDTO {
  firstName: string;
  lastName: string;
  age: number;
  gender: GenderType;
  phoneNumber?: string;
  email: string;
  password: string;
  enabled: boolean;
  suspended: boolean;
  suspensionReason?: string;
  suspendedUntil?: string;
  adminLevel: AdminLevel;
  createdById: number;
  department: Department;

  /* tokenExpired: boolean;
  createdAt?: string; // or Date
  updatedAt?: string;
  lastSeen?: string;
  lastLoginAt?: string;
  deletedAt?: string;
  roles: string[];
  isDeleted: boolean; */
}
