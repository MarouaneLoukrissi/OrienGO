import {Component, ElementRef, Input, QueryList, ViewChildren} from '@angular/core';

@Component({
  selector: 'app-bar-chart',
  templateUrl: './bar-chart.component.html',
  styleUrls: ['./bar-chart.component.css'],
})
export class BarChartComponent {
  @ViewChildren('barElements', { read: ElementRef })
  barElements!: QueryList<ElementRef>;

  @Input() data: { [key: string]: number } = {};

  categories = ['R', 'I', 'A', 'S', 'E', 'C'];
  fullNames: { [key: string]: string } = {
    R: 'Realistic',
    I: 'Investigative',
    A: 'Artistic',
    S: 'Social',
    E: 'Enterprising',
    C: 'Conventional',
  };

  colors = {
    R: '#3b82f6',
    I: '#8b5cf6',
    A: '#ec4899',
    S: '#10b981',
    E: '#ef4444',
    C: '#6366f1',
  };

  maxValue = 100;
  bars: { category: string; value: number; height: number; color: string }[] = [];

  tooltip = {
    visible: false,
    x: 0,
    y: 0,
    text: '',
    color: ''
  };
  ngOnInit() {
    this.generateBars();
  }

  private generateBars() {
    const chartHeightPx = 295.5;
    this.maxValue = 100;

    this.bars = this.categories.map((category) => {
      let value = this.data[category] || 0;
      if (value > this.maxValue) value = this.maxValue;

      const height = (value / this.maxValue) * chartHeightPx;

      return {
        category,
        value,
        height,
        color: this.colors[category as keyof typeof this.colors],
      };
    });
  }

  barShouldBeRounded(bar: {
    category: string;
    value: number;
    height: number;
    color: string;
  }): boolean {
    const barIndex = this.bars.indexOf(bar);
    const el = this.barElements?.get(barIndex)?.nativeElement;
    return el ? el.offsetWidth < 5 : false;
  }

  showTooltip(bar: any, event: MouseEvent) {
    const container = (event.currentTarget as HTMLElement).closest('.relative') as HTMLElement;
    const rect = container.getBoundingClientRect();

    this.tooltip.visible = true;
    this.tooltip.text = `${this.fullNames[bar.category]}: ${bar.value}%`;
    this.tooltip.color = bar.color;
    this.tooltip.x = event.clientX - rect.left + 10;
    this.tooltip.y = event.clientY - rect.top + 10;
  }

  moveTooltip(event: MouseEvent) {
    const container = (event.currentTarget as HTMLElement).closest('.relative') as HTMLElement;
    const rect = container.getBoundingClientRect();

    this.tooltip.x = event.clientX - rect.left + 10;
    this.tooltip.y = event.clientY - rect.top + 10;
  }

  hideTooltip() {
    this.tooltip.visible = false;
  }

}
