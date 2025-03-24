# Little Lemon Food Ordering App

**A native Android application for seamless food ordering at Little Lemon Restaurant.**

---

## 📱 About the Project

The **Little Lemon Food Ordering App** is a user-friendly, intuitive mobile application designed to simplify the dining experience at Little Lemon Restaurant. Customers can browse the menu, customize orders, and complete secure transactions with just a few taps. Built as part of a Coursera's Meta Android developer Capstone Project course, this app demonstrates modern Android development principles and technologies.

## Key Features and Functionalities

### Interactive Menu Browsing

*   **Comprehensive Menu Display:** The app fetches and displays a complete list of menu items, including their names, descriptions, prices, and images.
*   **Lazy Loading:** `LazyColumn` and `LazyRow` are used to efficiently render menu items and categories, optimizing performance for large datasets.
*   **Image handling:** Glide library is used to load images asynchronously from URL, manage caching, and handle errors.
*   **Item Details:** Users can tap on a menu item to view additional details.
*   **Data persistence:** The menu items are saved locally using Room, for an offline experience.

### Dynamic Filtering

*   **Category-Based Filtering:** The menu items can be filtered by categories (e.g., Starters, Mains, Desserts, Drinks, Specials).
*   **Real-time Updates:** Filtering is applied in real-time as the user selects or deselects categories.
*   **Dynamic categories:** Category chips will be populated dynamically based on the menu items.

### Advanced Search

*   **Search Bar:** A prominent search bar allows users to quickly find menu items by name or description.
*   **Intelligent Search:** The search functionality is not case-sensitive and will search both the description and the title of each item.
*   **Live Search:** As the user types, the menu is dynamically updated to show matching items.

### UI/UX Design

*   **Clean and Intuitive:** The app follows Material Design guidelines, offering a clear and easy-to-navigate interface.
*   **Responsive Layout:** The layout adapts well to different screen sizes and orientations, making it suitable for a wide range of devices.
*   **Theming:** The app is themed with a green primary color and a yellow secondary color, matching the Little Lemon brand.
*   **Placeholders:** Placeholders and error images are implemented using Glide, to provide a better user experience.

### Data Persistence and Preferences

*   **Local Database:** Room is used for local persistence of menu data, enabling the app to work offline.
*   **User Preferences:** SharedPreferences are used to store small pieces of data, like the user's selected preferences.

### Networking

*   **Ktor Client:** The Ktor HTTP Client is used for making network requests to fetch menu data from the remote API.
*   **JSON Handling:** Ktor is also utilized for handling JSON data during the network requests.
*   **Asynchronous requests:** Networking operations are made asynchronously to avoid blocking the main thread.

### Navigation

* **Compose Navigation:** Navigation Compose is used to implement a fluid navigation flow between the main sections of the app, such as the home screen and the user profile.
* **Navigation controller:** A navigation controller is used to control the navigation.
* **Navigation Graph:** A navigation graph is used to define the app navigation structure.

## Technical Concepts and Android/Kotlin Features

This project demonstrates proficiency in modern Android development practices, showcasing the following concepts:

### Jetpack Compose
*   **Declarative UI:** The entire UI is built using Jetpack Compose, showcasing a modern, declarative approach to UI development.
*   **State Management:** `rememberSaveable`, and mutable states are used for managing and persisting UI state across configuration changes.
*   **Layouts:** `LazyColumn`, `LazyRow`, `Column`, and `Row` are used to create dynamic and efficient layouts.
*   **Modifiers:** The use of `Modifier` for styling, layout adjustments, and click events is demonstrated extensively.
*   **Material Design 3:** Material Theme is used to achieve a consistent and modern UI, while using all the components included like `Text`, `OutlinedTextField`, `Card`, `Button`, etc.
*   **Theming:** The app is themed with a green primary color and a yellow secondary color.

### Kotlin Features

*   **Data Classes:** `MenuItemEntity` is implemented as a Kotlin data class, for holding menu items data.
*   **Extensions:** The filtering logic can easily be extended.
*   **Functional Programming:** Filtering and transformation of menu items using `filter`, `map`, and `distinct` are used.
*   **Lambdas and Higher-Order Functions:** Used extensively with Compose composables and for state change callbacks.
*   **Collections:** List, Maps, and other collections are used for managing and transforming the menu items.
*   **Coroutines:** Coroutines are used for asynchronous network operations.

### Android Architecture and Libraries

*   **Glide:** Used for image loading and caching from URLs.
*   **Ktor:** Used as an HTTP client for making network requests.
*   **Room:** Utilized for local database management and persistence.
*   **SharedPreferences:** Employed for saving simple key-value data.
*   **Navigation Compose:** Used for implementing the navigation between the different screens.
*   **Compose Material icons:** Used for the search bar icon.
*   **Dependency Injection (Future):** While not explicitly shown, the project is well-structured for future dependency injection implementation.
*   **Clean Code Practices:** The code is well-organized, readable, and follows common Kotlin and Android coding conventions.
*   **AndroidX libraries:** All the used libraries are androidx.

### Other Concepts

*   **UI Preview:** Compose preview functionalities are used for rapid development and visual testing.
*   **Resource Handling:** The application uses resource files for images and strings, properly managing them.
*   **String resources:** All string literals are defined in a `strings.xml` file.
*   **Accessibility:** The code includes content descriptions for images, enhancing accessibility.
*   **Error handling:** Glide provide error and loading handling, with placeholder and error images.

