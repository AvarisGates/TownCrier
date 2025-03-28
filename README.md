# Town Crier
A common event API library for the AvarisGates project.
Provides a lot of new events such as on player join, or on player kill.
The mod is split into two logical sides common and client.
For a more in depth view please check out the source code and the included documentation.

# Usage
To use the mod as a library add the maven repository and declare it as a dependency, in your `build.gradle` file like so:
```groovy
// Add the repository
repositories {
    maven {
        url = 'https://maven.avarisgates.com' // TownCrier
    }
}

// Declare it as a dependency
dependencies {
    // To package the mod with yours
    modImplementation "com.avaris:TownCrier:${project.towncrier_version}"
    
    // To use the mod in a seperate .jar file
    // This won't strictly require the mod
    compileOnly "com.avaris:TownCrier:${project.towncrier_version}"
}
```
It's also recommended to add the following to your `gradle.properties` file:
```properties
towncrier_version=CURRENT_VERSION
```
Be sure to add replace `CURRENT_VERSION` with the version you wish to use.
It's always recommended to use the latest one.

# Example
Let's register a new event callback. For example the `PLAYER_GOT_KILL_EVENT`, which is called after a player gets credit for killing a `LivingEntity`.
```java
// Includes
import com.avaris.towncrier.api.v1.impl.PlayerEvents;

// Your mod's init function
@Override
public void onInitialize() {
    // Register event callback
    PlayerEvents.PLAYER_GOT_KILL_EVENT.register((player, entity) -> {
        System.out.println(player.getNameForScoreboard()+" killed "+entity.getNameForScoreboard()+" using "+player.getMainHandStack().getItem().getName());
    });
}
```
