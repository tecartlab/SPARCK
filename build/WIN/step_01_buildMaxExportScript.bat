@Echo OFF

REM Copy JRE folder to SPARCK app
REM xcopy /s %~dp0\..\Bundle\maxScriptForExport.txt %~dp0\step_02_maxScriptForExport.txt

powershell -Command "(gc %~dp0\..\Bundle\maxScriptForExport.txt) -replace '<replacePath>', %~dp0\..\ | Out-File -encoding ASCII %~dp0\step_02_maxScriptForExport.txt"

REM Exit