export interface Career {
  id: string;
  title: string;
  category: string;
  categoryColor: string;
  description: string;
  education: string;
  salary: string;
  jobMarket: string;
  matchPercentage: number;
  skills: string[];
}

export interface EducationPath {
  id: string;
  title: string;
  duration: string;
  description: string;
  matchPercentage: number;
  specializations: string[];
}

export interface UserProfile {
  id: string;
  riasecProfile: string;
  preferences?: any;
}

export interface RecommendationsResponse {
  careers: Career[];
  educationPaths: EducationPath[];
  userProfile: UserProfile;
}