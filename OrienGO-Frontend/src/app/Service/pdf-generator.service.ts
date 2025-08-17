import { Injectable } from '@angular/core';
import jsPDF from 'jspdf';

export interface RiasecScore {
  key: string;
  value: number;
  color: string;
  shortLabel: string;
  description: string;
  tags: string[];
}

export interface RiasecData {
  dominantProfile: {
    key: string;
    value: number;
    description: string;
  };
  scores: RiasecScore[];
  top3Scores: Array<{
    key: string;
    value: number;
    color: string;
    shortLabel: string;
    description: string;
  }>;
  keyPoints: Array<{
    title: string;
    description: string;
  }>;
  testDate?: string;
  userName?: string;
}

@Injectable({
  providedIn: 'root',
})
export class PdfGeneratorService {
  private readonly pageWidth = 210; // A4 width in mm
  private readonly pageHeight = 297; // A4 height in mm
  private readonly margin = 6; // Reduced margin
  private readonly contentWidth = this.pageWidth - (this.margin * 2);

  async generateRiasecPdf(data: RiasecData, testId: number, userId: number): Promise<void> {
    const pdf = new jsPDF('p', 'mm', 'a4');
    let yPosition = this.margin;

    // Add header
    yPosition = this.addHeader(pdf, yPosition, data.dominantProfile.key);
    
    // Add dominant profile section
    yPosition = this.addDominantProfile(pdf, data.dominantProfile, yPosition);
    
    // Add top 3 scores
    yPosition = this.addTop3Scores(pdf, data.top3Scores, yPosition);
    
    // Add detailed scores
    yPosition = this.addDetailedScores(pdf, data.scores, yPosition);
    
    // Add second page for key insights
    //pdf.addPage();
    //yPosition = this.margin;
    
    // Add key insights on second page
    this.addKeyPoints(pdf, data.keyPoints, yPosition);
    
    // Add footer
    this.addFooter(pdf);
    
    // Save the PDF
    const fileName = `RIASEC_Results_User_${userId}_Test_${testId}_${new Date().toISOString().split('T')[0]}.pdf`;
    pdf.save(fileName);
  }

  private addHeader(pdf: jsPDF, yPosition: number, dominantKey: string): number {
    // Add logo/icon area - smaller
    pdf.setFillColor(245, 101, 12);
    pdf.circle(this.pageWidth / 2, yPosition + 10, 9, 'F'); // Larger circle
    
    // Add white icon in circle
    pdf.setTextColor(255, 255, 255);
    pdf.setFontSize(22);
    pdf.setFont('helvetica', 'bold');
    pdf.text(dominantKey.charAt(0), this.pageWidth / 2, yPosition + 12.8, { align: 'center' });
    
    yPosition += 28; // More space between circle and title
    
    // Add title
    pdf.setTextColor(51, 51, 51);
    pdf.setFontSize(18);
    pdf.setFont('helvetica', 'bold');
    pdf.text('RIASEC Test Results', this.pageWidth / 2, yPosition, { align: 'center' });
    
    yPosition += 8;
    
    // Add subtitle
    pdf.setFontSize(11);
    pdf.setFont('helvetica', 'normal');
    pdf.setTextColor(102, 102, 102);
    pdf.text('Comprehensive Career Interest Assessment Report', this.pageWidth / 2, yPosition, { align: 'center' });
    
    yPosition += 6;
    
    // Add date
    const currentDate = new Date().toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
    pdf.setFontSize(9);
    pdf.text(`Generated on ${currentDate}`, this.pageWidth / 2, yPosition, { align: 'center' });
    
    return yPosition + 15;
  }

