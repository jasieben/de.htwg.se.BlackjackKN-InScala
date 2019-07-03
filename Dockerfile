FROM hseeberger/scala-sbt
WORKDIR /BlackjackKNInScala
ADD . /BlackjackKNInScala
CMD sbt run
