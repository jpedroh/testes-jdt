package br.ufpe.cin;

import java.io.File;
import java.nio.file.Paths;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;

public class TestUtils {
  public static final String TEST_PROJECT_PATH = "src/test/resources/test-resource/src/main/java";

  private static ParserConfiguration createParserConfiguration() throws Exception {
    TypeSolver typeSolver = new CombinedTypeSolver(
        new ReflectionTypeSolver(false),
        new JavaParserTypeSolver(new File(TEST_PROJECT_PATH)));

    return new ParserConfiguration().setSymbolResolver(new JavaSymbolSolver(typeSolver));
  }

  public static CompilationUnit compileTestingCode() {
    try {
      return new SourceRoot(Paths.get(TEST_PROJECT_PATH), createParserConfiguration())
          .parse("br.ufpe.cin", "App.java");
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static MethodDeclaration getMethodDeclaration() {
    CompilationUnit compilationUnit = compileTestingCode();
    return new MethodDeclarationFinder().getMethodBlockFromTree(compilationUnit, "main");
  }
}
