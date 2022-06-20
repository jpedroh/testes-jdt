package br.ufpe.cin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;

public class ProjectAstGenerator {
  public CompilationUnit getProjectAst(Path classPathEntries, Path sourcePathEntries, String sourceFilePath)
      throws IOException {
    final String javaCode = Files.readString(Paths.get(sourceFilePath));

    IDocument document = new Document(javaCode);

    ASTParser parser = ASTParser.newParser(AST.JLS8);
    Map<String, String> options = JavaCore.getOptions();
    JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
    parser.setCompilerOptions(options);
    parser.setEnvironment(new String[] { classPathEntries.toString() },
        new String[] { sourcePathEntries.toString()}, new String[]  { "UTF-8" }, true);
    parser.setUnitName("testing_jdt");

    parser.setSource(document.get().toCharArray());
    parser.setKind(ASTParser.K_COMPILATION_UNIT);
    parser.setResolveBindings(true);
    parser.setBindingsRecovery(true);

    return (CompilationUnit) parser.createAST(null);
  }
}
