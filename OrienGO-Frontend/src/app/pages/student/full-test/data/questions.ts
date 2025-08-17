import { AnswerOptionResponseDTO } from "../../../../model/dto/AnswerOptionResponseDTO";

export interface Question {
  id: number;
  text: string;
  answerOptions: AnswerOptionResponseDTO[];
  chosenAnswer: AnswerOptionResponseDTO | null;
}
