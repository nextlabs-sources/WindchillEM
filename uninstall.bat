set "CURRENT_DIR=%cd%"
windchill stop
windchill --classpath %CURRENT_DIR%\*;%CLASSPATH% com.nextlabs.Uninstall
call "removeNXLJars.bat"
del "removeNXLJars.bat"
xconfmanager -t codebase/wt.properties --undefine wt.services.service.8443 -p
