# Arranca todo para grabar el video: MySQL, Apache, emulador y la app.
$sdk = "$env:LOCALAPPDATA\Android\Sdk"
$adb = "$sdk\platform-tools\adb.exe"
if (-not (Get-Process mysqld -ErrorAction SilentlyContinue)) {
    Start-Process C:\xampp\mysql\bin\mysqld.exe -ArgumentList '--defaults-file=C:\xampp\mysql\bin\my.ini','--standalone' -WindowStyle Hidden }
if (-not (Get-Process httpd -ErrorAction SilentlyContinue)) {
    Start-Process C:\xampp\apache\bin\httpd.exe -WindowStyle Hidden -WorkingDirectory C:\xampp\apache\bin }
if (-not (& $adb devices | Select-String 'emulator')) {
    Start-Process "$sdk\emulator\emulator.exe" -ArgumentList '-avd','Pixel_Cedula' -WindowStyle Hidden }
& $adb wait-for-device
do { Start-Sleep 3 } until ((& $adb shell getprop sys.boot_completed) -match '1')
& $adb install -r "$PSScriptRoot\VerificarCedulaApp\app\build\outputs\apk\debug\app-debug.apk"
& $adb shell am start -n com.example.personas/.VerificarCedulaActivity
'Listo. phpMyAdmin: http://localhost/phpmyadmin  (base bd_app, tabla usuarios)'
