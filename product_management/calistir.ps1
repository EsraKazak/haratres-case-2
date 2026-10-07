chcp 65001 | Out-Null
$env:JAVA_TOOL_OPTIONS = "-Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dspring.profiles.active=console"
Write-Host "Urun Yonetim Sistemi baslatiliyor..."
.\mvnw.cmd spring-boot:run