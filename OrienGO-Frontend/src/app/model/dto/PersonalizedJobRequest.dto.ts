export interface PersonalizedJobRequestDto {
  title: string;
  companyName?: string;
  location?: string;
  jobType?: string;
  description?: string;
  applyUrl?: string;
  salaryRange?: string;
  category?: string;
  source?: string;
  postedDate?: string; // LocalDate → ISO string

  // Extra company/job info
  requiredSkills?: string; // comma-separated
  companyUrl?: string;
  companyUrlDirect?: string;
  companyAddresses?: string; // comma-separated
  companyNumEmployees?: number;
  companyRevenue?: string;
  companyDescription?: string;
  experienceRange?: string;
  emails?: string; // comma-separated
  companyIndustry?: string;
  jobUrlDirect?: string;
  isRemote?: boolean;

  // Other fields
  expirationDate?: string; // LocalDate → ISO string
  duration?: string;
  advantages?: string[];
  matchPercentage: number;
  highlighted?: boolean;
  jobRecommendationId: number;
}
