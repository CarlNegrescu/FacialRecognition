# Cougar Cam: Facial Recognition Security System

Cougar Cam is a real-time facial recognition application built in Java. It uses a webcam to detect and identify registered users, simulating a secure entry system. The application features a complete graphical user interface (GUI) built with Java Swing for both camera operation and administrative user management.

---

## Features

-   **Real-Time Face Detection**: Captures video from a webcam and detects faces in real-time using OpenCV's Haar Cascades.
-   **Accurate Face Recognition**: Utilizes a deep learning model (`SFace`) to generate facial feature embeddings and match them against a database of known users.
-   **User Authentication Logic**: Grants or denies access based on a confidence threshold, requiring multiple successful recognitions to validate a user and prevent false positives.
-   **Graphical User Interface**: A user-friendly Swing GUI allows for starting/stopping the camera and accessing an admin panel.
-   **Secure Admin Panel**: A password-protected administrative interface to manage the user database.
-   **Full CRUD Functionality**: Administrators can **C**reate (Add), **R**ead (View), **U**pdate (Edit), and **D**elete users from the database.
-   **Persistent User Storage**: User information and facial encodings are stored in a local SQLite database.

---

## Technologies Used

-   **Language**: **Java**
-   **Computer Vision**: **OpenCV 4.x**
-   **GUI**: **Java Swing**
-   **Database**: **SQLite** with **JDBC** for connectivity
-   **Face Detection Model**: Haar Cascade (`haarcascade_frontalface_default.xml`)
-   **Face Recognition Model**: SFace (`face_recognition_sface_2021dec.onnx`)

---

## Prerequisites

Before you begin, ensure you have the following installed on your system:

-   **Java Development Kit (JDK)**: Version 8 or higher.
-   **OpenCV**: Must be installed on your system, and you must know the path to the Java bindings (e.g., `opencv-4XX.jar`) and the native library files (`.dll`, `.so`, or `.dylib`).
-   An IDE like **Eclipse** or **IntelliJ IDEA** is highly recommended for easier project setup.

---

## Setup and Installation

1.  **Clone the repository:**
    ```bash
    git clone
    cd FacialRecognition
    ```

2.  **Configure Project Dependencies:**
    -   Add the **OpenCV JAR file** (e.g., `opencv-4XX.jar`) to your project's build path/classpath.
    -   Add the **SQLite JDBC Driver JAR file** to your project's build path/classpath. You can download it from [here](https://github.com/xerial/sqlite-jdbc/releases).

3.  **Configure OpenCV Native Library:**
    This is the most critical step. The Java code needs to locate the native OpenCV library (`.dll` for Windows, `.so` for Linux). You must specify its path in your IDE's run configuration.
    -   In **Eclipse** or **IntelliJ**, go to "Run Configurations" -> "Arguments" -> "VM arguments".
    -   Add the following line, replacing `path/to/opencv/build/java/x64` with the actual path on your system:
        ```
        -Djava.library.path="path/to/opencv/build/java/x64"
        ```

4.  **Verify Project Structure:**
    Ensure that the `resources` directory is in the root of your project and contains the necessary files:
    ```
    .
    ├── src
    │   ├── backend
    │   ├── cameramodule
    │   ├── cougarCam
    │   ├── frontend
    │   └── utils
    ├── resources
    │   ├── facesDataBase.db
    │   ├── face_recognition_sface_2021dec.onnx
    │   └── haarcascades
    │       └── haarcascade_frontalface_default.xml
    └── ... (other project files)
    ```

---

## How to Run

1.  Open the project in your preferred Java IDE (Eclipse, IntelliJ, etc.).
2.  Ensure you have completed all the steps in the **Setup and Installation** section.
3.  Locate the `Main.java` file in the `cougarCam` package.
4.  Run `Main.java` as a Java Application.

---

## Usage

### Main Screen
When you launch the application, you will see the main screen with two options:
-   **Start Front Door Camera**: Begins the real-time video feed, detects faces, and attempts to recognize them. A window titled "CameraView" will appear showing the feed.
-   **Log In**: Opens a dialog to enter admin credentials.

### Admin Panel
1.  Click the **"Log In"** button.
2.  Enter the hardcoded credentials to access the admin panel:
    -   **Username**: `admin`
    -   **Password**: `password`
3.  Once logged in, you can click **"Manage Users"** to access the user database.

### Managing Users
In the "Manage Users" screen, you have three options:
-   **Add User**: Opens a new window where you can enter a first name, last name, and choose an image file for a new user. The application will detect the face in the image and save its encoding to the database.
-   **Edit User**: Allows you to select an existing user from a list and update their name or replace their photo.
-   **Delete User**: Lets you permanently remove a user from the database.

---

## Code Overview

-   **`cougarCam.Main`**: The entry point of the application. It initializes the `DataAccess` object and starts the `MainUI`.
-   **`cougarCam.DoorManager`**: The core controller class. It orchestrates the `Camera` and `FacialRec` modules and manages the data flow between them using blocking queues.
-   **`frontend.MainUI`**: Manages the main application window and navigation between the login and admin panels using a `CardLayout`.
-   **`frontend.AdminUI`**: Contains all the logic and components for the user management windows (Add, Edit, Delete).
-   **`cameramodule.Camera`**: Handles capturing video from the webcam, running the Haar Cascade detector to find faces, and placing cropped face images into a queue for processing.
-   **`backend.FacialRec`**: Pulls detected faces from the queue, generates feature embeddings using the SFace model, and compares them against the database records to find a match.
-   **`backend.DataAccess`**: Implements the `IDataAccess` interface and handles all SQL queries and communication with the `facesDataBase.db` SQLite database.
-   **`backend.SerializeData`**: A utility class to convert OpenCV `Mat` objects into byte arrays (for database storage) and back.