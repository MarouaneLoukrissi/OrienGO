import { AnswerOptionResponseDTO } from "./AnswerOptionResponseDTO";
import { QuestionResponseDTO } from "./QuestionResponseDTO";

export interface TestQuestionResponseDTO {
  id: number;
  question: QuestionResponseDTO;
  chosenAnswer: AnswerOptionResponseDTO;
}