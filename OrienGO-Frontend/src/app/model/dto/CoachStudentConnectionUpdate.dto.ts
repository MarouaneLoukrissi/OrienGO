import { ConnectionStatus } from "../enum/ConnectionStatus.enum";
import { RequestInitiator } from "../enum/RequestInitiator.enum";

export interface CoachStudentConnectionUpdateDTO {
  coachId: number;
  studentId: number;
  status: ConnectionStatus;
  respondedAt?: string; // LocalDateTime → string (ISO)
  requestedBy: RequestInitiator;
}