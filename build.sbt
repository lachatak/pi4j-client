lazy val root = Project("root", file("."))
  .aggregate(client, server, common)
  .disablePlugins(sbtassembly.AssemblyPlugin)

lazy val common = Project("common", file("common"))
  .settings(BaseSettings.defaultSettings: _*)
  .settings(Dependencies.common: _*)
  .settings(Testing.settings: _*)
  .disablePlugins(sbtassembly.AssemblyPlugin)

lazy val server = Project("server", file("server"))
  .dependsOn(common)
  .settings(BaseSettings.defaultSettings: _*)
  .settings(Dependencies.server: _*)
  .settings(Testing.settings: _*)
  .settings(Assembly.remoteServerAssemblySettings: _*)

lazy val client = Project("client", file("client"))
  .dependsOn(common)
  .settings(BaseSettings.clientSettings: _*)
  .settings(Dependencies.client: _*)
  .settings(Testing.clientSettings: _*)
  .settings(Assembly.clientAssemblySettings: _*)

