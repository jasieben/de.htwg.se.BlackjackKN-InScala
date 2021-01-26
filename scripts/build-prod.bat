cd ..
cd GameLogic
cmd /C start /wait sbt assembly
cd ..
cd PlayerManagement
cmd /C start /wait sbt assembly
cd ..
cmd /C start /wait docker-compose build
cd scripts