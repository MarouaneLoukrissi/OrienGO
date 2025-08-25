import { TestType } from "../enum/TestType.enum";

export interface TestCreateDTO {
  studentId: number;
  type: TestType;
}