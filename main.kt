import kotlinx.cinterop.*
import platform.posix.*

@OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)
fun main(args: Array<String>) {
  if (args.size < 2) {
    println("Usage: bpkg <action> <package manager>/<package>")
    return
  }

  val action = args[0]
  val mgr_pkg = args[1].split("/")

  var pkg_mgr = ""
  var pkg = ""

  if (mgr_pkg.size == 2) {
    pkg_mgr = mgr_pkg[0]
    pkg = mgr_pkg[1]
  } else {
    println("Error: you haven't written package manager or package")
  }

  if (pkg_mgr == "pacman") {
    system("sudo pacman $action $pkg")
  } else if (pkg_mgr == "xbps") {
    if (action == "-S") {
      system("sudo xbps-install $pkg")
    } else if (action == "-R") {
      system("sudo xbps-remove $pkg")
    } else if (action == "-Sy") {
      system("sudo xbps-install -S $pkg")
    } else {
      println("Error: Unsupported/Non-existing flag")
    }
  } else if (pkg_mgr == "apk") {
    if (action == "-S") {
      system("sudo apk add $pkg")
    } else if (action == "-R") {
      system("sudo apk remove $pkg")
    } else {
      println("Error: Unsupported/Non-existing flag")
    }
  } else if (pkg_mgr == "flatpak") {
    if (action == "-S") {
      system("flatpak install $pkg")
    } else if (action == "-R") {
      system("flatpak uninstall $pkg")
    } else {
      println("Error: Unsupported/Non-existing flag")
    }
  }
}
