@ECHO OFF

SET CP="%MIS_HOME%\libs\commons.jar"
SET CP="%MIS_HOME%\libs\inss.jar"
SET CP=%CP%;"%MIS_HOME%\libs\jabuti.jar"
SET CP=%CP%;"%MIS_HOME%\libs\bcel-5.2.jar"
SET CP=%CP%;"%MIS_HOME%\libs\commons-cli-1.2.jar"
SET CP=%CP%;"%MIS_HOME%\libs\log4j-1.2.16.jar"
java -cp %CP% br.usp.each.inss.bytecode.Instrumenter %*