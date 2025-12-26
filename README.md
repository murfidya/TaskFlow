# TASKFLOW APPLICATION USER MANUAL
## Personal Task Management Android Application
## TABLE OF CONTENTS
1. [Introduction](#1-introduction)
2. [System Requirements](#2-system-requirements)
3. [Application Installation](#3-application-installation)
4. [User Guide](#4-user-guide)
   - 4.1 [Creating a New Account](#41-creating-a-new-account)
   - 4.2 [Logging In](#42-logging-in)
   - 4.3 [Forgot Password](#43-forgot-password)
   - 4.4 [Home Page](#44-home-page)
   - 4.5 [Adding a New Task](#45-adding-a-new-task)
   - 4.6 [Editing a Task](#46-editing-a-task)
   - 4.7 [Deleting a Task](#47-deleting-a-task)
   - 4.8 [Calendar Page](#48-calendar-page)
   - 4.9 [Profile Page](#49-profile-page)
   - 4.10 [Changing Password](#410-changing-password)
   - 4.11 [Dark Mode](#411-dark-mode)
   - 4.12 [Logout](#412-logout)
5. [Application Features](#5-application-features)
6. [FAQ (Frequently Asked Questions)](#6-faq)
7. [Technical Information](#7-technical-information)


## 1. INTRODUCTION
### 1.1 About TaskFlow
**TaskFlow** is an Android-based task management application designed to help users organize, track, and complete their daily tasks more effectively and efficiently.

### 1.2 Application Purpose
This application aims to:
- Help users record and manage personal tasks
- Categorize tasks based on activity types
- Set priorities and deadlines for each task
- Track task completion status
- Provide a calendar view for task schedule visualization

### 1.3 Key Features
| Feature | Description |
|---------|-------------|
| Task Management | Add, edit, and delete tasks |
| Categorization | Group tasks by category (Personal, Work, Study, Other) |
| Priority | Set priority levels (Low, Medium, High) |
| Task Status | Track progress (Pending, In Progress, Completed) |
| Calendar | View tasks by date |
| Filter & Search | Find tasks quickly |
| Dark Mode | Comfortable display in low-light conditions |
| Cloud Sync | Data securely stored in Firebase |


## 2. SYSTEM REQUIREMENTS
### 2.1 Minimum Requirements
| Component | Requirement |
|-----------|-------------|
| Operating System | Android 10 (API Level 29) or higher |
| RAM | Minimum 2 GB |
| Storage | Minimum 50 MB free space |
| Internet Connection | Required for data synchronization |
| Account | Active email for registration |

### 2.2 Recommended Requirements
| Component | Recommendation |
|-----------|----------------|
| Operating System | Android 12 or higher |
| RAM | 4 GB or more |
| Internet Connection | WiFi or 4G/LTE for best experience |


## 3. APPLICATION INSTALLATION
### 3.1 Installation Steps
1. **Download APK File**
   - Obtain the TaskFlow APK file from a trusted source

2. **Allow Installation from Unknown Sources**
   - Open **Settings** > **Security**
   - Enable **Unknown Sources** or **Install Unknown Apps** option

3. **Install Application**
   - Open the downloaded APK file
   - Tap **Install**
   - Wait for the installation process to complete

4. **Open Application**
   - Tap **Open** or find the TaskFlow icon in the app menu


## 4. USER GUIDE
### 4.1 Creating a New Account
To use TaskFlow, you need to create an account first.

**Steps:**
1. Open the TaskFlow application
2. On the login page, tap the **Sign Up** button
3. Fill in the registration form:
   - **Username**: Enter a username (minimum 3 characters)
   - **Email**: Enter a valid email address
   - **Password**: Enter a password (minimum 7 characters)
4. Tap the **Sign Up** button
5. Check your email for account verification
6. Click the verification link in the email
7. Return to the application and log in with the created account

**Important Notes:**
- Email must be valid as it will be used for verification
- Password must be at least 7 characters for security
- Account cannot be used until email is verified


### 4.2 Logging In
After having a verified account, you can log in to the application.

**Steps:**
1. Open the TaskFlow application
2. Enter your registered **Email**
3. Enter your **Password**
4. Tap the **Login** button
5. If successful, you will be directed to the Dashboard page

**Possible Error Messages:**
- "Please verify your email first" - Email not yet verified
- "Login Failed" - Incorrect email or password
- "All fields are required" - Some fields are empty


### 4.3 Forgot Password
If you forget your password, use the password reset feature.

**Steps:**
1. On the login page, tap **Forgot Password?**
2. Enter your registered email address
3. Tap the **Recover Password** button
4. Check your email inbox
5. Click the password reset link in the email
6. Create a new password
7. Log in with the new password


### 4.4 Home Page
The Home page displays a list of all your tasks.

**Home Page Components:**
1. **Welcome Message**
   - Displays "Hello, [Username]!"

2. **Search Field**
   - Search tasks by title or description
   - Type keywords to filter tasks

3. **Category Filter**
   - Select category: All, Personal, Work, Study, Other
   - Displays tasks according to selected category

4. **Status Filter**
   - Select status: All, Pending, In Progress, Completed
   - Displays tasks according to selected status

5. **Priority Filter**
   - Select priority: All, High, Medium, Low
   - Displays tasks according to selected priority

6. **Task List**
   - Displays all tasks matching the filters
   - Tap a task to view or edit

7. **Add Button (+)**
   - Floating button at the bottom corner
   - Tap to add a new task


### 4.5 Adding a New Task
To add a new task to the list.

**Steps:**
1. Tap the **+** button (floating action button) on the Home page
2. Fill in the task form:

   | Field | Description | Required |
   |-------|-------------|----------|
   | Title | Task title | Yes |
   | Description | Detailed task description | No |
   | Category | Task category | Yes |
   | Priority | Priority level | Yes |
   | Status | Completion status | Yes |
   | Due Date | Deadline | No |

3. **Selecting Category:**
   - Personal: Personal tasks
   - Work: Work-related tasks
   - Study: Academic/learning tasks
   - Other: Other tasks

4. **Selecting Priority:**
   - Low: Low priority
   - Medium: Medium priority
   - High: High priority

5. **Selecting Status:**
   - Pending: Not started
   - In Progress: Currently working on
   - Completed: Already finished

6. **Selecting Date:**
   - Tap the date field
   - Select a date from the calendar that appears

7. Tap the **Save** button to save the task


### 4.6 Editing a Task
To modify information of an existing task.

**Steps:**
1. On the Home page, tap the task you want to edit
2. The Edit Task page will open
3. Modify the desired information:
   - Task title
   - Description
   - Category
   - Priority
   - Status
   - Due date
4. Tap the **Update** button to save changes

**Tips:**
- Change status to "Completed" when the task is finished
- Update priority if task urgency changes


### 4.7 Deleting a Task
To permanently remove a task from the list.

**Steps:**
1. Tap the task you want to delete
2. On the Edit Task page, tap the **Delete** button
3. A confirmation dialog will appear
4. Tap **Delete** to confirm deletion
5. Tap **Cancel** to abort

**Warning:**
- Deleted tasks cannot be recovered
- Make sure you are certain before deleting


### 4.8 Calendar Page

The Calendar page helps you view tasks by date.

**How to Access:**
1. Tap the **Calendar** icon in the bottom navigation bar

**How to Use:**
1. **Viewing the Calendar**
   - The calendar displays the current month
   - Swipe left/right for previous/next month
2. **Selecting a Date**
   - Tap the desired date
   - Task list for that date will appear below the calendar
3. **Viewing Tasks**
   - Tasks are displayed based on due date
   - Tap a task to view or edit
4. **Empty Message**
   - If there are no tasks on the selected date, a message "No tasks for this date" will appear


### 4.9 Profile Page
The Profile page displays account information and application settings.

**How to Access:**
1. Tap the **Profile** icon in the bottom navigation bar

**Information Displayed:**
- Username
- Email

**Available Settings:**
- Dark Mode
- Change Password
- Logout


### 4.10 Changing Password
To change your account password.

**Steps:**
1. Open the **Profile** page
2. Tap the **Change Password** option
3. Fill in the form:
   - **Current Password**: Your current password
   - **New Password**: New password (minimum 7 characters)
   - **Confirm Password**: Repeat the new password
4. Tap the **Change Password** button
5. If successful, you will return to the previous page

**Possible Error Messages:**
- "Current password is incorrect" - Wrong current password
- "Passwords do not match" - Password confirmation doesn't match
- "New password must be at least 7 characters" - Password too short


### 4.11 Dark Mode
Dark mode changes the application display to a dark theme.

**How to Enable:**
1. Open the **Profile** page
2. Find the **Dark Mode** option
3. Slide the toggle to the right to enable
4. Slide the toggle to the left to disable
5. 
**Benefits of Dark Mode:**
- Reduces eye strain in low-light conditions
- Saves battery on AMOLED screens
- Settings are saved and will be remembered on next login


### 4.12 Logout
To sign out from your TaskFlow account.
**Steps:**
1. Open the **Profile** page
2. Tap the **Logout** option
3. A confirmation dialog will appear
4. Tap **Yes** to sign out
5. Tap **No** to cancel
**Notes:**
- After logout, you must log in again to access tasks
- Task data remains stored on the server


## 5. APPLICATION FEATURES
### 5.1 Feature Summary
| Feature | Description |
|---------|-------------|
| **Authentication** | Login, registration, email verification, password reset |
| **Task Management** | CRUD (Create, Read, Update, Delete) tasks |
| **Categorization** | 4 default categories (Personal, Work, Study, Other) |
| **Priority** | 3 levels (Low, Medium, High) |
| **Status** | 3 statuses (Pending, In Progress, Completed) |
| **Filter** | Filter by category, status, and priority |
| **Search** | Search tasks by title/description |
| **Calendar** | Calendar view to see tasks by date |
| **Dark Mode** | Dark theme for eye comfort |
| **Cloud Sync** | Data synchronization with Firebase |

### 5.2 Task Categories
| Category | Color | Usage |
|----------|-------|-------|
| Personal | Green (#4CAF50) | Personal tasks, daily activities |
| Work | Blue (#2196F3) | Work tasks, office projects |
| Study | Orange (#FF9800) | Academic tasks, learning |
| Other | Gray (#9E9E9E) | Other tasks not fitting above categories |

### 5.3 Priority Levels
| Priority | Description |
|----------|-------------|
| High | Urgent and important tasks, must be completed immediately |
| Medium | Important but not too urgent tasks |
| Low | Tasks that can be postponed or done later |

### 5.4 Task Status
| Status | Description |
|--------|-------------|
| Pending | New task, not yet started |
| In Progress | Task is being worked on |
| Completed | Task is finished |


## 6. FAQ
### Q1: Is my data safe?
**A:** Yes, your data is stored in Firebase Cloud with an encrypted security system. Each user can only access their own data.
### Q2: Does this application require an internet connection?
**A:** Yes, an internet connection is required for data synchronization with Firebase servers.
### Q3: What if I forget my password?
**A:** Use the "Forgot Password" feature on the login page. A password reset link will be sent to your email.

## 7. TECHNICAL INFORMATION
### 7.1 Application Specifications
| Item | Detail |
|------|--------|
| Application Name | TaskFlow |
| Package Name | id.ac.binus.taskflow |
| Version | 1.0 |
| Minimum SDK | Android 10 (API 29) |
| Target SDK | Android 14 (API 36) |
| Programming Language | Java |
| Database | Firebase Firestore |
| Authentication | Firebase Authentication |

### 7.2 Technologies Used
| Component | Technology |
|-----------|------------|
| UI Framework | Android SDK |
| Database | Firebase Firestore |
| Authentication | Firebase Auth |
| UI Components | Material Design |
| Navigation | Bottom Navigation View |
| List Display | RecyclerView |
| Date Picker | DatePickerDialog |

### 7.3 Data Structure
**User Collection:**
```
users/
  └── {userId}/
        ├── username: String
        ├── email: String
        ├── photoUrl: String
        ├── darkMode: Boolean
        ├── createdAt: Timestamp
        ├── categories/
        │     └── {categoryId}/
        │           ├── name: String
        │           ├── color: String
        │           └── icon: String
        └── tasks/
              └── {taskId}/
                    ├── title: String
                    ├── description: String
                    ├── category: String
                    ├── priority: String
                    ├── status: String
                    ├── dueDate: Timestamp
                    ├── createdAt: Timestamp
                    └── updatedAt: Timestamp
```

## CLOSING
Thank you for using **TaskFlow**. We hope this application helps you organize and complete your daily tasks more effectively.
