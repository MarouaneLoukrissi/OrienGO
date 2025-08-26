// admin-return.dto.ts

import { AdminLevel } from "../enum/AdminLevel.dto";
import { Department } from "../enum/Department.enum";
import { GenderType } from "../enum/GenderType.enum";

export interface AdminReturnDTO {
  id: number;
  firstName: string;
  lastName: string;
  age: number;
  gender: GenderType;
  phoneNumber?: string;
  email: string;
  enabled: boolean;
  tokenExpired: boolean;
  createdAt?: string;
  updatedAt?: string;
  lastSeen?: string;
  suspended: boolean;
  suspensionReason?: string;
  suspendedUntil?: string;
  lastLoginAt?: string;
  deletedAt?: string;
  //roles: string[];
  adminLevel: AdminLevel;
  createdById: number;
  department: Department;
  isDeleted: boolean;
}