  private addDominantProfile(pdf: jsPDF, dominantProfile: any, yPosition: number): number {
    // Section title
    pdf.setFontSize(14);
    pdf.setFont('helvetica', 'bold');
    pdf.setTextColor(51, 51, 51);
    pdf.text('Dominant Profile', this.margin, yPosition);
    
    yPosition += 4;
    
    // Profile box
    pdf.setFillColor(245, 101, 12);
    pdf.roundedRect(this.margin, yPosition, this.contentWidth, 16, 2, 2, 'F');
    
    // Profile name - properly centered vertically
    pdf.setTextColor(255, 255, 255);
    pdf.setFontSize(14);
    pdf.setFont('helvetica', 'bold');
    pdf.text(dominantProfile.key, this.margin + 10, yPosition + 10);
    
    // Profile percentage - properly centered vertically
    pdf.setFontSize(16);
    pdf.text(`${dominantProfile.value}%`, this.pageWidth - this.margin - 25, yPosition + 10);
    
    yPosition += 12;
    
    // Remove the duplicate profile key text that appears below
    // Profile description
    pdf.setTextColor(102, 102, 102);
    pdf.setFontSize(10);
    pdf.setFont('helvetica', 'normal');
    const descriptionLines = pdf.splitTextToSize(dominantProfile.description, this.contentWidth);
    
    return yPosition + (descriptionLines.length * 4) + 12;
  }

  private addTop3Scores(pdf: jsPDF, top3Scores: any[], yPosition: number): number {
    // Section title
    pdf.setFontSize(14);
    pdf.setFont('helvetica', 'bold');
    pdf.setTextColor(51, 51, 51);
    pdf.text('Top 3 Scores', this.margin, yPosition);
    
    yPosition += 4;
    
    // Calculate box width with proper spacing for center alignment
    const totalSpacing = 16; // 8px between each box
    const boxWidth = (this.contentWidth - totalSpacing) / 3;
    const startX = this.margin + (this.contentWidth - (3 * boxWidth + totalSpacing)) / 2;
    
    top3Scores.forEach((score, index) => {
      const xPosition = startX + (index * (boxWidth + 8));
      
      // Score box
      pdf.setFillColor(249, 250, 251);
      pdf.roundedRect(xPosition, yPosition, boxWidth, 25, 1, 1, 'F');
      
      // Color indicator
      const rgb = this.hexToRgb(score.color);
      if (rgb) {
        pdf.setFillColor(rgb.r, rgb.g, rgb.b);
        pdf.circle(xPosition + boxWidth / 2, yPosition + 6, 2, 'F');
      }
      
      // Score label - centered
      pdf.setTextColor(51, 51, 51);
      pdf.setFontSize(9);
      pdf.setFont('helvetica', 'bold');
      pdf.text(score.shortLabel, xPosition + boxWidth / 2, yPosition + 12, { align: 'center' });
      
      // Score percentage - centered
      pdf.setTextColor(245, 101, 12);
      pdf.setFontSize(12);
      pdf.setFont('helvetica', 'bold');
      pdf.text(`${score.value}%`, xPosition + boxWidth / 2, yPosition + 20, { align: 'center' });
    });
    
    return yPosition + 36;
  }

  private addDetailedScores(pdf: jsPDF, scores: RiasecScore[], yPosition: number): number {
    // Section title
    pdf.setFontSize(14);
    pdf.setFont('helvetica', 'bold');
    pdf.setTextColor(51, 51, 51);
    pdf.text('Detailed Scores', this.margin, yPosition);
    
    yPosition += 10;
    
    // Create a two-column layout for better space usage
    const columnWidth = (this.contentWidth - 12) / 2;
    let leftColumnY = yPosition;
    let rightColumnY = yPosition;
    let isLeftColumn = true;
    
    scores.forEach((score, index) => {
      const currentY = isLeftColumn ? leftColumnY : rightColumnY;
      const xPosition = isLeftColumn ? this.margin : this.margin + columnWidth + 12;
      
      // Score name and percentage
      pdf.setFontSize(12);
      pdf.setFont('helvetica', 'bold');
      pdf.setTextColor(51, 51, 51);
      pdf.text(score.key, xPosition, currentY);
      
      pdf.setTextColor(245, 101, 12);
      pdf.text(`${score.value}%`, xPosition + columnWidth - 20, currentY);
      
      let newY = currentY + 6;
      
      // Description with better spacing
      pdf.setTextColor(102, 102, 102);
      pdf.setFontSize(10);
      pdf.setFont('helvetica', 'normal');
      const shortDesc = score.description.substring(0, 100) + (score.description.length > 100 ? '...' : '');
      const descLines = pdf.splitTextToSize(shortDesc, columnWidth - 5);
      pdf.text(descLines, xPosition, newY);
      
      newY += (descLines.length * 4); // Better line spacing
      
      // Progress bar
      const barWidth = columnWidth - 5;
      const barHeight = 4;
      
      // Background bar
      pdf.setFillColor(229, 231, 235);
      pdf.rect(xPosition, newY, barWidth, barHeight, 'F');
      
      // Progress bar
      const rgb = this.hexToRgb(score.color);
      if (rgb) {
        pdf.setFillColor(rgb.r, rgb.g, rgb.b);
        pdf.rect(xPosition, newY, (barWidth * score.value) / 100, barHeight, 'F');
      }
      
      newY += 12; // Better spacing between items
      
      // Update column positions
      if (isLeftColumn) {
        leftColumnY = newY;
      } else {
        rightColumnY = newY;
      }
      
      isLeftColumn = !isLeftColumn;
    });
    
    return Math.max(leftColumnY, rightColumnY) + 5;
  }

