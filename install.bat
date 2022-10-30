mode con: cols=160 lines=75
set "CURRENT_DIR=%cd%"
windchill stop
::if not exist com\ptc\windchill\enterprise\attachments\server\ mkdir com\ptc\windchill\enterprise\attachments\server\
::xcopy *.class com\ptc\windchill\enterprise\attachments\server\
::RMDIR /S /Q com
::jar uf C:\ptc\Windchill_10.2\Windchill\codebase\WEB-INF\lib\wncWeb.jar com\ptc\windchill\enterprise\attachments\server\*
windchill --classpath %CURRENT_DIR%\*;%CLASSPATH% com.nextlabs.Install
windchill com.nextlabs.em.windchill.tools.SetQueryAgentUser
xconfmanager -t codebase/wt.properties -s wt.services.service.8443=com.nextlabs.em.windchill.listener.ListenerService/com.nextlabs.em.windchill.listener.EntitlementManagerListenerService -p

