import { ConnectionStatus } from "../enum/ConnectionStatus.enum";
import { RequestInitiator } from "../enum/RequestInitiator.enum";

export interface CoachStudentConnectionResponseDTO {
  id: number;
  coachId: number;
  studentId: number;
  status: ConnectionStatus;
  requestedAt: string; // LocalDateTime → string
  respondedAt?: string;
  requestedBy: RequestInitiator;
}