import { Component } from '@angular/core';

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrl: './history.component.css'
})
export class HistoryComponent {
 filters: string[] = [
  'TEST_HISTORY.FILTERS.ALL',
  'TEST_HISTORY.FILTERS.LAST_3_MONTHS',
  'TEST_HISTORY.FILTERS.FULL',
  'TEST_HISTORY.FILTERS.EXPRESS'
];

  activeFilter: string = 'All tests';

   // La liste des tests à afficher (tu peux remplacer par des données dynamiques plus tard)
  tests = [
    {
      title: 'RIASEC Full',
      type: 'full',
      date: '2024-01-15',
      score: 85,
      profile: 'Investigator', 
      percentages: [75, 85, 45, 60, 70, 55]
    },
    {
      title: 'RIASEC Express',
      type: 'express',
      date: '2023-12-10',
      score: 80,
      profile: 'Investigator', 
      percentages: [70, 80, 50, 65, 70, 55]
    }
  ];

  //  Cette fonction filtre les tests en fonction du filtre sélectionné
  getFilteredTests() {
    const now = new Date();
    const threeMonthsAgo = new Date();
    threeMonthsAgo.setMonth(now.getMonth() - 3);

    switch (this.activeFilter) {
      case 'Full tests':
        return this.tests.filter(t => t.type === 'full');
      case 'Express tests':
        return this.tests.filter(t => t.type === 'express');
      case 'Last 3 months':
        return this.tests.filter(t => new Date(t.date) >= threeMonthsAgo);
      default:
        return this.tests;
    }
  }
   //Méthode appelée quand l'utilisateur clique sur un bouton
  setActiveFilter(filter: string) {
    this.activeFilter = filter;
  }
}
