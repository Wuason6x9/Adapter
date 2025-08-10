# Adapter library

### **Items Utils for Minecraft plugins.**

### [JavaDoc here](https://jitpack.io/com/github/Wuason6x9/Adapter/1.0.6.1/javadoc "Go to javadoc")

## Index

- [Introduction](#introduction)
- [Setup](#setup)
  - [Maven](#maven-pom)
  - [Gradle Groovy](#gradle)
  - [Gradle Kotlin DSL](#gradle-kotlin-dsl)
  - [After Setup](#after-setup)
- [Usage](#usage)
- [Compatibilities](#compatibilities)
- [More Examples](#more-examples)
- [Do you want to test the library?](#do-you-want-to-test-the-library)

> [!NOTE]
> This library will still be updated and improved, so if you have any suggestions or find any bugs, please let me know.

### Introduction

You are tired of using the api of each plugin to get the item? <br>
You are tired of using the api of each plugin to get the item id of the item, block or entity?

**This library is for you! <br>**

Simple format: **adapter_plugin:item_id** <br>

This library is a simple and easy-to-use library that allows you to get the item, block or entity from the id of the adapter, and vice versa.
Allows transforming string to itemstack and itemstack to string.<br>
**itemstack diamond -> mc:diamond <br> 
mc:diamond -> itemstack diamond**

### Setup

<details id="maven-pom">
<summary style="color: aqua">Maven (pom.xml) CLICK!</summary>

## Add the repository to your pom.xml file:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.6.0</version>
            <executions>
                <execution>
                    <id>shade</id>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <minimizeJar>true</minimizeJar>
                <relocations>
                    <relocation>
                        <pattern>dev.wuason.adapter</pattern>
                        <!-- TODO: Change this to my own package name -->
                        <shadedPattern>my.project.libs.adapter</shadedPattern>
                    </relocation>
                </relocations>
            </configuration>
        </plugin>
    </plugins>
</build>

<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.Wuason6x9</groupId>
    <artifactId>Adapter</artifactId>
    <version>RELEASE-VERSION</version>
    <scope>provided</scope>
</dependency>
```
</details>

<details id="gradle">
<summary style="color: aqua">Gradle (build.gradle) CLICK!</summary>

## Add the repository to your build.gradle file:

```groovy
plugins {
    id 'com.gradleup.shadow' version '8.3.5'
    id 'java'
}

repositories {
maven { url 'https://jitpack.io' }
}

dependencies {
implementation 'com.github.Wuason6x9:Adapter:RELEASE-VERSION'
}

tasks {
shadowJar {
relocate("dev.wuason.adapter", "my.project.libs.adapter")
}
}
```
</details>

<details id="gradle-kotlin-dsl">
<summary style="color: aqua">Gradle Kotlin DSL (build.gradle.kts) CLICK!</summary>

## Add the repository to your build.gradle.kts file:

```kotlin
plugins { 
    id("java")
    id("com.gradleup.shadow") version "8.3.5"
}

repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.Wuason6x9:Adapter:RELEASE-VERSION")
}

tasks.shadowJar {
    relocate("dev.wuason.adapter", "my.project.libs.adapter")
}
```
</details>

### After Setup

> [!IMPORTANT]
> If you do not this step, the library will not work.

You need to initialize the library in your plugin:
```java
public class PluginExample extends JavaPlugin {
    @Override
    public void onEnable() {
        Adapter.init(this);
    }
}
```


### Usage

Examples of how to use the library:

#### Get an ItemStack from an adapter id

```java
public class PluginExample extends JavaPlugin {
    @Override
    public void onEnable() {
        Adapter.init(this);
        
        String adapterId = "mc:diamond_sword";
        ItemStack item = Adapter.getItemStack(adapterId);
        
        if (item != null) {
            getLogger().info("Item: " + item.getType()); // Item: DIAMOND_SWORD
        }
    }
}
```

#### Get an adapter id from ItemStack

```java
public class PluginExample extends JavaPlugin {
    @Override
    public void onEnable() {
        Adapter.init(this);
        
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        String adapterId = Adapter.getAdapterId(item);
        
        if (adapterId != null) {
            getLogger().info("Adapter id: " + adapterId); // Adapter id: mc:diamond_sword
        }
    }
}
```

#### Get an adapter id from Block and Entity

```java
public class PluginExample extends JavaPlugin {
    @Override
    public void onEnable() {
        Adapter.init(this);
        String adapterBlock = Adapter.getAdapterId(block);
        String adapterEntity = Adapter.getAdapterId(entity);
    }
}
```

#### Check if an adapter id exists

```java
public class PluginExample extends JavaPlugin {
    @Override
    public void onEnable() {
        Adapter.init(this);
        
        String adapterId = "mc:diamond_sword";
        if (Adapter.exists(adapterId)) {
            getLogger().info("The adapter id exists");
        }
    }
}
```

#### Get all items from all adapters

```java
public class PluginExample extends JavaPlugin {
    @Override
    public void onEnable() {
        Adapter.init(this);
        
        List<String> items = Adapter.getAllItems();
        for (String item : items) {
            getLogger().info("Item: " + item);
        }
    }
}
```

### Compatibilities

| Plugin                                                                                                                                                | adapter    | aliases                                    | syntax                                                | format example                                                | extra info                                  |
|-------------------------------------------------------------------------------------------------------------------------------------------------------|------------|--------------------------------------------|-------------------------------------------------------|---------------------------------------------------------------|---------------------------------------------|
| Minecraft                                                                                                                                             | `mc`       | `vanilla`, `minecraft`                     | `(mc, minecraft, vanilla):(material)`                 | `mc:stone` or `mc:diamond_pickaxe[custom_name='"mypickaxe"']` | Support nbt/components /give command format |
| [ItemsAdder](https://www.spigotmc.org/resources/%E2%9C%A8itemsadder%E2%AD%90emotes-mobs-items-armors-hud-gui-emojis-blocks-wings-hats-liquids.73355/) | `ia`       | `itemsadder`                               | `(ia, itemsadder):[namespace](id)`                    | `ia:iasurvival:ruby_sword` or `ia:ruby_sword`                 |                                             |
| [Nexo](https://polymart.org/resource/nexo.6901)                                                                                                       | `nx`       | `nexo`                                     | `(nx, nexo):(id)`                                     | `nx:ruby_sword` or `nexo:ruby_sword`                          |                                             |
| [Oraxen](https://github.com/oraxen/oraxen)                                                                                                            | `or`       | `oraxen`, `oxn`                            | `(or, oraxen, oxn):(id)`                              | `or:ruby_sword` or `oraxen:ruby_sword`                        |                                             |
| [CustomItems](https://polymart.org/resource/custom-items.1)                                                                                           | `ci`       | `customitems`, `cui`                       | `(ci, cui):(id)`                                      | `ci:ruby_sword` or `cui:ruby_sword`                           |                                             |
| [MMOItems](https://gitlab.com/phoenix-dvpmt/mmoitems)                                                                                                 | `mmoi`     | `mmoitems`                                 | `(mmoi, mmoitems):(type):(id)[:level:tier]`           | `mmoi:sword:ruby_sword` or `mmoitems:sword:ruby_sword:1:rare` |                                             |
| [ExecutableBlocks](https://www.spigotmc.org/resources/custom-blocks-plugin-executable-blocks.93406/)                                                  | `eb`       | `executableblocks`                         | `(eb, executableblocks):(id)`                         | `executableblocks:ruby_block` or `eb:ruby_block`              |                                             |
| [ExecutableItems](https://www.spigotmc.org/resources/custom-items-plugin-executable-items.77578/)                                                     | `ei`       | `executableitems`                          | `(ei, executableitems):(id)`                          | `executableitems:ruby_sword` or `ei:ruby_sword`               |                                             |
| [MythicCrucible](https://mythiccraft.io/index.php?resources/crucible-custom-items-armor-furniture-blocks-more.2/)                                     | `crucible` | `mythicc`, `mcrucible`, `mythiccrucible`   | `(crucible, mythicc, mcrucible, mythiccrucible):(id)` | `crucible:ruby_sword` or `mcrucible:ruby_sword`               |                                             |
| [MythicMobs](https://www.spigotmc.org/resources/%E2%9A%94-mythicmobs-free-version-%E2%96%BAthe-1-custom-mob-creator%E2%97%84.5702/)                   | `mythic`   | `mythicmobs`, `mmobs`, `mythicm`           | `(mythic, mythicmobs, mmobs, mythicm):(id)`           | `mythic:ruby_sword` or `mythicmobs:ruby_sword`                |                                             |
| [StorageMechanic](https://www.spigotmc.org/resources/%E2%9A%A1storagemechanic%E2%9A%A1-mythicmobs-itemsadder-oraxen-more.112391/)                     | `sm`       | `storagem`, `smechanic`, `storagemechanic` | `(sm, storagem, smechanic, storagemechanic):(id)`     | `sm:backpack` or `storagem:backpack`                          |                                             |
| [CraftEngine](https://github.com/Xiao-MoMi/craft-engine)                                                                                              | `ce`       | `craftengine`, `craftE`, `cEngine`         | `(ce, craftengine, craftE, cEngine):(id)`             | `ce:ruby_sword` or `craftengine:ruby_sword`                   |                                             |
### More Examples

#### Compare adapters

##### It Is Similar
```java
public class PluginExample extends JavaPlugin {
    @Override
    public void onEnable() {
        Adapter.init(this);
        
        String adapterId = "mc:diamond";
        ItemStack item = new ItemStack(Material.DIAMOND);
        if (Adapter.isSimilar(adapterId, item)) { // The item is similar
            getLogger().info("The item is similar");
        }
        
        String adapterId2 = "mc:diamond_sword";
        String adapterId3 = "mc:diamond_sword[custom_name='\"weapon\"']";
        
        if (!Adapter.isSimilar(adapterId2, adapterId3)) { // The items aren't similar
            getLogger().info("The items aren't similar");
        }
    }
}
```
##### Any Similar
```java
public class PluginExample extends JavaPlugin {
    @Override
    public void onEnable() {
        Adapter.init(this);

        List<String> list = List.of("mc:diamond", "mc:diamond_sword", "mc:gold_sword");
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        
        Optional<String> similar = Adapter.anySimilar(list, item);
        if (similar.isPresent()) {
            getLogger().info("The item is similar to " + similar.get()); // The item is similar to mc:diamond_sword
        }
    }
}
```

### Do you want to test the library?

You can build and test the sample plugin included in the Gradle subproject named `plugin` to verify the adapter works with the supported compatibilities.

- Subproject: `plugin`
- Gradle task: `shadowJar`

You can use the minecraft commands to test the plugin:

- Base usage
  - /adapter <list|get|give|exist>
  - Quick example to get started: hold any item and run:
    - /adapter get hand

- /adapter list
  - Shows all available adapters with their type, name, and enabled state.
  - Example: /adapter list

- /adapter exist <adapter:id>
  - Checks whether a specific adapter id exists.
  - Example: /adapter exist mc:diamond_sword

- /adapter get hand
  - Players only. Prints the AdapterId and AdvancedId (if different) of the item in your main hand.
  - Example: /adapter get hand

- /adapter get block
  - Players only. Prints the AdapterId and AdvancedId (if different) of the block you are looking at (within 6 blocks).
  - Example: /adapter get block

- /adapter give <player> <adapter:id> [amount]
  - Gives the specified adapter item to a player. Amount is optional (default 1) and will be split into valid stacks automatically.
  - Examples:
    - /adapter give Steve mc:diamond_sword
    - /adapter give Steve mc:diamond 32

Tab completion:
- 1st arg: list, get, give, exist
- 2nd arg:
  - get: hand, block
  - give: online player names
  - exist: placeholder <adapter:id>
- 3rd arg (for give): dynamic suggestions of available adapter ids
- 4th arg (for give): 1, 16, 32, 64

