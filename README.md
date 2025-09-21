# NutriTrack

Welcome to NutriTrack, a mobile app designed to help users reflect on their eating habits and receive personalised insights into their dietary quality.

With NutriTrack, users can log in with their profile details, complete a short questionnaire, and instantly view their Food Quality Score with a full category breakdown. Beyond individual tracking, the app also includes a clinician/admin mode, enabling deeper nutritional data insights for health professionals.

📊 Track your dietary quality  
🥗 Explore personalised insights and breakdowns  
🔑 Clinician view with data-driven analysis  
📱 Built with Kotlin, Jetpack Compose, Room DB, and MVVM architecture  

## Core Features:
- Welcome Screen: Introduction and disclaimer.
- Login & Registration: Users authenticate with their User ID and Phone Number (loaded from the dataset). First-time users set up their account with a password.
- Food Intake Questionnaire: Capture meal timings, dietary categories, and preferences, with data stored locally.
- Home Screen: Displays the user’s Food Quality Score (HEIFA) retrieved from the dataset.
- Insights Screen: Detailed breakdown of scores across food categories (vegetables, fruits, grains, meats, dairy, etc.).
- NutriCoach Screen:
  - Pulls fruit data from the FruityVice API.
  - Integrates GenAI to generate motivational dietary tips.
  - Stores all AI-generated tips in a local database for future reference.
- Clinician/Admin Mode: Accessible via the settings page using the special key: "dollar-entry-apples" (without quote marks)
  - Provides access to average HEIFA scores by gender, plus GenAI-powered pattern detection in nutrition data.

## User Details:
When registering for the first time, users must provide a valid User ID and Phone Number pair from the dataset.

| UserID | Phone Number |
|--------|--------------|
| 1      | 61436567331  |
| 2      | 61436567332  |
| 4      | 61436567330  |
| 5      | 61436567333  |
| 6      | 61436567334  |
| 17     | 61436567335  |
| 24     | 61436567336  |
| 26     | 61436567337  |
| 28     | 61433327331  |

## Steps to run:
- Download the project ZIP and extract it
- Add your API key in local.properties in the following format:
  apiKey="your_api_key"  
- Open the project in Android Studio.
- Run the app on an emulator or physical device.
