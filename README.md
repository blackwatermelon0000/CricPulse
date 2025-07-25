# 🏏 CricPulse – JavaFX Cricket Management App

**CricPulse** is a desktop app built using JavaFX to simulate and manage cricket matches, teams, and players. It connects to an Oracle Express backend using JDBC.

## 🚀 Features

- Match simulation with scorecards
- Player and team management
- Points table, analytics, top scorers
- Oracle DB connectivity via ojdbc
- FXML-based JavaFX UI

## 🧰 Tech Stack

- JavaFX + FXML
- Oracle Express (XE)
- ojdbc11.jar
- IntelliJ IDEA

## 🔧 How to Run

1. Clone this repo
2. Open it in IntelliJ IDEA
3. Add `/lib/ojdbc11.jar` to dependencies
4. Run `CricketApp.java` from `/src/`
5. Run `cricket_db_script.sql` inside Oracle
6. Edit DB credentials in `DBConnect.java` if needed

## 💾 Database Setup

- File: `cricket_db_script.sql`
- DB: Oracle XE
- Table structure includes teams, players, matches, scorecards, etc.

## 📦 Build JAR (Optional)

1. File → Project Structure → Artifacts
2. Add JAR from Modules
3. Build → Build Artifacts → JAR → Build
