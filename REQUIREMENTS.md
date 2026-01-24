# Health Connect Visualizer App: Product Requirements

## 1. Vision

To create a powerful and insightful health and fitness data visualizer that seamlessly integrates with Health Connect. The app will empower users to understand their health data through beautiful, informative, and actionable visualizations.

## 2. Target Audience

*   Health and fitness enthusiasts who want to gain deeper insights from their data.
*   Individuals tracking specific health conditions (e.g., diabetes, hypertension).
*   Users who want a single, unified view of their health data from multiple apps.

## 3. Core Features

### 3.1. Dashboard

*   **Purpose:** Provide a quick, at-a-glance overview of the user's most important health metrics.
*   **Requirements:**
    *   Display a customizable grid of widgets.
    *   Each widget represents a specific Health Connect data type (e.g., Steps, Heart Rate, Sleep).
    *   Widgets should show the latest data point and a trend line for the last 7 days.
    *   Users can add, remove, and rearrange widgets.

### 3.2. Category-Specific Visualizations

#### 3.2.1. Vitals

*   **Blood Glucose:**
    *   **Graph:** Line graph showing blood glucose levels over time (day, week, month views).
    *   **Key Metrics:**
        *   **Time in Range (TIR):** Display the percentage of time spent in the target glucose range.
        *   **Variability:** Show the coefficient of variation (CV) to represent glucose variability.
        *   **Min/Max:** Highlight the minimum and maximum glucose values for the selected period.
        *   **Spike Detection:** Automatically detect and highlight significant spikes in blood glucose.
        *   **Average Glucose:** Display the average glucose level.
    *   **Highlights:** "Today's Highlights" section showing the number of spikes, time in range, and average glucose for the current day.
*   **Blood Pressure:**
    *   **Graph:** Scatter plot showing systolic and diastolic pressure over time.
    *   **Key Metrics:**
        *   **Average Systolic/Diastolic:** Display the average blood pressure.
        *   **Hypertension Stages:** Color-code the data points based on hypertension stages (Normal, Elevated, Stage 1, Stage 2).
    *   **Highlights:** "This Week's Report" showing the percentage of readings in each hypertension stage.
*   **Heart Rate:**
    *   **Graph:** Line graph showing resting heart rate and heart rate during activities.
    *   **Key Metrics:**
        *   **Resting Heart Rate:** Display the average resting heart rate.
        *   **Heart Rate Zones:** Show the time spent in different heart rate zones during exercise.
    *   **Highlights:** "Activity Insights" showing the workouts with the highest and lowest average heart rate.
*   **Oxygen Saturation (SpO2):**
    *   **Graph:** Line graph showing SpO2 levels over time, especially during sleep.
    *   **Key Metrics:**
        *   **Average SpO2:** Display the average oxygen saturation.
        *   **Low Oxygen Events:** Highlight instances where SpO2 drops below a certain threshold.
    *   **Highlights:** "Sleep Report" showing the number of low oxygen events during the last night's sleep.

#### 3.2.2. Activity

*   **Steps, Distance, Calories Burned:**
    *   **Graph:** Bar chart showing daily, weekly, and monthly totals.
    *   **Goals:** Allow users to set daily goals and visualize their progress.
    *   **Streaks:** Show the current and longest streak of meeting goals.
*   **Exercise:**
    *   **Map View:** For exercises with GPS data, display the route on a map.
    *   **Key Metrics:** Display duration, distance, pace, speed, elevation gain, and calories burned for each workout.
    *   **Personal Records:** Automatically detect and highlight personal records (e.g., fastest 5k, longest run).

#### 3.2.3. Sleep

*   **Graph:** Bar chart showing sleep duration and a stacked bar chart showing sleep stages (Awake, REM, Light, Deep).
*   **Key Metrics:**
    *   **Sleep Score:** A comprehensive score out of 100 based on duration, quality, and consistency.
    *   **Sleep Onset Latency:** Time it took to fall asleep.
    *   **Sleep Efficiency:** Percentage of time in bed that was spent asleep.
*   **Highlights:** "Last Night's Summary" showing the sleep score and a brief analysis of the sleep stages.

#### 3.2.4. Body Measurement

*   **Weight, Body Fat, Lean Body Mass:**
    *   **Graph:** Line graph showing trends over time.
    *   **Goal Tracking:** Allow users to set a target weight and track their progress.
*   **Waist/Hip Circumference:**
    *   **Key Metrics:** Calculate and display the waist-to-hip ratio.

#### 3.2.5. Nutrition

*   **Water Intake:**
    *   **Graph:** Bar chart showing daily water intake against a daily goal.
*   **Nutrition:**
    *   **Dashboard Widget:** Display a summary of macronutrient intake (protein, carbs, fat) for the day.

### 3.3. Data Import and Sync

*   **Initial Import:** On first launch, guide the user to connect to Health Connect and perform an initial import of their data.
*   **Background Sync:** Periodically sync new data from Health Connect in the background.
*   **Data Integrity:** Ensure that data is accurately and reliably synced, with no duplicates.

### 3.4. User Profile and Settings

*   **User Profile:** Allow users to set their name, age, gender, and other basic information.
*   **Notification Settings:** Allow users to configure notifications for things like meeting goals or new personal records.
*   **Theme:** Offer light and dark themes.

## 4. Non-Functional Requirements

*   **Performance:** The app should be fast and responsive, even with large amounts of data.
*   **Security:** All user data must be stored securely on the device and encrypted.
*   **Privacy:** The app must be transparent about the data it collects and how it is used. The app will only read data from Health Connect and will not write any data.
*   **Usability:** The app should be intuitive and easy to use for non-technical users.
