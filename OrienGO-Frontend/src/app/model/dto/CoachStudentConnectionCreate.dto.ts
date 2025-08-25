import { RequestInitiator } from "../enum/RequestInitiator.enum";

export interface CoachStudentConnectionCreateDTO {
  coachId: number;
  studentId: number;
  requestedBy: RequestInitiator;
}