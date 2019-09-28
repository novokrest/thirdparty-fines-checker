set -e
set -x
cd fineschecker
./gradlew -x test build
cd -
./build-selenium.sh
./run-selenium.sh
