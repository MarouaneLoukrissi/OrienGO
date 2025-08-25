import { AnswerOptionResponseDTO } from "../../../../model/dto/AnswerOptionResponse.dto";

export interface Question {
  id: number;
  text: string;
  answerOptions: AnswerOptionResponseDTO[];
  chosenAnswer: AnswerOptionResponseDTO | null;
}
