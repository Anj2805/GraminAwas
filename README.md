# GraminAwas - Rural Housing Management System

GraminAwas is an Android application designed to streamline and manage rural housing construction projects. The application facilitates efficient project management, fund tracking, and construction progress monitoring for all stakeholders involved in rural housing initiatives.

## Features

### Admin Dashboard
- Profile management
- Quick stats overview (beneficiaries, projects, funds)
- Beneficiary management
- Project management
- Fund allocation and tracking
- Reports generation
- Project map visualization
- Progress tracking
- Deadline monitoring

### Contractor Dashboard
- Profile management
- Project assignment tracking
- Progress updates
- Fund tracking
- Deadline management
- Reports & analytics
- Activity tracking
- Quality inspection reports

### Beneficiary Dashboard
- Project status tracking
- Fund management
- Deadline monitoring
- Feedback system
- Personal information management
- Project details access
- Construction updates
- Communication with contractors

## Technical Stack

### Core Technologies
- **Language**: Kotlin
- **Minimum SDK**: 24
- **Target SDK**: 34
- **Architecture**: MVVM (Model-View-ViewModel)

### Key Components
- **Android Architecture Components**
  - ViewModel
  - LiveData
  - Room Database
  - Navigation Component
  - Data Binding
  - View Binding

- **UI Components**
  - Material Design
  - RecyclerView
  - CardView
  - ConstraintLayout
  - Navigation Drawer
  - SwipeRefreshLayout

- **Image Handling**
  - Glide
  - PhotoView
  - Android Image Cropper

- **Location Services**
  - Google Maps
  - Google Play Services Location

- **Charts and Visualization**
  - MPAndroidChart

- **Asynchronous Programming**
  - Kotlin Coroutines
  - Flow API

## Database Structure

The application uses Room Database with the following main entities:

- **Beneficiary**
  - Personal information
  - Contact details
  - Aadhar number
  - Family information
  - Fund allocation status

- **Project**
  - Project details
  - Location
  - Timeline
  - Budget
  - Progress tracking

- **ProjectDeadline**
  - Milestone tracking
  - Due dates
  - Status monitoring

- **ProjectUpdate**
  - Progress updates
  - Image documentation
  - Status reports

## Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- JDK 11 or later
- Android SDK 24 or later
- Google Play Services

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/GraminAwas.git
   ```

2. Open the project in Android Studio

3. Sync the project with Gradle files

4. Build and run the application

### Configuration
1. Add your Google Maps API key in `local.properties`:
   ```
   MAPS_API_KEY=your_api_key_here
   ```

2. Configure the database in `AppDatabase.kt`

## Project Structure
```
app/
├── data/
│   ├── dao/
│   ├── entities/
│   └── repositories/
├── ui/
│   ├── admin/
│   ├── contractor/
│   ├── beneficiary/
│   └── common/
├── utils/
└── viewmodel/
```

## Contributing
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

## Acknowledgments
- Material Design Components
- Android Architecture Components
- Google Maps Platform
- MPAndroidChart
- Glide

## Contact
Your Name - your.email@example.com
Project Link: [https://github.com/yourusername/GraminAwas](https://github.com/yourusername/GraminAwas) 