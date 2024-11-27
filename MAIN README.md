
   ### ANGRY_BIRD STATIC GUI 

***********************************************************************************************************************************************************************************************************************
FILES DIRECTORIES

For Github 
#Go to master Branch for code and files


AngryBird/
|-- gardle
|-- idea
|-- assets
|-- core /
      |-- build
      |-- src /
            |--main/
                 |-- java/
                       |-- com.angrybird.com/
                                                  |-- .java files 

|-- lwgjl3/
	|-- build
        |-- src /
         	   |--main/
             		    |-- java/
                     		  |-- com.angrybird.com/
						destopLauncher.java



*********************************************************************************************************************************************************************************************************************
## Prerequisites

Before running the Angry Bird project, ensure you have the following installed:

1. **Java Development Kit (JDK)**:
   - Ensure you have JDK 8 or higher installed. You can download it from [Oracle's official website](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) or use [OpenJDK](https://openjdk.java.net/install/).

2. **Gradle**:
   - Gradle is used for project management. Download and install it from the [Gradle website](https://gradle.org/install/). Ensure it is added to your system PATH.

3. **IDE**:
   - **IntelliJ IDEA** or **Eclipse**: Download and install one of these IDEs for Java development.
     - [IntelliJ IDEA](https://www.jetbrains.com/idea/download/)
     - [Eclipse IDE](https://www.eclipse.org/downloads/)

4. **LibGDX**:
   - Familiarity with LibGDX framework, as this project uses it for game development. Follow the [official LibGDX setup guide](https://libgdx.com/wiki/start).

*********************************************************************************************************************************************************************************************************************


## Running the DesktopLauncher in IntelliJ or Eclipse

### Steps to Run in IntelliJ IDEA

1. **Open the Project**:
- Launch IntelliJ IDEA.
- Open the project by navigating to `File` > `Open` and selecting the root directory of the AngryBird project.

2. **Locate the DesktopLauncher**:
- In the Project tool window (usually on the left), navigate to `lwjgl3/src/main/java/com/angrybird/com/`.
- Open `DesktopLauncher.java`.

3. **Run the DesktopLauncher**:
- Right-click on the `DesktopLauncher` file.
- Select `Run 'DesktopLauncher.main()'` from the context menu.
- The application should now build and run.

### Steps to Run in Eclipse

1. **Open the Project**:
- Launch Eclipse.
- Import the project by navigating to `File` > `Import...` > `Existing Projects into Workspace`, then browse to the AngryBird project root directory.

2. **Locate the DesktopLauncher**:
- In the Package Explorer, expand `lwjgl3/src/main/java/com/angrybird/com/`.
- Open `DesktopLauncher.java`.

3. **Run the DesktopLauncher**:
- Right-click on the `DesktopLauncher` file.
- Select `Run As` > `Java Application`.
- The application should now build and run.

*********************************************************************************************************************************************************************************************************************

Basic Gameplay Features
Bird Launching:

Players drag and launch birds from a catapult to hit pigs and structures. The trajectory of the bird is based on the angle and speed of the drag.
Physics:

The game uses LibGDX’s physics engine to simulate realistic collisions and object behavior. Birds, pigs, and structures are affected by gravity and other forces, leading to dynamic interactions.
Levels:

The game consists of multiple levels, each with different sets of birds, structures, and pigs. The objective is to eliminate all pigs on the screen.
Structure Destruction:

The structures are made of materials like wood, glass, and steel. These materials break on impact based on their properties, and when blocks collapse, it may cause a chain reaction, causing more destruction.
Pig Types and Health:

There are different pig types with varying health. Some pigs can be destroyed with one hit, while others require multiple hits to defeat.
Birds with Unique Abilities:

The game features multiple types of birds, each with its own unique abilities, such as speed, impact force, or special powers (e.g., the bluebird splitting into multiple smaller birds).
Winning and Losing Conditions:

Win: The player wins the level if all pigs are destroyed.
Lose: The player loses if they run out of birds without destroying all the pigs.
Birds Resupply:

Once a bird is launched, the next bird is placed on the catapult.
Levels and Progression:

The game features at least three levels, each with unique structures and pigs to keep the gameplay exciting.
Serialisation and Game Saves
The game includes a save and restore feature that allows players to save their progress and continue from where they left off. This is accomplished using serialization.

Save Game: Saves the current state of the level, including:

Birds remaining
Pigs' health and destruction status
Structures and their destruction status
Player's score
Restore Game: A restore menu allows players to load a previously saved game state.
*********************************************************************************************************************************************************************************************************************

## References for Our Project

1. Stack Overflow
2. libGDX Tutorial

*********************************************************************************************************************************************************************************************************************
## Team Members

- **Name**: Sahil Amrawat, **ID**: 2023462
- **Name**: Vaibhav Sorot, **ID**: 2023573

****************************************************************************************************************************************************************************************************************
## GitHub Repository

- [Main Repository](https://github.com/Sahilamr/APproject-.git)


