import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-test',
  templateUrl: './test.component.html',
  styleUrl: './test.component.css'
})
export class TestComponent {
  @Output() testStarted = new EventEmitter<number>();
  startTest(questionCount: number) {
    this.testStarted.emit(questionCount);
  }
}
