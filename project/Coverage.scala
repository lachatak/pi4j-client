import scoverage.ScoverageSbtPlugin.ScoverageKeys._

object Coverage {

  lazy val apiSettings = Seq(
    coverageExcludedPackages := "org.kaloz.pi4j.client.*"
  )

  lazy val coreSettings = Seq(
    coverageExcludedPackages := "org.kaloz.pi4j.client.fallback.*"
  )

}
