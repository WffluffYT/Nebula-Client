# Nebula Client

Purple & black Fabric client mod for Minecraft **26.2**.

## Build (needs JDK 25)
```
./gradlew build        # or: gradle wrapper && ./gradlew build
```
Jar lands in `build/libs/nebula-client-1.0.0.jar`. Drop it in your `mods` folder with Fabric Loader 0.19.5+ and Fabric API 0.161.0+26.2.
To test in dev: `./gradlew runClient`.

## Use
Press **Right Shift** to open the menu.
- Left-click a module = toggle
- Right-click a module = settings
- Right-click a panel header = collapse, drag header = move

## Modules
| Category | Module | What it does |
|---|---|---|
| Render | Xray | Hides everything except ores, ancient debris, chests/spawners |
| Render | Fullbright | Client-side night vision |
| Render | Zoom | Narrow FOV telescope |
| Performance | FPS Boost | Min particles, no clouds/shadows, smooth lighting off, biome blend off, unlimited FPS, VSync off, render distance slider. Restores your settings when turned off |
| Movement | Toggle Sprint | Auto-sprint while holding forward |
| HUD | FPS Counter, Coordinates, Keystrokes | Clean overlay pills |

## If it fails to compile
I couldn't compile against 26.2 where I wrote this, so a few Minecraft API names are best guesses.
Every one is tagged `VERSION-SENSITIVE (n)` in the source:
1. `gui/Draw.java` - draw-context class name (`GuiGraphicsExtractor` vs `GuiGraphics`)
2. `gui/Draw.java` - text call (`text` vs `drawString`)
3. `mixin/BlockMixin.java` - `Block.shouldRenderFace` signature
4. `gui/ClickGui.java` - screen render method (`extractRenderState` vs `render`)
5. `gui/ClickGui.java` - mouse callbacks (`MouseButtonEvent`)
6. `NebulaClient.java` - key mapping + category
7. `NebulaClient.java` - HUD registration

Xray note: it works on the vanilla renderer. With Sodium installed the face-culling hook needs a Sodium-specific mixin.
Most multiplayer servers ban Xray, so use it in singleplayer or on servers that allow it.
