import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {
  isSuperAdmin = false;

  ngOnInit(): void {
    this.isSuperAdmin = localStorage.getItem('role') === 'SUPER_ADMIN';
  }
}

