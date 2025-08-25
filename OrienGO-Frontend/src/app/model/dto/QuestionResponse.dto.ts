import { AnswerOptionResponseDTO } from './AnswerOptionResponse.dto';

export interface QuestionResponseDTO {
  id: number;
  text: string;
  answerOptions: AnswerOptionResponseDTO[];
}
