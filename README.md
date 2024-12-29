## BangkitEvent, personal project implementing retrofit (fetch API), content scroll, bottom navigation, search function, recycleview with onclick listener, and error handling

# DicodingEvent

**DicodingEvent** is an application designed to display upcoming and finished events from Dicoding. This app provides features such as search functionality, a home page with a recycler view slider, notifications, dark mode, and localization. This app implementing retrofit (fetching API), content scroll, bottom Navbar, search functionality, recycleview with onclick, error handling, dark mode support, job notification handler, and localization.

## Features

- **Upcoming Events**: Displays a list of upcoming events fetched through an API.
- **Finished Events**: Shows a list of completed events.
- **Detailed Events**: Shows detail of event.
- **Favorite Events**: Mark event as favorite.
- **Search Functionality**: Allows users to search for events based on specific keywords.
- **Home Slider**: The home page features a recycler view slider to highlight key events.
- **Dark Mode**: Supports a dark mode for a more comfortable user experience.
- **Localization**: Offers multi-language support to cater to users' regional preferences.
- **Notification**: Sends notifications related to specific events.

## Screenshots

<p align="center">
  <img src="https://github.com/user-attachments/assets/ec69816a-307b-43dc-95f0-5db5e4f9ba11" alt="Screenshot 1" width="200"/>
  <img src="https://github.com/user-attachments/assets/bb830364-1043-403b-abdf-5f24547fde9b" alt="Screenshot 2" width="200"/>
  <img src="https://github.com/user-attachments/assets/e7e0e711-0287-48cd-9a17-66ac9e2fcdbc" alt="Screenshot 3" width="200"/>
</p> <br>
<p align="center">
  <img src="https://github.com/user-attachments/assets/a6bbc8fe-9212-47b7-9cf5-b32d1d235859" alt="Screenshot 4" width="200"/>
  <img src="https://github.com/user-attachments/assets/230f93c7-8c27-4f68-853b-4bba4c5703ce" alt="Screenshot 5" width="200"/>
</p>
## Getting Started

### Prerequisites

- Android Studio (latest version recommended)
- Minimum SDK: 21
- Compile SDK: 33

### Installation

1. Clone this repository:
    ```bash
    git clone https://github.com/rifaset123/DicodingEvent.git
    ```

2. Open the project in Android Studio.

3. Sync the project to download dependencies:
    - Go to **File > Sync Project with Gradle Files**.

4. Build and run the app on an emulator or physical device.

## Root Folder Structure

```plaintext
📂 local
┣ 📂 entity
┃ ┗ 📄 EventEntity
┣ 📂 repository
┃ ┣ 📄 EventRepo
┃ ┗ 📄 Result
┣ 📂 room
┃ ┣ 📄 EventDao
┃ ┗ 📄 EventDatabase
┣ 📂 response
┃ ┣ 📄 EventIDResponse.kt
┃ ┗ 📄 EventResponse.kt
┣ 📂 retrofit
┃ ┣ 📄 ApiConfig
┃ ┗ 📄 ApiService

📂 di
┣ 📄 Injection

📂 ui
┣ 📂 detail
┣ 📂 favorite
┃ ┣ 📄 FavoriteAdapter
┃ ┣ 📄 FavoriteFragment
┃ ┗ 📄 FavoriteViewModel
┣ 📂 finished
┃ ┣ 📄 FinishedFragment
┃ ┗ 📄 FinishedViewModel
┣ 📂 home
┃ ┣ 📄 HomeFragment
┃ ┗ 📄 HomeViewModel
┣ 📂 settings
┣ 📂 upcoming
┃ ┗ 📄 ViewModelFactory

📂 utils
┣ 📄 AppExecutors
┣ 📄 OnEventClickListener
┣ 📄 EventAdapter
┣ 📄 MainActivity
```

## Dependencies

```kotlin
dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.preference)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Error handling
    implementation(libs.logging.interceptor)

    // Image loading
    implementation(libs.github.glide)

    // Search bar
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    // Room for database
    implementation(libs.androidx.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.androidx.room.ktx)

    // DataStore
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // WorkManager
    implementation(libs.androidx.work.runtime)
    implementation(libs.android.async.http)

    // Moshi for JSON parsing
    implementation(libs.moshi.kotlin)
    ksp(libs.moshi.kotlin.codegen)
}


