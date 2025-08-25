import { AnswerOptionResponseDTO } from "./AnswerOptionResponse.dto";
import { QuestionResponseDTO } from "./QuestionResponse.dto";

export interface TestQuestionResponseDTO {
  id: number;
  question: QuestionResponseDTO;
  chosenAnswer: AnswerOptionResponseDTO;
}