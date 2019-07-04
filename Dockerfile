FROM hseeberger/scala-sbt
RUN apt-get update && apt-get install -y --no-install-recommends openjfx && rm -rf /var/lib/apt/lists/*
WORKDIR /blackjack
ADD . /blackjack
CMD sbt run

# sudo docker build -t blackjack:v1
# sudo docker run -a stdin -a stdout -i blackjack:v1
