# Health Connect Knowledge

This document summarizes the key information about Health Connect from the Android Developers website (https://developer.android.com/health-and-fitness/health-connect).

## Project Overview

Health Connect is a platform designed to simplify connectivity between health and fitness apps on Android. It allows apps to combine valuable health data, including medical records, to generate powerful insights, with user permission.

## Key Aspects and Functionalities

*   **Data Types:** Health Connect stores and structures various health and fitness data types, such as heart rate measurements, step counts, sleep data, skin temperature, and mindfulness data.
*   **Exercise Routes:** Users can track GPS routes for exercise activities and share workout maps with other apps.
*   **Medical Records:** The Medical Records feature extends Health Connect to include basic medical data in the Fast Healthcare Interoperability Resources (FHIR®) format, providing APIs for reading and writing this data and a UI for managing permissions.
*   **CRUD Operations and Data Synchronization:** Provides standard insert, update, and delete functions for recorded data, and allows client apps to synchronize data out of Health Connect.
*   **Android Compatibility:** Compatible with Android SDK version 28 (Pie) and higher, with an availability check for user devices.
*   **Reading Data:** Enables applications to continuously access a user's health data, even in the background, for uninterrupted analysis and synchronization.
*   **Migration from Google Fit:** Starting in 2026, Google will transition away from Google Fit APIs, with a migration guide available for moving from Fit Android API to Health Connect.
*   **Developer Resources:** The platform offers quick start guides, Codelabs, data type indexes, and migration guides. It covers data management basics like checking availability, feature availability, data format, writing, reading (raw and aggregated), deleting, synchronizing data, metadata requirements, and rate limits.
*   **UI and Permission Guidelines:** Includes guidelines for building onboarding flows, promoting Health Connect, managing permissions, and displaying data.
*   **Testing and Publishing:** Provides information on testing integrations with the Health Connect Toolbox, creating unit tests, and publishing apps.
*   **News and Updates:** Highlights recent updates such as the Health Connect Jetpack SDK Beta, new datatypes, and the stable release of Health Connect Jetpack Library 1.1.0.

## Developer Resources Mentioned

*   Quick start guides
*   Codelabs
*   Data type indexes
*   Migration guides (e.g., from Google Fit Android API)
*   Health Connect Toolbox for testing
*   Videos introducing new APIs, managing permissions, reading and writing data, and integration tips.

## Health Connect Data Categories and Types

Health Connect organizes data into several categories:

*   **Activity:**
    *   Active Calories Burned
    *   Basal Body Temperature
    *   Basal Metabolic Rate
    *   Cycling Pedaling Cadence
    *   Distance
    *   Elevation Gained
    *   Exercise
    *   Floors Climbed
    *   Power
    *   Speed
    *   Steps
    *   Total Calories Burned
    *   Training Plans
    *   VO2 Max
    *   Wheelchair Pushes
*   **Body Measurement:**
    *   Body Fat
    *   Bone Mass
    *   Height
    *   Hip Circumference
    *   Lean Body Mass
    *   Waist Circumference
    *   Weight
*   **Cycle Tracking:**
    *   Cervical Mucus
    *   Menstruation
    *   Ovulation Test
    *   Sexual Activity
*   **Nutrition:**
    *   Hydration
    *   Nutrition
*   **Sleep:**
    *   Sleep
*   **Vitals:**
    *   Blood Glucose
    *   Blood Pressure
    *   Body Temperature
    *   Heart Rate
    *   Oxygen Saturation
    *   Respiratory Rate
    *   Resting Heart Rate
*   **Wellness:**
    *   Mindfulness
*   **Medical Records:** (in FHIR® format)
    *   Allergies
    *   Conditions
    *   Lab Results
    *   Medications
    *   Patient Information
    *   Practitioner Details
    *   Pregnancy

## Permissions in Health Connect

Health Connect uses a granular permission model, requiring apps to request specific permissions for each data type.

*   **Permission Format:** `android.permission.health.READ_<DATA_TYPE>` or `android.permission.health.WRITE_<DATA_TYPE>`
*   **Examples:**
    *   `android.permission.health.READ_HEART_RATE`
    *   `android.permission.health.WRITE_STEPS`
*   **Manifest Declaration:** Permissions must be declared in the `AndroidManifest.xml` file.
*   **User Control:** Users grant and revoke permissions through the Health Connect app.
*   **Background Access:** Requires the `PERMISSION_READ_HEALTH_DATA_IN_BACKGROUND` permission.
*   **Historical Data:** Accessing data older than 30 days requires the `PERMISSION_READ_HEALTH_DATA_HISTORY` permission.

## Data Schema and Storage

*   **On-device Storage:** All data is stored and encrypted on the user's device.
*   **No Cloud Storage:** Google does not use the cloud to store Health Connect data.
*   **FHIR®:** Medical records are stored in the Fast Healthcare Interoperability Resources (FHIR®) format.