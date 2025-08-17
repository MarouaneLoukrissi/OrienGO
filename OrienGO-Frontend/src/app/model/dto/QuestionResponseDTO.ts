import { AnswerOptionResponseDTO } from './AnswerOptionResponseDTO';

export interface QuestionResponseDTO {
  id: number;
  text: string;
  answerOptions: AnswerOptionResponseDTO[];
}
