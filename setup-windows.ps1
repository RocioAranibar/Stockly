$ErrorActionPreference = "Stop"
Write-Host "Stockly - preparando Gradle Wrapper..." -ForegroundColor Green
$jar = Join-Path $PSScriptRoot "gradle\wrapper\gradle-wrapper.jar"
if (!(Test-Path $jar)) {
  Invoke-WebRequest -UseBasicParsing -Uri "https://raw.githubusercontent.com/gradle/gradle/v8.13.0/gradle/wrapper/gradle-wrapper.jar" -OutFile $jar
}
Write-Host "Listo. Abrí esta carpeta desde Android Studio y ejecutá composeApp." -ForegroundColor Green
