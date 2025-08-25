import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NotificationService } from '../../../Service/notification.service';
import { TestResultService } from '../../../Service/testResult.service';
import { PdfGeneratorService, RiasecData } from '../../../Service/pdf-generator.service';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { TestResultMapMediaDTO } from '../../../model/dto/TestResultMapMedia.dto';
import { MediaService } from '../../../Service/media.service';
import { MediaType } from '../../../model/enum/MediaType.enum';

type ScoreKey = 'REALISTIC' | 'INVESTIGATIVE' | 'ARTISTIC' | 'SOCIAL' | 'ENTERPRISING' | 'CONVENTIONAL';
const RIASEC_ORDER: ScoreKey[] = ['REALISTIC', 'INVESTIGATIVE', 'ARTISTIC', 'SOCIAL', 'ENTERPRISING', 'CONVENTIONAL'];

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrl: './results.component.css'
})
export class ResultsComponent implements OnInit{
  title = 'riasec-results';
  isGeneratingPdf = false;
  isGeneratingSharedPdf = false;

  dominantProfile: {
    key: ScoreKey;
    value: number;
    description: string;
  } | null = null;

  radarData = {
    R: 0,
    I: 0,
    A: 0,
    S: 0,
    E: 0,
    C: 0
  };

  barData = {
    R: 0,
    I: 0,
    A: 0,
    S: 0,
    E: 0,
    C: 0
  };

  top3Scores: Array<{
    key: ScoreKey;    // use ScoreKey here
    value: number;
    color: string;
    shortLabel: string;
    descriptionKey: string;
  }> = [];

  colorMap: Record<ScoreKey, string> = {
    REALISTIC: '#3b82f6',
    INVESTIGATIVE: '#8b5cf6',
    ARTISTIC: '#ec4899',
    SOCIAL: '#10b981',
    ENTERPRISING: '#ef4444',
    CONVENTIONAL: '#6366f1'
  };

  shortLabelMap: Record<ScoreKey, string> = {
    REALISTIC: 'R',
    INVESTIGATIVE: 'I',
    ARTISTIC: 'A',
    SOCIAL: 'S',
    ENTERPRISING: 'E',
    CONVENTIONAL: 'C'
  };

  descriptionKeyMap: Record<ScoreKey, string> = {
    REALISTIC: 'RIASEC_RESULTS.REALISTIC_SHORT',
    INVESTIGATIVE: 'RIASEC_RESULTS.INVESTIGATOR',
    ARTISTIC: 'RIASEC_RESULTS.ARTISTIC',
    SOCIAL: 'RIASEC_RESULTS.SOCIAL',
    ENTERPRISING: 'RIASEC_RESULTS.ENTERPRISING',
    CONVENTIONAL: 'RIASEC_RESULTS.CONVENTIONAL'
  };

  constructor(
    private notificationService: NotificationService,
    private router: Router,
    private route: ActivatedRoute,
    private testResultService: TestResultService,
    private pdfGeneratorService: PdfGeneratorService,
    private http: HttpClient,
    private mediaService: MediaService
  ) {}

  userId : number = 1;
  testId: string | null = null;
  testResultId : number | null = null;
  mediaId : number | null = null;
  pdfFetched : boolean = false;
  

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.testId = params.get('testId');

