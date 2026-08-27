# Changelog

All notable changes to the Raycaster Engine project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.0.0] - 2026-08-27

### Added
- **ImageRender.java** - New rendering engine for 3.28 feet (1 meter) block textures
  - Full texture mapping with UV coordinates (0.0 to 1.0)
  - Minecraft-style block rendering with complete texture on each face
  - Distance-based shading for 3D depth perception
  - Fallback texture generation (Minecraft-style grass/dirt block)
  - Block size constant: 3.28 feet = 1 meter

- **Texture System**
  - Single texture support from `assets/image/image.png`
  - Automatic fallback texture if image not found
  - Texture loading status display in HUD

- **HUD Enhancements**
  - Block size information display (3.28 ft / 1 meter)
  - Texture status (ON/OFF) indicator
  - Texture info (size and load status)
  - Player position and angle tracking

- **Controls**
  - `T` - Toggle textures ON/OFF
  - `C` - Generate random map
  - `R` - Reset player position
  - `F` - Toggle FPS display
  - `ENTER` - Grab mouse
  - `ESC` - Release mouse
  - `WASD` - Movement

### Changed
- **Complete code restructure**
  - Separated texture rendering into dedicated `ImageRender.java`
  - Improved texture coordinate calculation for full block display
  - Enhanced raycasting algorithm for better performance
  - Updated `Map.java` for better texture handling

- **Visual Improvements**
  - Solid gray floor instead of gradient
  - Minecraft-style block rendering
  - Better distance shading
  - Full texture display on each block face

### Fixed
- Texture not displaying on walls issue
- Variable naming conflicts in rendering methods
- Texture coordinate calculation for proper UV mapping
- Mouse look sensitivity and accuracy
- Screen flickering with proper buffer strategy

### Removed
- Sky gradient (replaced with solid floor)
- Multiple texture types (now uses single texture for all blocks)
- Unused color palette textures

---

## [1.5.0] - 2026-08-26

### Added
- **BlockTexture.java** - Initial texture loading system
- Multiple texture paths support
- Checkerboard fallback texture
- Basic texture mapping

### Changed
- Updated raycasting for better wall detection
- Improved collision detection
- Optimized rendering performance

### Fixed
- Map display issues
- Wall rendering problems
- Memory leak issues

---

## [1.0.0] - 2026-08-25

### Added
- **Initial Release**
  - Basic raycasting engine
  - Wolfenstein 3D-style rendering
  - JFrame and Canvas implementation
  - Basic player movement (WASD)
  - Mouse look support
  - Minimap display
  - Colored walls with distance shading
  - View bobbing
  - Weapon display
  - FPS counter

### Technical Features
- Pure Java AWT rendering
- Double buffering (BufferStrategy)
- DDA (Digital Differential Analyzer) raycasting
- Collision detection
- 60 FPS target with fixed timestep
- Robot class for mouse grabbing

### Controls
- `WASD` - Move player
- `Mouse` - Look around
- `ENTER` - Grab mouse
- `ESC` - Release mouse
- `F` - Toggle FPS display
- `R` - Reset player position

---

## [0.5.0] - 2026-08-20

### Added
- Initial prototype
- Basic rendering test
- Movement system prototype

---

## Upcoming Features

### Planned for v2.1.0
- [ ] Multiple texture support (different textures for different blocks)
- [ ] Dynamic lighting system
- [ ] Day/night cycle
- [ ] Sound effects (Java Sound API)
- [ ] Menu system
- [ ] Settings configuration

### Planned for v2.2.0
- [ ] Enemy NPCs with AI
- [ ] Shooting mechanics
- [ ] Health system
- [ ] Inventory system
- [ ] Doors and interactive objects
- [ ] Item pickup

### Planned for v3.0.0
- [ ] Multiplayer support (TCP/WebSocket)
- [ ] Custom map editor
- [ ] Texture pack support
- [ ] Modding API
- [ ] Save/Load game states
- [ ] Cross-platform support

---

## Version Naming Convention

- **Major version (X.0.0)** - Breaking changes, major features, complete rewrites
- **Minor version (0.X.0)** - New features, enhancements, non-breaking changes
- **Patch version (0.0.X)** - Bug fixes, performance improvements, minor updates

---

*Last Updated: 2026-08-27*