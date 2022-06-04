package br.ufpe.cin;

import java.nio.file.Path;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;

public class AstGenerator {
  public CompilationUnit getAst(Path projectRoot, String packageName, String fileName) throws Exception {
    return new SourceRoot(projectRoot, createParserConfiguration(projectRoot))
        .parse(packageName, fileName);
  }

  private ParserConfiguration createParserConfiguration(Path projectRoot) throws Exception {
    TypeSolver typeSolver = new CombinedTypeSolver(
        new ReflectionTypeSolver(false),
        new JavaParserTypeSolver(projectRoot));

    return new ParserConfiguration().setSymbolResolver(new JavaSymbolSolver(typeSolver));
  }
}
