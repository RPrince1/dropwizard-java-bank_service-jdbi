@echo on
../gradlew clean build jarIt
IF ERRORLEVEL 1 goto RedBuild
IF ERRORLEVEL 0 goto GreenBuild

:RedBuild
color 4f
type tools\buildflags\dos_failed.txt

goto TheEnd

:GreenBuild
color 2F
type tools\buildflags\dos_passed.txt

:TheEnd
pause

color