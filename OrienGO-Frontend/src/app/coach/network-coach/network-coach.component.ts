import { Component } from '@angular/core';

@Component({
  selector: 'app-network-coach',
  templateUrl: './network-coach.component.html',
  styleUrls: ['./network-coach.component.css']
})
export class NetworkCoachComponent {
  currentTab: 'MY_NETWORK' | 'MY_PENDINGS' = 'MY_NETWORK';

  acceptedStudents = [
    { name: 'Sanaa El Idrissi', email: 'sanaa@example.com', avatar: 'assets/avatar1.png', connectedOn: '02/07/2025' },
    { name: 'Yassmine Laaziz', email: 'yassmine@example.com', avatar: 'assets/avatar2.png', connectedOn: '01/07/2025' }
  ];

  pendingStudents = [
    { name: 'Samira Ait Haddou', email: 'samira@example.com', avatar: 'assets/avatar3.png' },
    { name: 'Myriam Loukili', email: 'myriam@example.com', avatar: 'assets/avatar4.png' }
  ];
}
