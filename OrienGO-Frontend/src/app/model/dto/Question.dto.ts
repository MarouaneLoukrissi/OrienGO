import { Category } from "../enum/Category.enum";

export interface QuestionDTO {
  category: Category;
  text: string;
}
