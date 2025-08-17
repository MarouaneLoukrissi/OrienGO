import { Component } from '@angular/core';

interface Coach {
  id: string;
  name: string;
  email: string;
  connectedDate: string;
  profileImage: string;
}

@Component({
  selector: 'app-networks',
  templateUrl: './networks.component.html',
  styleUrl: './networks.component.css'
})
export class NetworksComponent {
  activeTab: 'network' | 'pendings' = 'network';
  sortBy = 'recent';
  searchQuery = '';
  newCoachCode = '';

  // Mock data - replace with API calls
  coachs: Coach[] = [
    {
      id: '1',
      name: 'Marouane Loukrissi',
      email: 'm.loukrissi4464@taalim.ma',
      connectedDate: '9 july 2025',
      profileImage: 'https://images.pexels.com/photos/2379004/pexels-photo-2379004.jpeg?auto=compress&cs=tinysrgb&w=150&h=150&dpr=1'
    },
    {
      id: '2',
      name: 'Marouane Loukrissi',
      email: 'm.loukrissi4464@taalim.ma',
      connectedDate: '9 july 2025',
      profileImage: 'https://images.pexels.com/photos/2379004/pexels-photo-2379004.jpeg?auto=compress&cs=tinysrgb&w=150&h=150&dpr=1'
    }
  ];

  pendingCoachs: Coach[] = [
    {
      id: '3',
      name: 'Marouane Loukrissi',
      email: 'm.loukrissi4464@taalim.ma',
      connectedDate: '9 july 2025',
      profileImage: 'https://images.pexels.com/photos/2379004/pexels-photo-2379004.jpeg?auto=compress&cs=tinysrgb&w=150&h=150&dpr=1'
    },
    {
      id: '4',
      name: 'Marouane Loukrissi',
      email: 'm.loukrissi4464@taalim.ma',
      connectedDate: '9 july 2025',
      profileImage: 'https://images.pexels.com/photos/2379004/pexels-photo-2379004.jpeg?auto=compress&cs=tinysrgb&w=150&h=150&dpr=1'
    }
  ];

  getFilteredCoachs(): Coach[] {
    const currentCoachs = this.activeTab === 'network' ? this.coachs : this.pendingCoachs;
    
    let filtered = currentCoachs.filter(coach =>
      coach.name.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
      coach.email.toLowerCase().includes(this.searchQuery.toLowerCase())
    );

    // Sort implementation
    switch (this.sortBy) {
      case 'name':
        filtered.sort((a, b) => a.name.localeCompare(b.name));
        break;
      case 'oldest':
        filtered.sort((a, b) => new Date(a.connectedDate).getTime() - new Date(b.connectedDate).getTime());
        break;
      case 'recent':
      default:
        filtered.sort((a, b) => new Date(b.connectedDate).getTime() - new Date(a.connectedDate).getTime());
        break;
    }

    return filtered;
  }

  sendMessage(coach: Coach): void {
    console.log('Sending message to:', coach);
    // TODO: Implement message functionality
  }

  removeCoach(coach: Coach): void {
    console.log('Removing coach:', coach);
    // TODO: Implement remove functionality
    if (this.activeTab === 'network') {
      this.coachs = this.coachs.filter(t => t.id !== coach.id);
    } else {
      this.pendingCoachs = this.pendingCoachs.filter(t => t.id !== coach.id);
    }
  }

  sendRequest(): void {
    if (!this.newCoachCode.trim()) return;
    
    console.log('Sending request to:', this.newCoachCode);
    // TODO: Implement add coach functionality
    this.newCoachCode = '';
  }
}
