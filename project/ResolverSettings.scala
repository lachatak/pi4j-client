import sbt._
import sbt.Keys._

object ResolverSettings {

  lazy val settings = Seq(resolvers ++=
    Seq(
      Resolver.defaultLocal,
      Resolver.mavenLocal,
      Resolver.typesafeRepo("snapshots"),
      Resolver.sonatypeRepo("snapshots"),
      Resolver.sonatypeRepo("releases")
    )
  )
}