      if (this.testId) {
        this.loadResult(this.testId);
      } else {
        this.notificationService.showError('No test ID found in URL.');
      }
    });
  }

  private async loadResult(testId: string) {
    this.testResultService.getByTestId(Number(testId)).subscribe({
      next: async (res) => {
        const round1 = (num: number) => Math.round((num + Number.EPSILON) * 10) / 10;
        this.testResultId = res.data.id;

        this.radarData = {
          R: round1(res.data.scores.REALISTIC ?? 0),
          I: round1(res.data.scores.INVESTIGATIVE ?? 0),
          A: round1(res.data.scores.ARTISTIC ?? 0),
          S: round1(res.data.scores.SOCIAL ?? 0),
          E: round1(res.data.scores.ENTERPRISING ?? 0),
          C: round1(res.data.scores.CONVENTIONAL ?? 0)
        };
        this.barData = { ...this.radarData };

        this.detailedScores = this.detailedScores.map(score => {
          let backendScore = 0;
          switch(score.label.charAt(0)) {
            case 'R': backendScore = res.data.scores.REALISTIC ?? 0; break;
            case 'I': backendScore = res.data.scores.INVESTIGATIVE ?? 0; break;
            case 'A': backendScore = res.data.scores.ARTISTIC ?? 0; break;
            case 'S': backendScore = res.data.scores.SOCIAL ?? 0; break;
            case 'E': backendScore = res.data.scores.ENTERPRISING ?? 0; break;
            case 'C': backendScore = res.data.scores.CONVENTIONAL ?? 0; break;
          }
          return { ...score, percentage: round1(backendScore) };
        });

        this.dominantProfile = {
          key: res.data.dominantType,
          value: round1(res.data.scores[res.data.dominantType] ?? 0),
          description: res.data.dominantTypeDescription
        };

        this.mediaId = res.data.pdfId ?? null;

        // Sort scores and take top 3
        const scoresArray = Object.entries(res.data.scores)
          .map(([key, value]) => ({ key: key as ScoreKey, value: round1(value ?? 0) }));

        scoresArray.sort((a, b) => {
          if (b.value !== a.value) return b.value - a.value;
          return RIASEC_ORDER.indexOf(a.key) - RIASEC_ORDER.indexOf(b.key);
        });

        this.top3Scores = scoresArray.slice(0, 3).map(score => ({
          key: score.key,
          value: score.value,
          color: this.colorMap[score.key] || '#888',
          shortLabel: this.shortLabelMap[score.key] || score.key.charAt(0),
          descriptionKey: this.descriptionKeyMap[score.key] || ''
        }));

        console.log(res);

        // Attempt to fetch PDF if mediaId exists
        this.pdfFetched = false;
        if (this.mediaId) {
          try {
            await firstValueFrom(this.mediaService.getMediaFileById(this.mediaId));
            this.pdfFetched = true; // PDF exists on backend
          } catch (err) {
            console.warn('Failed to fetch PDF from backend, will generate a new one.', err);
          }
        }

        // Only upload/generate in background if no PDF exists or fetch failed
        if (!this.mediaId || !this.pdfFetched) {
          this.uploadPdfInBackground(this.testResultId);
        }

      },
      error: (err) => {
        const serverMessage = err.error?.message || 'Unknown error occurred';
        this.notificationService.showError(serverMessage);
        console.log(err);
      }
    });
  }


  async downloadPdf() {
    if (!this.dominantProfile) {
      this.notificationService.showError('Results not loaded yet. Please wait.');
      return;
    }

    this.isGeneratingPdf = true;
    
    try {

      if (this.mediaId) {
        try {
          const pdfBlob = await firstValueFrom(
            this.mediaService.getMediaFileById(this.mediaId)
          );
          const fileName = `RIASEC_Results_User_${this.userId}_Test_${this.testId}_${new Date().toISOString().split('T')[0]}.pdf`;
          const blobUrl = URL.createObjectURL(pdfBlob);
          const a = document.createElement('a');
          a.href = blobUrl;
          a.download = fileName;
          a.click();
          URL.revokeObjectURL(blobUrl);

          this.notificationService.showSuccess('PDF loaded from server.');
          return; // Stop here if successful
        } catch (fetchError) {
          console.warn('Failed to fetch PDF from server, generating a new one.', fetchError);
          // Continue to generate PDF below
        }
      }
      const riasecData: RiasecData = {
        dominantProfile: {
          key: this.dominantProfile.key,
          value: this.dominantProfile.value,
          description: this.dominantProfile.description
        },
        scores: this.detailedScores.map(score => ({
          key: score.label,
          value: score.percentage,
          color: score.color,
          shortLabel: score.label.charAt(0),
          description: score.description,
          tags: score.tags
        })),
        top3Scores: this.top3Scores.map(score => ({
          key: score.key,
          value: score.value,
          color: score.color,
          shortLabel: score.shortLabel,
          description: score.descriptionKey
        })),
        keyPoints: this.keyPoints,
        testDate: new Date().toISOString(),
        userName: 'User' // You can get this from authentication service if available
      };

      await this.pdfGeneratorService.generateRiasecPdf(riasecData, Number(this.testId), this.userId);
      this.notificationService.showSuccess('PDF downloaded successfully!');
    } catch (error) {
      console.error('Error generating PDF:', error);
      this.notificationService.showError('Failed to generate PDF. Please try again.');
    } finally {
      this.isGeneratingPdf = false;
    }
  }

  async sharePdfLink() {
    if (!this.dominantProfile) {
      this.notificationService.showError('Results not loaded yet. Please wait.');
      return;
    }

    this.isGeneratingSharedPdf = true;

    try {
      let pdfBlob: Blob | null = null;

      // Step 1: Try to fetch existing PDF from server
      if (this.mediaId) {
        try {
          //console.log("7777777777777")
          pdfBlob = await firstValueFrom(
            this.mediaService.getMediaFileById(this.mediaId)
          );
          console.log('PDF loaded from server.');
        } catch (fetchError) {
          //console.log("6666666666666")
          console.warn('Failed to fetch PDF from server, generating a new one.', fetchError);
          pdfBlob = null; // Will generate below
        }
      }

      // Step 2: Generate PDF if not found on server
      if (!pdfBlob) {
        //console.log("888888888888")
        const riasecData: RiasecData = {
          dominantProfile: {
            key: this.dominantProfile.key,
            value: this.dominantProfile.value,
            description: this.dominantProfile.description
          },
          scores: this.detailedScores.map(score => ({
            key: score.label,
            value: score.percentage,
            color: score.color,
            shortLabel: score.label.charAt(0),
            description: score.description,
            tags: score.tags
          })),
          top3Scores: this.top3Scores.map(score => ({
            key: score.key,
            value: score.value,
            color: score.color,
            shortLabel: score.shortLabel,
            description: score.descriptionKey
          })),
          keyPoints: this.keyPoints,
          testDate: new Date().toISOString(),
          userName: 'User' // Optional: get from auth service
        };

        pdfBlob = await this.pdfGeneratorService.generatePdfBlob(riasecData);
        console.log('PDF generated.');
      }

      // Step 3: Share or open PDF
      const fileName = `RIASEC_Results_User_${this.userId}_Test_${this.testId}_${new Date().toISOString().split('T')[0]}.pdf`;
      const file = new File([pdfBlob], fileName, { type: 'application/pdf' });

      if (navigator.share && navigator.canShare?.({ files: [file] })) {
        await navigator.share({
          title: 'My RIASEC Test Results',
          text: 'Here are my RIASEC test results!',
          files: [file]
        });
      } else {
        const url = URL.createObjectURL(pdfBlob);
        window.open(url, '_blank');
      }
    } catch (error) {
      console.error('Error sharing PDF:', error);
      this.notificationService.showError('Failed to share PDF. Please try again.');
    } finally {
      this.isGeneratingSharedPdf = false;
    }
  }

  private async uploadPdfInBackground(testResultId: number) {
    if (!this.dominantProfile) return;

    try {
      const riasecData: RiasecData = {
        dominantProfile: {
          key: this.dominantProfile.key,
          value: this.dominantProfile.value,
          description: this.dominantProfile.description
        },
        scores: this.detailedScores.map(score => ({
          key: score.label,
          value: score.percentage,
          color: score.color,
          shortLabel: score.label.charAt(0),
          description: score.description,
          tags: score.tags
        })),
        top3Scores: this.top3Scores.map(score => ({
          key: score.key,
          value: score.value,
          color: score.color,
          shortLabel: score.shortLabel,
          description: score.descriptionKey
        })),
        keyPoints: this.keyPoints,
        testDate: new Date().toISOString(),
        userName: 'User'
      };

      // Call the service that generates & uploads
      await this
        .generateAndUploadPdf(riasecData, Number(this.testId), this.userId, testResultId);

      console.log('PDF saved in background.');
    } catch (error) {
      console.error('Background PDF upload failed:', error);
    }
  }


  generateAndUploadPdf(riasecData: RiasecData, testId: number, userId: number, testResultId: number) {
    this.pdfGeneratorService.generatePdfBlob(riasecData).then(pdfBlob => {
      // Step 1: Upload PDF
      const formData = new FormData();
      const fileName = `RIASEC_Results_User_${userId}_Test_${testId}_${new Date().toISOString().split('T')[0]}.pdf`;
      formData.append('media', new File([pdfBlob], fileName, { type: 'application/pdf' }));
      formData.append('userId', userId.toString());
      formData.append('type', MediaType.RESULT_PDF)
      this.mediaService.createMedia(formData).subscribe({
        next: (res) => {
          this.mapTestResult(testResultId, res.data.id)
        },
        error: (err) => {
          console.error('Failed to upload PDF: ', err)
        }
      });
    });
  }

  mapTestResult(testResultId: number, mediaId: number) {
    const dto: TestResultMapMediaDTO = {
      id: testResultId??0,
      mediaId: mediaId??0
    };

    this.testResultService.mapTestResultToMedia(dto).subscribe({
      next: (res) => {
        this.mediaId=mediaId;
        this.pdfFetched=true
        console.log('PDF stored in DB and mapped successfully');
      },
      error: (err) => {
        console.error('Failed to map PDF to test result:', err)
      }
    });
  }

  detailedScores = [
    {
      label: 'Realistic (R)',
      percentage: 0,
      color: '#3b82f6',
      description: 'Practical, hands-on, action-oriented',
      tags: ['Tool handling', 'Concrete activities', 'Practical problem-solving']
    },
    {
      label: 'Investigative (I)',
      percentage: 0,
      color: '#8b5cf6',
      description: 'Analytical, curious, scientific',
      tags: ['Research', 'Analysis', 'Experimentation']
    },
    {
      label: 'Artistic (A)',
      percentage: 0,
      color: '#ec4899',
      description: 'Creative, expressive, original',
      tags: ['Creativity', 'Artistic expression', 'Innovation']
    },
    {
      label: 'Social (S)',
      percentage: 0,
      color: '#10b981',
      description: 'Altruistic, cooperative, kind',
      tags: ['Helping others', 'Communication', 'Teaching']
    },
    {
      label: 'Enterprising (E)',
      percentage: 0,
      color: '#ef4444',
      description: 'Persuasive, ambitious, energetic',
      tags: ['Leadership', 'Persuasion', 'Risk-taking']
    },
    {
      label: 'Conventional (C)',
      percentage: 0,
      color: '#6366f1',
      description: 'Organized, precise, methodical',
      tags: ['Organization', 'Precision', 'Procedures']
    }
  ];

  keyPoints = [
    {
      title: 'Primary Orientation',
      description: 'Your Realistic profile indicates a preference for practical, hands-on work oriented toward action.'
    },
    {
      title: 'Natural Skills',
      description: 'Your strengths include: tool handling, concrete activities, practical problem-solving.'
    },
    {
      title: 'Ideal Environment',
      description: 'You will thrive in environments that value your dominant traits.'
    },
    {
      title: 'Development Potential',
      description: 'Your other dimensions (I, A) offer complementary opportunities'
    }
  ];
}
