// admin-modify.dto.ts

import { AdminLevel } from "../enum/AdminLevel.dto";
import { Department } from "../enum/Department.enum";
import { GenderType } from "../enum/GenderType.enum";

export interface AdminModifyDTO {
  firstName: string;
  lastName: string;
  age: number;
  gender: GenderType;
  phoneNumber?: string;
  password?: string;
  enabled: boolean;
  suspended: boolean;
  suspensionReason?: string;
  suspendedUntil?: string;
  adminLevel: AdminLevel;
  tokenExpired: boolean;
  department: Department;

  /* createdAt?: string;
  updatedAt?: string;
  lastSeen?: string;
  lastLoginAt?: string;
  deletedAt?: string;
  roles: string[];
  createdById: number;
  isDeleted: boolean; */
}
