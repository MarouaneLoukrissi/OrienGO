import { LinkType } from "../enum/LinkType.enum";

export interface PersonalizedJobResponseDto {
  id: number;

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
  requiredSkills?: string;
  companyUrl?: string;
  companyUrlDirect?: string;
  companyAddresses?: string;
  companyNumEmployees?: number;
  companyRevenue?: string;
  companyDescription?: string;
  experienceRange?: string;
  emails?: string;
  companyIndustry?: string;
  jobUrlDirect?: string;
  isRemote?: boolean;

  // Other fields
  createdAt: string; // LocalDateTime → ISO string
  expirationDate?: string;
  duration?: string;
  advantages?: string[];
  matchPercentage: number;
  highlighted?: boolean;

  jobRecommendationId: number;

  linkTypes: LinkType[]

  softDeleted: boolean;
}
