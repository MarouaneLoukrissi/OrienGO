import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-radar-chart',
  templateUrl: './radar-chart.component.html',
  styleUrls: ['./radar-chart.component.css'],
})
export class RadarChartComponent implements OnInit {
  @Input() data: { [key: string]: number } = {};

  // Geometry based on SVG coordinate system
  size = 300;   // Used for math (viewBox coordinates)
  center = 150;
  radius = 100;
  levels = 5;

  hoverCat: string | null = null;
  categories = ['R', 'I', 'A', 'S', 'E', 'C'];
  fullNames: { [key: string]: string } = {
    R: 'Realistic',
    I: 'Investigative',
    A: 'Artistic',
    S: 'Social',
    E: 'Enterprising',
    C: 'Conventional',
  };
  colors: { [key: string]: string } = {
    R: '#fb923c',
    I: '#fb923c',
    A: '#fb923c',
    S: '#fb923c',
    E: '#fb923c',
    C: '#fb923c',
  };

  angleStep = (2 * Math.PI) / this.categories.length;

  tooltip = {
    x: 0,
    y: 0,
    label: '',
    value: 0,
    visible: false
  };

  showTooltip(x: number, y: number, label: string, value: number) {
    const marginX = 10;  // shift tooltip a bit right from cursor
    const marginY = 20;  // shift tooltip a bit above cursor

    this.tooltip = {
      x: x-marginX,
      y: y-marginY,
      label: this.fullNames[label] || label,
      value,
      visible: true,
    };
  }
  onMouseEnter(event: MouseEvent, point: { x: number; y: number; category: string; value: number }) {
    const container = (event.target as HTMLElement).closest('.relative') as HTMLElement;
    const containerRect = container.getBoundingClientRect();

    const marginX = 10;
    const marginY = 10;

    this.hoverCat = point.category;
    this.tooltip = {
      x: event.clientX - containerRect.left + marginX,
      y: event.clientY - containerRect.top + marginY,
      label: this.fullNames[point.category] || point.category,
      value: point.value,
      visible: true
    };
  }

  onMouseLeave() {
    this.hoverCat = null;
    this.tooltip.visible = false;
  }

  ngOnInit() {
    this.buildChart();
  }
  

  gridLevels: string[] = [];
  axisLines: Array<{ x1: number; y1: number; x2: number; y2: number }> = [];
  labels: Array<{ x: number; y: number; category: string }> = [];
  dataPath = '';
  dataPoints: Array<{ x: number; y: number; category: string; value: number; color: string }> = [];

  private toPoint(angle: number, dist: number) {
    return {
      x: this.center + dist * Math.cos(angle - Math.PI / 2),
      y: this.center + dist * Math.sin(angle - Math.PI / 2),
    };
  }

  private buildChart() {
    this.gridLevels = [];
    this.axisLines = [];
    this.labels = [];
    this.dataPoints = [];

    for (let lvl = 1; lvl <= this.levels; lvl++) {
      const r = (this.radius * lvl) / this.levels;
      const pts = this.categories.map((_, i) => this.toPoint(i * this.angleStep, r));
      this.gridLevels.push(
        pts.map((pt, idx) => `${idx === 0 ? 'M' : 'L'} ${pt.x} ${pt.y}`).join(' ') + ' Z'
      );
    }

    this.axisLines = this.categories.map((_, i) => {
      const end = this.toPoint(i * this.angleStep, this.radius);
      return { x1: this.center, y1: this.center, x2: end.x, y2: end.y };
    });

    this.labels = this.categories.map((cat, i) => {
      const lbl = this.toPoint(i * this.angleStep, this.radius + 20);
      return { x: lbl.x, y: lbl.y, category: cat };
    });

    this.dataPoints = this.categories.map((cat, i) => {
      const val = Math.min(this.data[cat] || 0, 100);
      const dist = (this.radius * val) / 100;
      const pt = this.toPoint(i * this.angleStep, dist);
      return { ...pt, category: cat, value: val, color: this.colors[cat] };
    });

    this.dataPath = this.dataPoints.map((p, idx) => 
      `${idx === 0 ? 'M' : 'L'} ${p.x} ${p.y}`).join(' ') + ' Z';
  }
}