  private addKeyPoints(pdf: jsPDF, keyPoints: any[], yPosition: number): number {
    // Section title with more space
    pdf.setFontSize(14);
    pdf.setFont('helvetica', 'bold');
    pdf.setTextColor(51, 51, 51);
    pdf.text('Key Insights', this.margin, yPosition);
    
    yPosition += 8;
    
    keyPoints.forEach((point, index) => {
      // Bullet point
      pdf.setFillColor(245, 101, 12);
      pdf.circle(this.margin + 4, yPosition + 2, 2, 'F');
      
      // Point title
      pdf.setFontSize(12);
      pdf.setFont('helvetica', 'bold');
      pdf.setTextColor(51, 51, 51);
      pdf.text(point.title, this.margin + 12, yPosition + 1 );
      
      yPosition += 6;
      
      // Point description with proper spacing
      pdf.setFontSize(11);
      pdf.setFont('helvetica', 'normal');
      pdf.setTextColor(102, 102, 102);
      const descLines = pdf.splitTextToSize(point.description, this.contentWidth - 15);
      pdf.text(descLines, this.margin + 12, yPosition);
      
      yPosition += (descLines.length * 5) + 2; // Better line spacing
    });
    
    return yPosition;
  }

  private addFooter(pdf: jsPDF): void {
    const pageCount = pdf.getNumberOfPages();
    
    for (let i = 1; i <= pageCount; i++) {
      pdf.setPage(i);
      
      // Footer line
      pdf.setDrawColor(229, 231, 235);
      pdf.line(this.margin, this.pageHeight - 20, this.pageWidth - this.margin, this.pageHeight - 20);
      
      // Footer text
      pdf.setFontSize(8);
      pdf.setFont('helvetica', 'normal');
      pdf.setTextColor(156, 163, 175);
      pdf.text('RIASEC Career Interest Assessment Report', this.margin, this.pageHeight - 12);
      pdf.text(`Page ${i} of ${pageCount}`, this.pageWidth - this.margin - 25, this.pageHeight - 12);
    }
  }

  private hexToRgb(hex: string): { r: number; g: number; b: number } | null {
    const result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
    return result ? {
      r: parseInt(result[1], 16),
      g: parseInt(result[2], 16),
      b: parseInt(result[3], 16)
    } : null;
  }
  
  async generatePdfBlob(data: RiasecData): Promise<Blob> {
    const pdf = new jsPDF('p', 'mm', 'a4');
    let yPosition = this.margin;

    // Same rendering logic as generateRiasecPdf
    yPosition = this.addHeader(pdf, yPosition,data.dominantProfile.key);
    yPosition = this.addDominantProfile(pdf, data.dominantProfile, yPosition);
    yPosition = this.addTop3Scores(pdf, data.top3Scores, yPosition);
    yPosition = this.addDetailedScores(pdf, data.scores, yPosition);
    this.addKeyPoints(pdf, data.keyPoints, yPosition);
    this.addFooter(pdf);

    // Create Blob
    const pdfBlob = pdf.output('blob');
    return pdfBlob;
  }

}