# Installation
- You can manually build bpkgs from source, you need to install kotlinc-native
```bash
# For arch linux:
paru -S kotlin-native
# or
yay -S kotlin-native
```
- Next clone the repository and compile it
```bash
git clone https://github.com/Milkiway13/bpkgs
cd bpkgs
kotlinc-native main.kt -o bpkg
```
- And finally make it executable and add it to /usr/bin
```bash
chmod +x bpkg.kexe
sudo mv bpkg.kexe /usr/bin/bpkg
```
- If you don't wanna compile it or you can't install kotlinc-native, you can just download the binary from the releases page, make it executable and move to /usr/bin
# Usage
- To use it you simply run this:
```bash
bpkg <flag> <package_manager>/<package>
```
### Flags
| Flag | What it does |
| ---- | ------------ |
| ```-S``` | Install package |
| ```-Sy``` | Sync repos and install package |
| ```-R``` | Remove package |

#### Flags supported(excluding -S/-R)
| Flag | Xbps | Flatpak | Apk | 
| ---- | ---- | ------- | --- |
|  -Sy | Yes  |   No    | No  |

###### Note
Pacman/Paru/Yay aren't listed since they suport every flag

# Supported package managers
- [X] Pacman
- [X] XBPS
- [X] apk
- [X] flatpak
- [ ] nix
- [ ] apt (coming in 26.3)
- [ ] paru/yay (coming in 26.3)
- [ ] dnf (coming in 26.3)
- [ ] emerge (coming in 26.3)
- [ ] zypper
