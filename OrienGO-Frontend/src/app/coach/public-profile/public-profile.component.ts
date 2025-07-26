import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-public-profile',
  templateUrl: './public-profile.component.html',
  styleUrl: './public-profile.component.css'
})
export class PublicProfileComponent implements OnInit {
  coachId!: string;

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.coachId = this.route.snapshot.paramMap.get('id')!;
    console.log("Coach ID:", this.coachId);
  }
}