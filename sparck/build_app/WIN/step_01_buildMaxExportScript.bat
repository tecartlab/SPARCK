@Echo OFF

setlocal
for %%B in (%~dp0\.) do set parent=%%~dpB
ECHO parent=%parent%
FOR %%a IN ("%parent:~0,-1%") DO SET grandparent=%%~dpa
ECHO grandparent=%grandparent%

powershell -Command "(gc %~dp0\..\Bundle\maxScriptForExport.txt) -replace '<replacePath>', '%grandparent%' | Out-File -encoding ASCII %~dp0\step_02_maxScriptForExport.txt"

powershell -Command "(gc %~dp0\step_02_maxScriptForExport.txt) -replace '\\', '/' | Out-File -encoding ASCII %~dp0\step_02_maxScriptForExport.txt"

powershell -Command "(gc %~dp0\step_02_maxScriptForExport.txt) -replace 'SPARCK.icns', 'SPARCK.ico' | Out-File -encoding ASCII %~dp0\step_02_maxScriptForExport.txt"

powershell -Command "(gc %~dp0\step_02_maxScriptForExport.txt) -replace '_onlyOSX', '_onlyWIN' | Out-File -encoding ASCII %~dp0\step_02_maxScriptForExport.txt"

REM Exit