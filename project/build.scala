import sbt._
import Keys._
import org.scalatra.sbt._
import org.scalatra.sbt.PluginKeys._
import com.mojolly.scalate.ScalatePlugin._
import ScalateKeys._

object GeorginaBuild extends Build {
  val Organization = "com.mjamesruggiero"
  val Name = "Georgina"
  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.10.3"
  val ScalatraVersion = "2.2.2"

  lazy val project = Project (
    "georgina",
    file("."),
    settings = Defaults.defaultSettings ++ ScalatraPlugin.scalatraWithJRebel ++ scalateSettings ++ Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      resolvers += Classpaths.typesafeReleases,
      libraryDependencies ++= Seq(
        "ch.qos.logback"          % "logback-classic"            % "1.0.13" % "runtime",
        "com.google.guava"        % "guava"                      % "14.0-rc3",
        "com.h2database"          %  "h2"                        % "[1.3,)",
        "io.argonaut"             %% "argonaut"                  % "6.0.3",
        "joda-time"               % "joda-time"                  % "2.2",
        "mysql"                   % "mysql-connector-java"       % "[5.1,)",
        "org.eclipse.jetty"       % "jetty-webapp"               % "8.1.8.v20121106" % "container",
        "org.scalatra"            %% "scalatra"                  % ScalatraVersion,
        "org.scalatra"            %% "scalatra-scalate"          % ScalatraVersion,
        "org.scalatra"            %% "scalatra-scalatest"        % "2.2.2" % "test",
        "org.scalaz"              %% "scalaz-core"               % "7.0.6",
        "org.scalaz.stream"       %% "scalaz-stream"             % "0.4.1",
        "org.scalikejdbc"         %% "scalikejdbc"               % "1.7.4",
        "org.scalikejdbc"         %% "scalikejdbc-config"        % "1.7.5",
        "org.scalikejdbc"         %% "scalikejdbc-interpolation" % "1.7.4",
        "org.scalikejdbc"         %% "scalikejdbc-test"          % "1.7.5"   % "test",
        "org.eclipse.jetty.orbit" % "javax.servlet"              % "3.0.0.v201112011016" % "container;provided;test" artifacts (Artifact("javax.servlet", "jar", "jar"))
      ),
      scalateTemplateConfig in Compile <<= (sourceDirectory in Compile){ base =>
        Seq(
          TemplateConfig(
            base / "webapp" / "WEB-INF" / "templates",
            Seq.empty,  /* default imports should be added here */
            Seq(
              Binding("context", "_root_.org.scalatra.scalate.ScalatraRenderContext", importMembers = true, isImplicit = true)
            ),  /* add extra bindings here */
            Some("templates")
          )
        )
      }
    )
  )
}
