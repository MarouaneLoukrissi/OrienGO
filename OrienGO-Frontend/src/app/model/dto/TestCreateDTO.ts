import { TestType } from "../enum/TestType";

export interface TestCreateDTO {
  studentId: number;
  type: TestType;
}