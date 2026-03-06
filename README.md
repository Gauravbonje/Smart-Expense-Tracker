#  Smart Expense Tracker (
**Developed by Gaurav Yadav | B.Tech Computer Science & Engineering Student**

##  Learning Purpose
This project was developed as a hands-on learning initiative to master modern Android development. Through this project, I explored the transition from temporary data handling to persistent storage and implemented basic AI-driven logic to simplify user experience.

## 🛠 Tech Stack
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Modern Declarative UI)
- **Architecture:** MVVM (Model-View-ViewModel)
- **Database:** Room Database (SQLite abstraction for persistent storage)
- **Local Storage:** SharedPreferences (for persistent Budget settings)
- **Concurrency:** Kotlin Coroutines & Flow (for real-time data updates)

##  Key Features Applied
- **AI Smart Input:** Uses Regex-based parsing to automatically detect categories like Travel (Uber/Ola), Online Delivery (Swiggy/Zomato), and Transactions (Human names) from a single text string.
- **Data Persistence:** Integrated a Room Database Singleton to ensure expenses are saved on the disk and not lost after app restarts.
- **Dynamic Filtering:** Implemented Calendar-based logic to auto-refresh the home screen view for the current month while maintaining historical records.
- **Budget Monitoring:** Real-time tracking of Daily and Monthly limits with visual progress bars.

## Future Scope (Extra Features to Add)
- **OCR Integration:** Using Google ML Kit to scan physical receipts and auto-fill expenses.
- **Data Export:** Exporting monthly spending reports to PDF or Excel formats.
- **Interactive Charts:** Detailed spending breakdowns using Bar and Pie charts for deeper financial analysis.
- **Dark Mode Support:** Implementing dynamic theming for better accessibility.

##  Download APK
You can download and test the latest build of the app here:
[Download app-debug.apk](app-debug.apk) 
*(Note: Upload your app-debug.apk file to the root of your GitHub repository for this link to work)*
