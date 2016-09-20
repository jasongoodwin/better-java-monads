name := """better-monads"""

organization := "com.jason-goodwin"

libraryDependencies ++= Seq(
  "com.novocode" % "junit-interface" % "0.11" % "test",
  "junit" % "junit" % "4.11" % "test"
)

crossPaths := false

publishMavenStyle := true

autoScalaLibrary := false

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>http://jasongoodwin.com/better-java-monads</url>
  <licenses>
    <license>
      <name>Apache2</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:jasongoodwin/better-java-monads</url>
    <connection>scm:git:git@github.com:jasongoodwin/better-java-monads.git</connection>
  </scm>
  <developers>
    <developer>
      <id>jasongoodwin</id>
      <name>Jason Goodwin</name>
      <url>http://refactoringfactory.wordpress.com</url>
    </developer>
  </developers>)

credentials += Credentials(Path.userHome / ".nexuscredentials")
