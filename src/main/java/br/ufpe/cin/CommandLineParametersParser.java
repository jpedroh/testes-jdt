package br.ufpe.cin;

import java.nio.file.Path;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

public class CommandLineParametersParser {
  @Builder
  @AllArgsConstructor
  @ToString
  public static class CommandLineParameters {
    public final Path projectPath;
    public final Path classTargetPath;
    public final Path sourcePath;
    public final Path targetClassPath;
    public final String targetMethodName;
    public final String commitHash;
  }

  private final Options options;
  private final CommandLineParser parser;
  private final HelpFormatter formatter;

  private static final String projectPathTag = "p";
  private static final String classTargetPathTag = "t";
  private static final String sourcePathTag = "s";
  private static final String targetClassPathTag = "f";
  private static final String targetMethodNameTag = "m";
  private static final String commitHashTag = "c";
  private static final String helpTag = "h";

  public CommandLineParametersParser() {
    options = new Options();
    parser = new DefaultParser();
    formatter = new HelpFormatter();

    Option projectPathOption = Option.builder()
        .option(projectPathTag)
        .longOpt("projectPath")
        .hasArg()
        .desc("Project path")
        .required()
        .build();
    Option classTargetPathOption = Option.builder()
        .option(classTargetPathTag)
        .longOpt("classTargetPath")
        .hasArg()
        .desc("Class Target path")
        .required()
        .build();
    Option sourcePathOption = Option.builder()
        .option(sourcePathTag)
        .longOpt("sourcePath")
        .hasArg()
        .desc("Source code path")
        .required()
        .build();
    Option targetClassPathOption = Option.builder()
        .option(targetClassPathTag)
        .longOpt("targetClassPath")
        .hasArg()
        .desc("Target Class Path")
        .required()
        .build();
    Option targetMethodNameOption = Option.builder()
        .option(targetMethodNameTag)
        .longOpt("targetMethodName")
        .hasArg()
        .desc("Target Method Name")
        .required()
        .build();
    Option commitHashOption = Option.builder()
        .option(commitHashTag)
        .longOpt("commitHash")
        .hasArg()
        .desc("Commit Hash")
        .required()
        .build();
    Option helpOption = Option.builder()
        .option(helpTag)
        .longOpt("help")
        .desc("Shows this help message")
        .build();

    options
        .addOption(projectPathOption)
        .addOption(classTargetPathOption)
        .addOption(sourcePathOption)
        .addOption(targetClassPathOption)
        .addOption(targetMethodNameOption)
        .addOption(commitHashOption)
        .addOption(helpOption);
  }

  public CommandLineParameters parse(String[] args) {
    try {
      CommandLine cmd = parser.parse(options, args);

      if (cmd.hasOption("help")) {
        formatter.printHelp("", options);
        return null;
      }

      final Path projectPath = Path.of(cmd.getOptionValue(projectPathTag));
      final Path targetClassPath = projectPath.relativize(Path.of(cmd.getOptionValue(targetClassPathTag)));

      return CommandLineParameters
          .builder()
          .projectPath(projectPath)
          .classTargetPath(Path.of(cmd.getOptionValue(classTargetPathTag)))
          .sourcePath(Path.of(cmd.getOptionValue(sourcePathTag)))
          .targetClassPath(targetClassPath)
          .targetMethodName(cmd.getOptionValue(targetMethodNameTag))
          .commitHash(cmd.getOptionValue(commitHashTag))
          .build();
    } catch (ParseException e) {
      formatter.printHelp("OSean.EX", options);
      throw new IllegalArgumentException(e.getMessage());
    }
  }
}