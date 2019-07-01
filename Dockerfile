FROM hseeberger/scala-sbt
WORKDIR /blackjack
ADD . /blackjack
CMD sbt run
