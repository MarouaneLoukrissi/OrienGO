import { Component } from '@angular/core';

interface Student {
  id: string;
  name: string;
  email: string;
  connectedDate: string;
  profileImage: string;
}

@Component({
  selector: 'app-networks',
  templateUrl: './network-coach.component.html',
  styleUrl: './network-coach.component.css'
})
export class NetworkCoachComponent {
  activeTab: 'network' | 'pendings' = 'network';
  sortBy = 'recent';
  searchQuery = '';
  newStudentCode = '';

  // Mock data - replace with API calls
  students: Student[] = [
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

  pendingStudents: Student[] = [
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

  getFilteredStudents(): Student[] {
    const currentStudents = this.activeTab === 'network' ? this.students : this.pendingStudents;

    let filtered = currentStudents.filter(student =>
      student.name.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
      student.email.toLowerCase().includes(this.searchQuery.toLowerCase())
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

  sendMessage(student: Student): void {
    console.log('Sending message to:', student);
    // TODO: Implement message functionality
  }

  removeStudent(student: Student): void {
    console.log('Removing student:', student);
    // TODO: Implement remove functionality
    if (this.activeTab === 'network') {
      this.students = this.students.filter(t => t.id !== student.id);
    } else {
      this.pendingStudents = this.pendingStudents.filter(t => t.id !== student.id);
    }
  }

  sendRequest(): void {
    if (!this.newStudentCode.trim()) return;

    console.log('Sending request to:', this.newStudentCode);
    // TODO: Implement add coach functionality
    this.newStudentCode = '';
  }
}
