# MobileTowers
This is a simple Paper 1.20 plugin that emulates mobile network towers in Minecraft.
Some features may be buggy and the plugin isn't well optimised, the code is not good. You're free to use this on your own server or fork this as long as you respect the license.

# How to build
Just import this into your favorite Java IDE (such as IntelliJ Idea) and run the equivalent of `gradlew build`, `gradlew jar` and `gradlew reobfJar`.

# Running the plugin
Just drag this in your Paper server (1.20.2 forks should work well, but use them at your own risk), start your server, configure the plugin at "Plugins -> MobileTowers -> config.yml" and restart your server.

# Placeholders
PlaceholderAPI is recommended for placeholders.
Note: some of the Placeholders may not be implemented in the plugin.

## network
The following placeholder shows the Player's network signal quality. Use it with `%mobiletowers.network%`.
