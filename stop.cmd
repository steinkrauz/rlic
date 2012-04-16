@echo off
call settings.cmd
java -cp .;rlicserver-1.2.jar com.scac.RLicServer.RLicAdmin shutdown %RHOST% %RPORT%
