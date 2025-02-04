@echo off
cd target
powershell -Command "Compress-Archive -Path allure-results -DestinationPath allure-results.zip"
