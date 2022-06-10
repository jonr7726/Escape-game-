# Escape
Escape was a game I created in 2020 for a school project where we had to use Greenfoot to make a game. It is an ascii-style horror game inspired by the film 'Alien' that has the player trying to escape a space station before it explodes with an advisary chasing them. To run the game Greenfoot must be installed; the game features a help screen that explains the rules and controls.

## Features
 - All of the game's art (with the exception of the background image) is created at run-time by displaying text to the screen
 - The game features music and sound effects (I did not create these) that attempt to emerse the player into an erie horror setting
 - 3 different levels are included, as well as a map editor that allows easy creation of maps using a GUI that are then saved and can be played
 - The advisary chasing the player will move randomly around the level looking for the player. It uses ray casting to spot the player, and then uses a breadth-first search to pathfind toward the player
 - Because of the project's restriction to using Greenfoot, I ended up having to override Greenfoot's rendering system (that uses the 'Actor' class) so that objects are not rendered every single frame if they don't need to be. This was successful in increasing the game's framerate, making it playable
