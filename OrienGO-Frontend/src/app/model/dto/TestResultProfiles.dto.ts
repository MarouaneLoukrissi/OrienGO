import { Category } from '../enum/Category.enum';

export interface ProfileScoreDTO {
  category: Category;
  percentage: number;
}

export interface TestResultProfilesDTO {
  profiles: ProfileScoreDTO[];
}
