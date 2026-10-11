[**Modrinth Page**](https://modrinth.com/mod/low-durability-switcher)

# Low Durability Switcher

Client-side Fabric mod that switches to another usable hotbar tool before the selected tool breaks.

Requires Fabric API and Cloth Config. Mod Menu adds a settings screen when installed.
Tool switching starts disabled in a new config; enable it with the R key or in Mod Menu.

## Supported Minecraft versions

The source uses official Mojang mappings throughout. Stonecutter builds separate jars for
1.21.8, 1.21.9, 1.21.10, 1.21.11, 26.1, 26.2 and 26.3. The 26.1 jar declares
compatibility with the 26.1 patch series, including 26.1.1 and 26.1.2.

Use JDK 25 for Gradle. The build uses Java 21 bytecode for 1.21.x and Java 25
bytecode for 26.x. Run `./gradlew.bat :26.3:build` for one version, or
`./gradlew.bat :1.21.8:build :1.21.9:build :1.21.10:build :1.21.11:build :26.1:build :26.2:build :26.3:build`
for all versions. Versioned jars are in each `versions/<version>/build/libs` directory.

# Features

- Tool and armor thresholds can be changed in Mod Menu
- Bind that allows you to turn the mod on and off _(Switching status in Options -> Controls)_
- The slot changes when the selected tool reaches the configured durability threshold. It prefers a healthy tool, then an empty slot, then a non-damageable item.
- Armor alerts sound at the configured threshold and every five durability points below it (for example, 60, 55, 50).
- Memorizing the status of the switcher after exiting the game. You will not need to turn on the switcher every time you re-enter the game

## Since 1.2 update:
- Added Mod Menu support (needs [Mod Menu](https://modrinth.com/mod/modmenu) + [Cloth Config]([https://modrinth.com/mod/cloth-config](https://modrinth.com/mod/cloth-config)))
- New fucntion: Armor sound alert (This function emits a sound when the armor strength falls below the specified threshold)

![idea64_9HPzx0fzlV](https://github.com/user-attachments/assets/7de77b3f-0f6a-4b45-a517-33ac3b607c2d)
![idea64_K0pSs8aqMY](https://github.com/user-attachments/assets/4a635690-3b45-47fd-b950-3923890e6778)


# Demonstration
https://github.com/FanyaOff/Low-Durability-Switcher/assets/73064979/cd843129-8891-4937-8c28-8149ae311b5c

# If you find a bug, [open the issue](https://github.com/FanyaOff/Low-Durability-Switcher/issues/new)

