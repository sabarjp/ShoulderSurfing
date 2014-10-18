start /WAIT cmd /c recompile.bat 

start /WAIT cmd /c reobfuscate.bat 

xcopy .\META-INF .\reobf\minecraft\META-INF /S /Y /I
del /Q .\reobf\minecraft\*.class

cd reobf\minecraft\
zip -D -r -m ..\..\jars\coremods\sabar.jar .

cd ..\..

startclient.bat
pause
