package br.ufpe.cin;

import java.io.File;
import java.nio.file.Path;

public class JavaProjectUtils {
  public static Path qualifiedNameToPath(String qualifiedName) {
    return Path.of(qualifiedName.replace(".", File.separator) + ".java");
  }
}
