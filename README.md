# Chrome Dinosaur - Java Game 🦖

This project is a simple Java Swing clone of the famous **Dino Run** game that appears in Google Chrome when there is no internet connection.  
Try to avoid cacti and birds to achieve the highest possible score!

## Features
- The dinosaur runs, jumps and dies upon collision.
- Cacti and birds spawn at random times and heights.
- Score tracking system.
- Press "Space" to restart the game after a Game Over.
- Simple bird animation with flapping wings.
  
## Installation and Running
1. Download or clone the repository:
   ```bash
   git clone https://github.com/your-username/chrome-dinosaur-java.git
   ```
2. Make sure you have Java installed (Java 8 or higher recommended).
3. Open a terminal in the project directory.
4. Compile the project:
   ```bash
   javac ChromeDinosaur.java
   ```
5. Run the game:
   ```bash
   java ChromeDinosaur
   ```

> **Note:** Make sure the required images (`dino-run.gif`, `dino-dead.png`, `dino-jump.png`, `cactus1.png`, `cactus2.png`, `cactus3.png`, `bird1.png`, `bird2.png`) are located inside the `./img/` folder.

## Game Controls
- **Space Bar**:
  - While playing: Make the dinosaur jump.
  - After Game Over: Restart the game.

## Project Structure
- `ChromeDinosaur.java`: Main game file.
  - `Block` class: Basic structure for obstacles and the dinosaur.
  - `AnimatedBird` class: Special class for animated birds.

## Technologies Used
- **Java Swing** (for GUI)
- **Java AWT** (for graphics drawing and event handling)

## Contributing
Contributions are welcome!  
Fork the repository, make your changes and submit a pull request.
