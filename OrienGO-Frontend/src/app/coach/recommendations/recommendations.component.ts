import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-recommendations',
  templateUrl: './recommendations.component.html',
})
export class RecommendationsComponent implements OnInit {
  eleveId: string | null = null;

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
    this.eleveId = this.route.snapshot.paramMap.get('id');
    console.log('ID de l’élève :', this.eleveId);
    // Plus tard : appeler l’API pour charger les recommandations dynamiques
  }

  recommendations = [
    {
      title: 'PSYCHOLOGIST',
      match: '66%',
      description: 'PSYCHOLOGICAL_SUPPORT_DESC',
      education: 'MASTER_PSYCHOLOGY',
      salary: '30 000 - 50 000$/YEAR',
      jobMarket: 'HIGH',
      tags: ['EMPATHY', 'LISTENING', 'THERAPY']
    },
    {
      title: 'TEACHER',
      match: '68%',
      description: 'KNOWLEDGE_GUIDANCE_DESC',
      education: 'MASTER_MEEF',
      salary: '25 000 - 45 000$/YEAR',
      jobMarket: 'STABLE',
      tags: ['PEDAGOGY', 'COMMUNICATION', 'CREATIVITY']
    }
  ];
}
