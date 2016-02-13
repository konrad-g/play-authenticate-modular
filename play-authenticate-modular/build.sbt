organization := "com.feth"

name := "play-authenticate-usage"

scalaVersion := "2.11.6"

version := "1.0-SNAPSHOT"

val appDependencies = Seq(
  "be.objectify"  %% "deadbolt-java"     % "2.4.3",
  // Comment the next line for local development of the Play Authentication core:
  "com.feth"      %% "play-authenticate" % "0.7.2-SNAPSHOT", // use 0.7.1 on production
  "org.postgresql"    %  "postgresql"        % "9.4-1201-jdbc41",
  cache,
  javaWs,
  javaJdbc,
  specs2 % Test,
  "org.webjars" % "bootstrap" % "3.2.0",
  "org.easytesting" % "fest-assert" % "1.4" % "test",
  "mysql" % "mysql-connector-java" % "5.1.18"
)

// add resolver for deadbolt and easymail snapshots
resolvers += Resolver.sonatypeRepo("snapshots")

// display deprecated or poorly formed Java
javacOptions ++= Seq("-Xlint:unchecked")
javacOptions ++= Seq("-Xlint:deprecation")
javacOptions ++= Seq("-Xdiags:verbose")

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

//  Uncomment the next line for local development of the Play Authenticate core:
//lazy val playAuthenticate = project.in(file("modules/play-authenticate")).enablePlugins(PlayJava)

lazy val root = (project in file("."))
  .enablePlugins(PlayJava, PlayEbean)
  .settings(
    libraryDependencies ++= appDependencies
  )
  /* Uncomment the next lines for local development of the Play Authenticate core: */
  //.dependsOn(playAuthenticate)
  //.aggregate(playAuthenticate)

// Modular language files - App
unmanagedResourceDirectories in Compile += baseDirectory.value / "app/com/play/auth/elements/gui/base/i18n"
unmanagedResourceDirectories in Compile += baseDirectory.value / "app/com/play/auth/elements/gui/restricted/i18n"
unmanagedResourceDirectories in Compile += baseDirectory.value / "app/com/play/auth/elements/gui/index/i18n"
unmanagedResourceDirectories in Compile += baseDirectory.value / "app/com/play/auth/elements/gui/profile/i18n"

// Modular language files - Auth
unmanagedResourceDirectories in Compile += baseDirectory.value / "app/com/play/auth/elements/auth/main/i18n"
unmanagedResourceDirectories in Compile += baseDirectory.value / "app/com/play/auth/elements/auth/gui/account/i18n"
unmanagedResourceDirectories in Compile += baseDirectory.value / "app/com/play/auth/elements/auth/gui/login/i18n"
unmanagedResourceDirectories in Compile += baseDirectory.value / "app/com/play/auth/elements/auth/gui/password_change/i18n"
unmanagedResourceDirectories in Compile += baseDirectory.value / "app/com/play/auth/elements/auth/gui/password_remind/i18n"
unmanagedResourceDirectories in Compile += baseDirectory.value / "app/com/play/auth/elements/auth/gui/signup/i18n"