import { Component, AfterViewInit, ElementRef, ViewChildren, QueryList } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements AfterViewInit {
  @ViewChildren('fadeBlock', { read: ElementRef }) blocks!: QueryList<ElementRef>;

  ngAfterViewInit(): void {
    const observer = new IntersectionObserver(
      entries => {
        entries.forEach(entry => {
          if (entry.isIntersecting) {
            entry.target.classList.add('animate-fade-in');
            observer.unobserve(entry.target); // Évite l'animation à répétition
          }
        });
      },
      {
        threshold: 0.5,
        rootMargin: '0px 0px -100px 0px' // ⚠️ important pour éviter déclenchement prématuré
      }
    );

    this.blocks.forEach(block => {
      observer.observe(block.nativeElement);
    });
  }
}
