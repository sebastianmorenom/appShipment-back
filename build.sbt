import AssemblyKeys._

assemblySettings

mainClass in assembly := Some("play.core.server.ProdServerStart")

fullClasspath in assembly += Attributed.blank(PlayKeys.playPackageAssets.value)

name := "appshipment-back"

version := "1.0"

lazy val `appshipment-back` = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

routesGenerator := InjectedRoutesGenerator

libraryDependencies ++= Seq( javaJdbc ,  cache , javaWs, filters )

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

