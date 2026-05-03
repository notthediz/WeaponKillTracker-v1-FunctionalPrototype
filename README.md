# WeaponKillTracker-v1-FunctionalPrototype
A Minecraft Paper plugin that tracks player kills and weapon usage with persistent storage.

# WeaponKillTracker

A Minecraft Paper plugin that tracks player kills and records which weapons are used to get them.  
Data is persisted across server restarts using YAML storage.

---

## Features

- Tracks total player kills
- Tracks kills per weapon (including unarmed kills as `AIR`)
- Saves data to `stats.yml` automatically
- Loads data on server startup
- `/stats` command to view personal kill statistics
- Fully persistent across server restarts

---

## How it works

When a player kills an entity:
- The plugin detects the killer using `EntityDeathEvent`
- Increments their total kill count
- Records the weapon in their main hand at the time of the kill
- Saves everything into memory and persists it to `stats.yml` on shutdown

Unarmed kills are recorded as `AIR`.

---

## Commands

### `/stats`
Displays your personal kill statistics:

Example output:
--- Your Stats ---

Kills: 12

Weapons:

DIAMOND_SWORD: 7
BOW: 3
AIR: 2

---

## Requirements

- Java 21+
- Paper 1.20.6+
- Maven (for building)

---

## Installation

1. Build the plugin using Maven:

mvn clean package

2. Locate the compiled jar:

/target/weaponkilltracker-1.0.jar


3. Move it into your server's plugins folder:

/plugins


4. Start or restart your Paper server

---

## Data Storage

Player statistics are stored in:

/plugins/WeaponKillTracker/stats.yml


Example structure:
yaml
kills:
  655addc3-69e7-4d13-937d-3f3f555eaf72: 12

weapons:
  655addc3-69e7-4d13-937d-3f3f555eaf72:
    DIAMOND_SWORD: 7
    BOW: 3
    AIR: 2

## Tech Stack

Java
Paper API
Bukkit/Spigot event system
YAML configuration storage
Maven build system

## Project Status

This is a functional prototype (v1) built for learning plugin development concepts such as:

Event handling
Command execution
Persistent data storage
Plugin lifecycle (enable/disable)

Future improvements planned:

/stats <player> support
leaderboard system (/topkills)
GUI-based stats menu
cleaner architecture (service/manager separation)
👤 Author

Built by @notthediz as a learning project in Minecraft plugin development.
