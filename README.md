PacKitty 🐱💖
A lightweight 2D arcade game built with Java Swing, featuring grid-based movement, collectible food, roaming ghosts, score and lives tracking, a persistent high score, a brief death animation, looping BGM, and a collision SFX. Navigate PacKitty through a tile‑mapped maze, avoid ghosts, and clear all food to reset the map and keep the run going.

Features
Tile map maze (21×19 tiles at 32 px) with solid walls, food dots, and four ghost types with simple randomized movement.

Smooth keyboard controls with continuous motion and collision handling against walls and ghosts.

Row‑9 corridor logic that prevents PacKitty from exiting the map at the extreme ends and pauses movement at the boundaries.

Lives (default 3), run score, and persistent high score displayed via a bold, white HUD.

Death sequence: on collision, shows a dedicated “dead kitty” sprite and plays a one‑shot death sound, then resets to origin.

Audio: a 10‑second WAV BGM loops during gameplay; BGM pauses on game over and resumes on restart.

Controls
Arrow keys: move Up, Down, Left, Right.

Any key after Game Over: restart (resets map positions, lives, score, and resumes BGM).

Assets
Sprites and tiles (e.g., wall pink.png, hk_front/left/right, ghost sprites).

Death sprite: packitty_dead.png.

Audio: packittybgm.wav (looping background), packittydeath.wav (death SFX).
All assets are bundled with the project; resource loading is classpath‑relative.

How it works
Game loop driven by a Swing Timer; paintComponent renders sprites, walls, foods, and HUD.

Collision detection via axis‑aligned rectangle checks for walls, ghosts, and foods.

Ghosts choose random directions and auto‑correct vertical movement when trapped in the center corridor.

BGM uses javax.sound.sampled Clip with LOOP_CONTINUOUSLY; SFX reuses a pre‑opened Clip and rewinds before play.

Build and run
Requirements: JDK 8+ (recommended JDK 17+).

Compile: javac App.java PacKitty.java

Run: java App
Ensure image and audio files are on the classpath (kept in the same project folder or a resources directory included on the classpath).

Project structure
App.java: initializes the JFrame, adds the PacKitty panel, configures window behavior.

PacKitty.java: main game panel (rendering, input handling, game loop, collisions, audio, HUD).

