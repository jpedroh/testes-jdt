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

public class CommandLineParametersParser {
  @Builder
  @AllArgsConstructor
  public static class CommandLineParameters {
    public final Path projectPath;
    public final Path classTargetPath;
    public final Path sourcePath;
    public final String targetClassPath;
    public final String targetMethodName;
    public final String leftCommitHash;
    public final String rightCommitHash;
  }

  private final Options options;
  private final CommandLineParser parser;
  private final HelpFormatter formatter;

  private static final String projectPathTag = "p";
  private static final String classTargetPathTag = "c";
  private static final String sourcePathTag = "s";
  private static final String targetClassPath = "f";
  private static final String targetMethodNameTag = "m";
  private static final String helpTag = "h";
  private static final String leftCommitHashTag = "l";
  private static final String rightCommitHashTag = "r";

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
        .option(targetClassPath)
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
    Option leftCommitHashOption = Option.builder()
        .option(leftCommitHashTag)
        .longOpt("leftCommitHash")
        .hasArg()
        .desc("Left Commit Hash")
        .required()
        .build();
    Option rightCommitHashOption = Option.builder()
        .option(rightCommitHashTag)
        .longOpt("rightCommitHash")
        .hasArg()
        .desc("Right Commit Hash")
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
        .addOption(leftCommitHashOption)
        .addOption(rightCommitHashOption)
        .addOption(helpOption);
  }

  public CommandLineParameters parse(String[] args) {
    try {
      CommandLine cmd = parser.parse(options, args);

      if (cmd.hasOption("help")) {
        formatter.printHelp("", options);
        return null;
      }

      return CommandLineParameters
          .builder()
          .projectPath(Path.of(cmd.getOptionValue(projectPathTag)))
          .classTargetPath(Path.of(cmd.getOptionValue(classTargetPathTag)))
          .sourcePath(Path.of(cmd.getOptionValue(sourcePathTag)))
          .targetClassPath(cmd.getOptionValue(targetClassPath))
          .targetMethodName(cmd.getOptionValue(targetMethodNameTag))
          .leftCommitHash(cmd.getOptionValue(leftCommitHashTag))
          .rightCommitHash(cmd.getOptionValue(rightCommitHashTag))
          .build();
    } catch (ParseException e) {
      formatter.printHelp("OSean.EX", options);
      throw new IllegalArgumentException(e.getMessage());
    }
  }
}