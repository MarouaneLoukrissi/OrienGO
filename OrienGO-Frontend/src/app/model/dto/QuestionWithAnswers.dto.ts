import { Category } from "../enum/Category.enum";
import { AnswerOptionFilteredDTO } from "./AnswerOptionFiltered.dto";

export interface QuestionWithAnswersDTO {
  text: string;
  category: Category;
  answerOptions: AnswerOptionFilteredDTO[]; // Must always be exactly 5
}