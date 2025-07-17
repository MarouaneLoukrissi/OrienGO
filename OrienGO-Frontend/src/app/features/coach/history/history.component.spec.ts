import { Component } from '@angular/core';

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
})
export class HistoryComponent {
  filters = ['All', 'Full Test', 'Express Test', 'Last 3 Months'];
  activeFilter = 'All';

  tests = [
    {
      title: 'RIASEC Full',
      date: '15/01/2024',
      type: 'Full Test',
      percentages: [75, 85, 45, 60, 70, 55],
      score: 85,
    },
    {
      title: 'RIASEC Express',
      date: '10/12/2023',
      type: 'Express Test',
      percentages: [70, 80, 50, 65, 70, 55],
      score: 80,
    },
    {
      title: 'RIASEC Full',
      date: '03/05/2024',
      type: 'Full Test',
      percentages: [80, 75, 60, 50, 45, 40],
      score: 83,
    }
  ];

  setActiveFilter(filter: string) {
    this.activeFilter = filter;
  }

  getFilteredTests() {
    const now = new Date();
    if (this.activeFilter === 'All') {
      return this.tests;
    } else if (this.activeFilter === 'Last 3 Months') {
      return this.tests.filter(test => {
        const testDate = new Date(test.date.split('/').reverse().join('-'));
        const diffTime = Math.abs(now.getTime() - testDate.getTime());
        const diffDays = diffTime / (1000 * 60 * 60 * 24);
        return diffDays <= 90;
      });
    } else {
      return this.tests.filter(test => test.type === this.activeFilter);
    }
  }
}
