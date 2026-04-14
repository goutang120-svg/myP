$ErrorActionPreference = "Stop"

Write-Host "Start system-service (local)..."
Start-Process powershell -ArgumentList @("-NoProfile","-Command","cd '$PSScriptRoot\..\system-service'; mvn -q -DskipTests spring-boot:run -Dspring-boot.run.profiles=local") -WorkingDirectory "$PSScriptRoot\..\system-service"

Write-Host "Start biz-service (local)..."
Start-Process powershell -ArgumentList @("-NoProfile","-Command","cd '$PSScriptRoot\..\biz-service'; mvn -q -DskipTests spring-boot:run -Dspring-boot.run.profiles=local") -WorkingDirectory "$PSScriptRoot\..\biz-service"

Write-Host "Start gateway (local)..."
Start-Process powershell -ArgumentList @("-NoProfile","-Command","cd '$PSScriptRoot\..\gateway'; mvn -q -DskipTests spring-boot:run -Dspring-boot.run.profiles=local") -WorkingDirectory "$PSScriptRoot\..\gateway"

Write-Host "Done."

