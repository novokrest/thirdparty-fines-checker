FROM openjdk:11

RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add -
RUN sh -c 'echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list'
RUN apt-get -y update
RUN apt-get install -y google-chrome-stable

RUN mkdir -p /app
COPY fineschecker/build/libs/fineschecker-1.0.0.jar /app/bin.jar
COPY drivers/chromedriver_linux_77 /app/chromedriver
COPY run.sh /app
WORKDIR /app
CMD ["java", "-jar", "-Dwebdriver.chrome.driver=chromedriver", "-Dhttps.protocols=TLSv1,TLSv1.1,TLSv1.2", "bin.jar"]

