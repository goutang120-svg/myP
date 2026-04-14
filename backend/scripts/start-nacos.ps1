$ErrorActionPreference = "Stop"

param(
  [Parameter(Mandatory=$true)][string]$Namespace,
  [string]$NacosAddr = "62.234.1.152:8848",
  [string]$Group = "STUDENT_MAGE",
  [string]$Username = "",
  [string]$Password = ""
)

$env:NACOS_ADDR = $NacosAddr
$env:NACOS_NAMESPACE = $Namespace
$env:NACOS_GROUP = $Group
$env:NACOS_USERNAME = $Username
$env:NACOS_PASSWORD = $Password

Write-Host "Using NACOS_ADDR=$env:NACOS_ADDR"
Write-Host "Using NACOS_NAMESPACE=$env:NACOS_NAMESPACE"
Write-Host "Using NACOS_GROUP=$env:NACOS_GROUP"

Write-Host "Start system-service..."
Start-Process powershell -ArgumentList @("-NoProfile","-Command","cd '$PSScriptRoot\..\system-service'; mvn -q -DskipTests spring-boot:run -Dspring-boot.run.profiles=nacos") -WorkingDirectory "$PSScriptRoot\..\system-service"

Write-Host "Start biz-service..."
Start-Process powershell -ArgumentList @("-NoProfile","-Command","cd '$PSScriptRoot\..\biz-service'; mvn -q -DskipTests spring-boot:run -Dspring-boot.run.profiles=nacos") -WorkingDirectory "$PSScriptRoot\..\biz-service"

Write-Host "Start gateway..."
Start-Process powershell -ArgumentList @("-NoProfile","-Command","cd '$PSScriptRoot\..\gateway'; mvn -q -DskipTests spring-boot:run -Dspring-boot.run.profiles=nacos") -WorkingDirectory "$PSScriptRoot\..\gateway"

Write-Host "Done. Check Nacos console for instances."

