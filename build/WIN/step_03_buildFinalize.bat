@Echo OFF

REM Copy JRE folder to SPARCK app
xcopy /s "C:\Program Files\Java\jdk-13.0.2" %~dp0\build\SPARCK\jre
rmdir /s /q %~dp0\build\SPARCK\jre\jmods
rmdir /s /q %~dp0\build\SPARCK\jre\include
rmdir /s /q %~dp0\build\SPARCK\jre\legal
del %~dp0\build\SPARCK\jre\lib\ct.sym
del %~dp0\build\SPARCK\jre\lib\src.zip

REM Remove unused files...
rmdir /s /q %~dp0\build\SPARCK\resources\support\CEF
rmdir /s /q %~dp0\build\SPARCK\resources\java-classes\classes
rmdir /s /q %~dp0\build\SPARCK\resources\packages\max-mxj\examples
rmdir /s /q %~dp0\build\SPARCK\resources\packages\max-mxj\help
rmdir /s /q %~dp0\build\SPARCK\resources\packages\max-mxj\java-doc
rmdir /s /q %~dp0\build\SPARCK\resources\packages\max-mxj\misc
rmdir /s /q %~dp0\build\SPARCK\resources\packages\max-mxj\script
rmdir /s /q %~dp0\build\SPARCK\resources\packages\max-mxj\source
rmdir /s /q %~dp0\build\SPARCK\resources\packages\max-mxj\sysbuild
rmdir /s /q %~dp0\build\SPARCK\resources\packages\max-mxj\testing
rmdir /s /q %~dp0\build\SPARCK\resources\packages\max-mxj\.git
del %~dp0\build\SPARCK\resources\packages\max-mxj\*.*

REM Change menu...
xcopy %~dp0..\Bundle\maxinterface.json %~dp0\build\SPARCK\resources\interfaces\maxinterface.json

REM Add Java Lib...
xcopy %~dp0..\..\sparck\java-classes\lib %~dp0\build\SPARCK\resources\java-classes\lib
rmdir /s /q %~dp0\build\SPARCK\resources\java-classes\lib\opencv
del %~dp0\build\SPARCK\resources\java-classes\lib\*.dylib

REM Set Java Initialization...
xcopy %~dp0\Bundle\max.java.config.txt %~dp0\build\SPARCK\resources\java-classes

REM Copy Shaders...
xcopy %~dp0..\..\sparck\misc\shaders %~dp0\build\SPARCK\resources\media\jitter\shaders\shaders


REM Change Max Console -> Sparck Console inside /resources/interfaces/maxinterface.json Line 2849
REM Remove customeditmenu inside /resources/interfaces/maxinterface.json Line 3629 + 3630
REM use http://jsonviewer.stack.hu/ to look at maxinterface.json

REM Exit