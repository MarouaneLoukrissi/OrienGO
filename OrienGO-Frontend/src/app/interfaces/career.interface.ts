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
  jobRecommendationId: number;
  isSaved: boolean;
  isFavorite: boolean;
  backendJobId: number | undefined
}

export interface EducationPath {
  id: string;
  title: string;
  duration: string;
  description: string;
  matchPercentage: number;
  specializations: string[];
  isSaved: boolean;
  isFavorite: boolean;
  backendTrainingId: number | undefined
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
