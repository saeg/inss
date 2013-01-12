@ECHO OFF

SET CP=%CP%;"%MIS_HOME%\libs\jabuti.jar"
SET CP=%CP%;"%MIS_HOME%\libs\bcel-5.2.jar"
SET CP=%CP%;"%MIS_HOME%\libs\gxl.jar"
SET CP=%CP%;"%MIS_HOME%\libs\j2gxl.jar"
SET CP=%CP%;"%MIS_HOME%\libs\commons.jar"
SET CP=%CP%;"%MIS_HOME%\libs\commons-cli-1.2.jar"
java -cp %CP% br.usp.each.j2gxl.command.AllUses %*