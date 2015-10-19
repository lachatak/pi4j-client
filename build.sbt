lazy val root = Project("root", file("."))
  .aggregate(client, server, common)
  .disablePlugins(sbtassembly.AssemblyPlugin)

lazy val common = Project("common", file("common"))
  .settings(BaseSettings.settings: _*)
  .settings(Dependencies.common: _*)
  .settings(Assembly.pi4jCommonAssemblySettings: _*)

lazy val server = Project("server", file("server"))
  .dependsOn(common)
  .settings(BaseSettings.settings: _*)
  .settings(Dependencies.server: _*)
  .settings(Assembly.pi4jRemoteServerAssemblySettings: _*)

lazy val client = Project("client", file("client"))
  .dependsOn(common)
  .settings(BaseSettings.settings: _*)
  .settings(javacOptions += "-g")
  .settings(Dependencies.client: _*)
  .settings(Assembly.pi4jClientAssemblySettings: _*)
//for local testing
  .settings(javaOptions in (Test, run) ++= Seq("-javaagent:" + System.getProperty("user.home") + "/.ivy2/cache/org.aspectj/aspectjweaver/jars/aspectjweaver-1.8.7.jar","-Dpi4j.client.mode=stub"))
  .settings(fork in (Test, run) := true)

