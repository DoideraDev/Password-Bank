# 🔐 Password Bank

**The right choice if you want to really secure your passwords**

A modern, user-friendly desktop application for securely managing and storing your passwords with enterprise-grade protection.

---

## 📋 Overview

Password Bank is a powerful password management solution built with JavaFX that provides a secure, intuitive interface for storing, organizing, and managing your credentials. Whether you need to maintain a few passwords or hundreds, Password Bank offers robust encryption and recovery features to keep your sensitive data safe.

## ✨ Key Features

- **Secure Password Storage** – Your passwords are encrypted and protected with industry-standard security
- **User Authentication** – Create and manage accounts with master password protection
- **Password Management** – Easily create, edit, search, and delete stored passwords
- **Account Recovery** – Security question-based account recovery system with mobile number and alternate email support
- **Session Management** – Stay logged in option for quick access (optional)
- **Dark Mode** – Eye-friendly dark theme for comfortable use
- **Auto-Update** – Built-in updater module ensures you're always running the latest version
- **Clean Uninstall** – Dedicated uninstaller for completely removing the application

## 🛠️ Technology Stack

- **Language:** Java 17
- **UI Framework:** JavaFX 21
- **Build Tool:** Maven
- **Architecture:** Multi-module modular Java application

## 📦 Project Structure

```
Password-Bank/
├── main-app/          # Main application module
├── updater/           # Auto-update module
├── uninstaller/       # Application uninstaller
└── doiderautils/      # Utility library and shared components
```

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.8+

### Build from Source

```bash
cd passwordbank
mvn clean install
```

### Run the Application

```bash
cd main-app
mvn javafx:run
```

## 🔒 Security Features

- **Password Encryption** – All stored passwords are encrypted before being saved to disk
- **Master Password Protection** – Access to your password vault requires master password authentication
- **Secure Account Recovery** – Three security questions and answers for account recovery
- **Local Storage** – All data is stored locally on your machine (no cloud synchronization)

## 📋 User Capabilities

### Account Management
- Create a new secure account
- Recover account access through security questions
- Customize username and email
- Toggle between dark and light themes

### Password Management
- Store unlimited passwords with identifiers (service names)
- Quick search functionality for finding stored passwords
- View/hide passwords with toggle button
- Edit existing password entries
- Delete passwords securely
- Organized grid view of all stored passwords

## 📝 Installation

Application is packaged as an MSI installer for Windows users, available in the releases section. The installer includes:
- Main Application
- Auto-Updater
- Uninstaller utility

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**DoideraDev**

---

## 🤝 Contributing

Contributions are welcome! Feel free to fork this project and submit pull requests for any improvements.

---

**Password Bank** - *Secure Password Management Made Simple*
