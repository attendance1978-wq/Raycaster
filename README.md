# 🎮 Raycaster Engine - 3.28 ft Block Renderer

A Minecraft-style 3D raycasting engine built entirely in pure Java using AWT/Swing. This engine renders 3.28 feet (1 meter) blocks with full texture mapping, similar to Minecraft's block rendering system.

![Version](https://img.shields.io/badge/version-2.0.0-blue.svg)
![Java](https://img.shields.io/badge/java-8+-orange.svg)
![License](https://img.shields.io/badge/license-MIT-green.svg)
![Platform](https://img.shields.io/badge/platform-Windows%20%7C%20Linux%20%7C%20Mac-lightgrey.svg)

## 📸 Preview
```
┌─────────────────────────────────────────────────────────────┐
│ ╔═══════════════════════════════════════════════════════╗   │
│ ║                                           3D VIEWPORT ║   │
│ ║                                                       ║   │
│ ║                                         ┌───────────┐ ║   │
│ ║                                         │ TEXTURE   │ ║   │
│ ║                                         │ ON WALL   │ ║   │
│ ║                                         └───────────┘ ║   │
│ ║                                                       ║   │
│ ║  [Minimap]     ┌──┐                                   ║   │
│ ║ ■■■■■■ │ │ [Crosshair]                                ║   │
│ ║ ■ ■            └──┘                                   ║   │
│ ║ ■■■■■■                                                ║   │
│ ║                                                       ║   │
│ ║ [Weapon]                                              ║   │
│ ║ ┌──┐                                                  ║   │
│ ║ │ │                                                   ║   │
│ ║ └──┘                                                  ║   │
│ ╚═══════════════════════════════════════════════════════╝   │
│ FPS: 60 | Pos: 10.5, 10.5 | Block: 3.28 ft                  │
│ WASD: Move | Mouse: Look | T: Texture | C: Random Map       │
└─────────────────────────────────────────────────────────────┘
```

## ✨ Features

### 🎯 Core Features
- **3.28 Feet (1 Meter) Block Rendering** - Minecraft-style block size
- **Full Texture Mapping** - Each block face displays the complete texture
- **UV Coordinate System** - Proper texture coordinates (0.0 to 1.0)
- **Distance-Based Shading** - 3D depth perception with lighting
- **60 FPS Target** - Smooth gameplay with fixed timestep

### 🎨 Visual Features
- **Texture Support** - Custom textures from `assets/image/image.png`
- **Fallback Texture** - Built-in Minecraft-style texture if image not found
- **Solid Floor** - Clean gray floor without sky
- **Minimap** - Top-down view with colored walls
- **View Bobbing** - Natural head movement when walking
- **Weapon Display** - Simple weapon model with bobbing

### 🎮 Controls
| Key | Action |
|-----|--------|
| `W` `A` `S` `D` | Move player (forward, left, backward, right) |
| `Mouse` | Look around (click to grab) |
| `ENTER` | Grab mouse |
| `ESC` | Release mouse |
| `T` | Toggle textures ON/OFF |
| `C` | Generate random map |
| `R` | Reset player position |
| `F` | Toggle FPS display |

### 🖥️ HUD Display
- **FPS Counter** - Real-time frame rate
- **Player Position** - X, Y coordinates
- **Player Angle** - Rotation in radians
- **Block Size** - 3.28 ft (1 meter) display
- **Texture Status** - ON/OFF indicator
- **Controls Guide** - Always visible

## 📁 Project Structure
```
RaycasterEngine/
├── assets/
│   └── image/
│       └── image.png          # Your texture image
├── BlockTexture.java          # Texture loader
├── ImageRender.java           # 3.28 ft block renderer
├── Map.java                   # Map data and collision
├── Player.java                # Player movement and controls
├── RaycasterEngine.java       # Main game engine
├── out/                       # Compiled .class files
├── README.md                  # This file
├── CHANGELOG.md               # Version history
├── CONTRIBUTORS.md            # Contributing guide
├── LICENSE                    # MIT License
├── build.bat                  # Windows build script
├── build.sh                   # Linux/Mac build script
├── run.bat                    # Windows run script
├── run.sh                     # Linux/Mac run script
└── restart.bat                # Clean, compile, and run
```

## 🚀 Getting Started

### Prerequisites
- **Java JDK 8 or higher** - Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/)
- **Git** (optional) - For cloning the repository

### Installation

#### 1. Clone or Download
```bash
git clone https://github.com/attendance1978-wq/Raycaster.git
cd Raycaster
```

#### 2. Add Your Texture
Place your texture image at `assets/image/image.png`:

```bash
mkdir -p assets/image
# Copy your image.png to assets/image/
```

#### 3. Compile
**Windows:**
```bash
build.bat
```

**Linux/Mac:**
```bash
chmod +x build.sh
./build.sh
```

#### 4. Run
**Windows:**
```bash
run.bat
```

**Linux/Mac:**
```bash
chmod +x run.sh
./run.sh
```

### Quick Start (One Command)
**Windows:**
```bash
restart.bat
```

**Linux/Mac:**
```bash
./restart.bat
```

## 🎮 How to Play
1. **Start the game** - Run `restart.bat` or `java -cp out RaycasterEngine`
2. **Grab mouse** - Press `ENTER` or click in the window
3. **Move around** - Use `WASD` keys
4. **Look around** - Move your mouse
5. **Toggle textures** - Press `T` to turn textures on/off
6. **Generate new map** - Press `C` for a random map
7. **Reset position** - Press `R` if you get stuck
8. **Release mouse** - Press `ESC`

## 🎨 Customizing Textures

### Adding Your Own Texture
1. Create your texture - Any size (recommended: 64x64, 128x128, 256x256)
2. Save as PNG - `assets/image/image.png`
3. Run the game - Your texture will appear on all walls

### Texture Tips
- Use PNG format for best quality
- Square textures work best (64x64, 128x128, etc.)
- Minecraft texture packs are compatible
- The texture wraps around each block face

## 🔧 Technical Details

### Rendering Pipeline
1. Clear screen - Fill with floor color
2. Cast rays - For each screen column
3. DDA algorithm - Find wall intersections
4. Calculate distance - Perpendicular distance
5. Calculate wall height - 3.28 feet blocks
6. Texture mapping - UV coordinates (0.0 to 1.0)
7. Apply shading - Distance-based darkness
8. Draw pixels - Fill the vertical strip
9. Swap buffers - Display the frame

### Performance
- Target FPS: 60
- Resolution: 1024x768
- Rendering: Pure Java AWT (no GPU)
- Memory: ~50-100 MB
- CPU: Single-threaded

## 🐛 Troubleshooting

### Common Issues

**Texture not loading**
```
Problem: Walls show solid colors instead of texture
Solution: 
1. Check if assets/image/image.png exists
2. Check console for error messages
3. Press T to toggle texture on/off
4. Use the fallback texture
```

**Mouse not working**
```
Problem: Can't look around with mouse
Solution:
1. Press ENTER to grab mouse
2. Click in the window
3. Check if mouse is captured (crosshair cursor)
4. Press ESC to release and try again
```

**Compilation errors**
```
Problem: javac not found or compilation fails
Solution:
1. Install Java JDK 8+
2. Add Java to PATH
3. Use full path to javac
4. Check for typos in code
```

## 🔄 Version History
See [CHANGELOG.md](CHANGELOG.md) for detailed version history.

## 🤝 Contributing
We welcome contributions! See [CONTRIBUTORS.md](contribution.md) for guidelines.

## 📄 License
This project is licensed under the MIT License - see the [LICENSE](license) file for details.

## 🙏 Acknowledgments
- **Lode Vandevenne** - Original raycasting tutorial
- **Minecraft** - Block rendering inspiration
- **Wolfenstein 3D** - Original raycasting game
- **Java AWT/Swing** - Rendering framework

## 📞 Support
- Issues: GitHub Issues
- Email: your.email@example.com

---

Made with ❤️ and Java
Last Updated: 2026-08-27
