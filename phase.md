# Pure-Java Voxel Engine — Development Roadmap

## Phase 1: The Core Engine
**Weeks 1–12**
**Goal:** Get a window on the screen and render a 3D-ish world without any GPU acceleration.

### Key Actions
- **The Renderer:** Create a `JFrame` and a `Canvas`. Override `paint(Graphics g)`. Use `Graphics2D` to draw polygons.
- **The Math Library:** Write your own `Vector3D`, `Matrix4x4` (for perspective projection), and `Quaternion` classes — Java doesn't have these built in.
- **Raycasting (Not Polygons):** Forget modern 3D rendering; pure Java AWT is too slow for textured triangles. Implement a Raycaster (like Wolfenstein 3D) or a Voxel heightmap renderer. Cast rays from the player's eye through the screen, calculate intersections with blocks, and draw vertical strips using `g.drawLine()` or `g.fillRect()`.
- **Input Handling:** Add a `KeyListener` and `MouseListener` to the Canvas. Implement "mouse grab" using `Robot` to hide the cursor and track infinite mouse movement.

---

## Phase 2: The Voxel World & Player
**Weeks 13–24**
**Goal:** Generate a world and let the player move through it.

### Key Actions
- **Data Structures:** Use a 3D `HashMap<Point3D, BlockType>` or a `short[]` array to store blocks. Pure Java has no built-in optimized 3D arrays, so you'll need custom hash functions for x, y, z.
- **Procedural Generation:** Use `java.util.Random` to implement Perlin/Simplex noise from scratch (port the mathematical formula into pure Java). Use this to generate terrain heights.
- **Player Physics:** Implement gravity using `double` velocities. Write your own Axis-Aligned Bounding Box (AABB) collision detection against the block grid.
- **Interaction:** Implement "Ray-Block Intersection" — cast a ray from the player's eye forward, step along it using a DDA (Digital Differential Analyzer) algorithm, and detect which block the player is looking at. Left-click = break, right-click = place.

---

## Phase 3: Gameplay & World Simulation
**Weeks 25–40**
**Goal:** Add inventory, crafting, and basic AI.

### Key Actions
- **Entity System:** Create a pure Java `Entity` class. For mobs, implement simple pathfinding using a BFS (Breadth-First Search) algorithm on your block grid — write the Queue and Node logic yourself using `java.util.ArrayList`, since you have no external AI libs.
- **Inventory:** Use `java.util.HashMap<Integer, ItemStack>` for the player's inventory. Since `org.json` is an external lib, save inventory data using `java.io` streams to write custom `.dat` files, or use `java.util.Properties` for simple key-value saves.
- **Crafting:** Hard-code a 2×2 or 3×3 crafting grid matching algorithm. Loop through recipes using nested for-loops to check if the placed items match the shape.
- **Sound:** No good sound support available (`javax.sound.sampled` exists but is notoriously terrible and blocking). Use `java.awt.Toolkit.getDefaultToolkit().beep()` for basic feedback, or load raw `.wav` files manually using `AudioInputStream` — expect significant latency.

---

## Phase 4: Chunk Management & Performance
**Weeks 41–55**
**Goal:** Make the world bigger than your RAM allows.

### Key Actions
- **Chunk System:** Split the world into 16×16×64 chunks. Write your own `Chunk` class that stores blocks in a flat `byte[]` array.
- **Threading:** Use `java.lang.Thread` and `ConcurrentHashMap` to load/unload chunks in the background. Since OpenGL isn't available, rendering must happen on the Event Dispatch Thread (EDT) — use `SwingUtilities.invokeLater()` to pass rendered frames to the canvas.
- **Frustum Culling:** Before drawing anything, check if a block is inside the player's field of view using your custom math library, to avoid drawing behind the player.

---

## Phase 5: The "Nether" & Persistence
**Weeks 56–70**
**Goal:** Add new dimensions and save the world.

### Key Actions
- **Dimension Hopping:** When the player builds a portal, change a `dimensionID` variable. Clear the current world and generate a new one (Hell biome with lava) using a different noise seed.
- **World Saving:** Write your own binary serializer using `java.io.DataOutputStream`. Loop through every block in every chunk and write its byte ID to a `.minecraft`-style region file on disk.
- **Lighting:** Implement a flood-fill lighting algorithm on the CPU. When a block is placed, recursively check all 6 neighboring blocks to spread light levels (this will be slow — limit it to a 10-block radius).

---

## Phase 6: Polish, UI & Final Delivery
**Weeks 71–100**
**Goal:** Turn the barebones engine into a playable game.

### Key Actions
- **HUD:** Draw the health bar, hunger bar, and hotbar using `Graphics2D` shapes and `Font` rendering. Create a "pixel-art" font using `g.drawString()` of bitmap characters loaded from a text file.
- **Inventory Screen:** Create a modal `JDialog` that overlays the Canvas. Pause the game loop, capture mouse clicks on the dialog's custom-drawn slots, and move items between arrays.
- **Optimization (The CPU Struggle):** Since everything runs on the CPU, implement a "dirty chunk" system — only redraw a chunk if a block inside it changed. Cache the rendered image of each chunk in a `BufferedImage` so you don't recalculate perspective every frame.
- **The "End" Boss:** Create a simple "dragon" entity that flies in a sine-wave pattern using `Math.sin()`. Spawn Endermen that teleport by simply changing their x, y, z coordinates instantly.
