@echo off
echo Building Slay the Spire Board Game Mod...
echo Running unit tests and packaging...
mvn clean package
if %ERRORLEVEL% EQU 0 (
    echo.
    echo Build successful! The mod has been copied to:
    echo D:\SteamLibrary\steamapps\common\SlayTheSpire\mods\sts_tbg_tvg.jar
    echo.
    echo You can now launch Slay the Spire with ModTheSpire to use the mod.
) else (
    echo.
    echo Build failed. Check the errors above.
)
pause
