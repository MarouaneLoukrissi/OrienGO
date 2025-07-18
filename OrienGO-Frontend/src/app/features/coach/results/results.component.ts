import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html'
})
export class ResultsComponent implements OnInit {
  eleveId: string = '';
  eleveResultat: any;

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
    this.eleveId = this.route.snapshot.paramMap.get('id') || '';

    // Simule les données d’un élève (à remplacer plus tard par le vrai backend)
    this.eleveResultat = {
      nom: 'Alex Johnson',
      dominantProfile: 'Realistic',
      score: 44,
      scores: {
        realistic: 44,
        investigative: 30,
        artistic: 40,
        social: 35,
        enterprising: 50,
        conventional: 20
      }
    };
  }
  scores = [
  { label: 'results.realistic', value: 44 },
  { label: 'results.investigative', value: 41 },
  { label: 'results.artistic', value: 39 },
  { label: 'results.social', value: 33 },
  { label: 'results.enterprising', value: 27 },
  { label: 'results.conventional', value: 22 }
];

}
