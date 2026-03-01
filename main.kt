import kotlinx.cinterop.*
import platform.posix.*

@OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)
fun main(args: Array<String>) {
  if (args.size < 2) {
    println("Usage: bpkg <action> <package manager>/<package>")
    return
  }

  val config = loadConfig()

  val sudo_command = config["sudo_command"] ?: "sudo"

  val action = args[0]
  val mgr_pkg = args[1].split("/")

  var pkg_mgr = ""
  var pkg = ""
  var emerge_cat = ""

  when (mgr_pkg.size) {
    2 -> {
      pkg_mgr = mgr_pkg[0]
      pkg = mgr_pkg[1]
    }
    3 -> {
      pkg_mgr = mgr_pkg[0]
      emerge_cat = mgr_pkg[1]
      pkg = mgr_pkg[2]
    }
    else -> {
      println("Error: Expected format 'mgr/pkg' or 'emerge/cat/pkg")
      return
    }
  }

  when (pkg_mgr) {
    "pacman" -> {
      system("$sudo_command pacman $action $pkg")
    }
    "paru" -> {
      system("paru $action $pkg")
    }
    "yay" -> {
      system("yay $action $pkg")
    }
    "xbps" -> {
      when (action) {
        "-S" -> {
          system("$sudo_command xbps-install $pkg")
        }
        "-R" -> {
          system("$sudo_command xbps-remove $pkg")
        }
        "-Sy" -> {
          system("$sudo_command xbps-install -S $pkg")
        }
      }
    }
    "apk" -> {
      when (action) {
        "-S" -> {
          system("$sudo_command apk add $pkg")
        }
        "-R" -> {
          system("$sudo_command apk del $pkg")
        }
      }
    }
    "flatpak" -> {
      when (action) {
        "-S" -> {
          system("flatpak install $pkg")
        }
        "-R" -> {
          system("flatpak uninstall $pkg")
        }
      }
    }
    "emerge" -> {
      when (action) {
        "-S" -> {
          system("$sudo_command emerge -a $emerge_cat/$pkg")
        }
        "-R" -> {
          system("$sudo_command emerge -a -c $emerge_cat/$pkg")
        }
      }
    }
    "apt" -> {
      when (action) {
        "-S" -> {
          system("$sudo_command apt install $pkg")
        }
        "-R" -> {
          system("$sudo_command apt remove $pkg")
        }
      }
    }
    "dnf" -> {
      when (action) {
        "-S" -> {
          system("$sudo_command dnf install $pkg")
        }
        "-R" -> {
          system("$sudo_command dnf remove $pkg")
        }
      }
    }
  }
}

@OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)
fun loadConfig(): Map<String, String> {
  val home = getenv("HOME")?.toKString() ?: return emptyMap()
  val path = "$home/.config/bpkg/config"

  val file = fopen(path, "r") ?: return emptyMap()

  val configMap = mutableMapOf<String, String>()

  try {
    memScoped {
      val buffer = allocArray<ByteVar>(1024)

      while (fgets(buffer, 1024, file) != null) {
        val line = buffer.toKString().trim()

        if (line.isEmpty() || line.startsWith("#")) continue

        val parts = line.split("=", limit = 2)
        if (parts.size == 2) {
          val key = parts[0].trim()
          val value = parts[1].trim()
          configMap[key] = value
        }
      }
    }
  } finally {
    fclose(file)
  }

  return configMap
}
