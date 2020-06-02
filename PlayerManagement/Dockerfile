FROM hseeberger/scala-sbt:8u212_1.2.8_2.13.0
RUN apt-get update && apt-get install -y --no-install-recommends openjfx && rm -rf /var/lib/apt/lists/*
WORKDIR /PlayerManagement
ADD . /PlayerManagement
CMD sbt run

# sudo docker build -t blackjack:v1
# sudo docker run -a stdin -a stdout -i blackjack:v1
