import { GenderType } from "../enum/GenderType.enum";

export interface UserResponseDTO {
  id: number;

  firstName: string;
  lastName: string;
  age: number;
  gender: GenderType;
  phoneNumber: string;
  email: string;

  enabled: boolean;
  tokenExpired: boolean;

  createdAt: string;   // ISO string from backend (LocalDateTime → string)
  updatedAt: string;
  lastSeen: string | null;

  suspended: boolean;
  suspensionReason: string | null;
  suspendedUntil: string | null;

  lastLoginAt: string | null;

  roles: Set<string>;   // roles are just names in backend
  deletedAt: string | null;

  isDeleted: boolean;
  online: boolean;
}
