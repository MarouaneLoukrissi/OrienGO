import { TestType } from "../enum/TestType.enum";

export interface TestCountDTO {
  testType: TestType; // e.g. "COMPLETE" or "EXPRESS"
  count: number;
}