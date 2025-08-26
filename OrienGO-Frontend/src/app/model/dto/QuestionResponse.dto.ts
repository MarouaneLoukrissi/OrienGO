import { Category } from '../enum/Category.enum';
import { AnswerOptionResponseDTO } from './AnswerOptionResponse.dto';

export interface QuestionResponseDTO {
  category?: Category;
  id: number;
  text: string;
  answerOptions: AnswerOptionResponseDTO[];
  softDeleted?: boolean
}
