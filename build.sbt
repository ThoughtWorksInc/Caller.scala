publishArtifact := false

organization in ThisBuild := "com.thoughtworks.feature"

crossScalaVersions in ThisBuild := Seq("2.11.11-bin-typelevel-4", "2.12.4-bin-typelevel-4")

scalaOrganization in updateSbtClassifiers in ThisBuild := (scalaOrganization in Global).value

scalaOrganization in ThisBuild := "org.typelevel"

lazy val Caller = crossProject.crossType(CrossType.Pure)

lazy val CallerJVM = Caller.jvm.addSbtFiles(file("../build.sbt.shared"))

lazy val CallerJS = Caller.js.addSbtFiles(file("../build.sbt.shared"))

lazy val Constructor = crossProject.crossType(CrossType.Pure).dependsOn(Mixin % Test)

lazy val ConstructorJVM = Constructor.jvm.addSbtFiles(file("../build.sbt.shared"))

lazy val ConstructorJS = Constructor.js.addSbtFiles(file("../build.sbt.shared"))

lazy val Mixin = crossProject.crossType(CrossType.Pure)

lazy val MixinJVM = Mixin.jvm.addSbtFiles(file("../build.sbt.shared"))

lazy val MixinJS = Mixin.js.addSbtFiles(file("../build.sbt.shared"))

lazy val Demixin = crossProject.crossType(CrossType.Pure)

lazy val DemixinJVM = Demixin.jvm.addSbtFiles(file("../build.sbt.shared"))

lazy val DemixinJS = Demixin.js.addSbtFiles(file("../build.sbt.shared"))

lazy val The = crossProject.crossType(CrossType.Pure)

lazy val TheJVM = The.jvm.addSbtFiles(file("../build.sbt.shared"))

lazy val TheJS = The.js.addSbtFiles(file("../build.sbt.shared"))

lazy val SyntacticTypeTree = crossProject.crossType(CrossType.Pure)

lazy val Untyper = crossProject.crossType(CrossType.Pure)

lazy val UntyperJVM = Untyper.jvm.addSbtFiles(file("../build.sbt.shared"))

lazy val UntyperJS = Untyper.js.addSbtFiles(file("../build.sbt.shared"))

lazy val Factory = crossProject.crossType(CrossType.Pure).dependsOn(Untyper, The, SelfType, Glb)

lazy val FactoryJVM = Factory.jvm.addSbtFiles(file("../build.sbt.shared"))

lazy val FactoryJS = Factory.js.addSbtFiles(file("../build.sbt.shared"))

lazy val PartialApply = crossProject.crossType(CrossType.Pure)

lazy val PartialApplyJVM = PartialApply.jvm.addSbtFiles(file("../build.sbt.shared"))

lazy val PartialApplyJS = PartialApply.js.addSbtFiles(file("../build.sbt.shared"))

lazy val ImplicitApply = crossProject.crossType(CrossType.Pure)

lazy val ImplicitApplyJVM = ImplicitApply.jvm.addSbtFiles(file("../build.sbt.shared"))

lazy val ImplicitApplyJS = ImplicitApply.js.addSbtFiles(file("../build.sbt.shared"))

lazy val ByName = crossProject.crossType(CrossType.Pure)

lazy val ByNameJVM = ByName.jvm.addSbtFiles(file("../build.sbt.shared"))

lazy val ByNameJS = ByName.js.addSbtFiles(file("../build.sbt.shared"))

lazy val SelfType = crossProject.crossType(CrossType.Pure)

lazy val SelfTypeJVM = SelfType.jvm.addSbtFiles(file("../build.sbt.shared"))

lazy val SelfTypeJS = SelfType.js.addSbtFiles(file("../build.sbt.shared"))

lazy val Structural = crossProject.crossType(CrossType.Pure).dependsOn(Untyper)

lazy val StructuralJVM = Structural.jvm.addSbtFiles(file("../build.sbt.shared"))

lazy val StructuralJS = Structural.js.addSbtFiles(file("../build.sbt.shared"))

lazy val Glb = crossProject.crossType(CrossType.Pure)

lazy val GlbJVM = Glb.jvm.addSbtFiles(file("../build.sbt.shared"))

lazy val GlbJS = Glb.js.addSbtFiles(file("../build.sbt.shared"))

lazy val `mixins-ImplicitsSingleton` = crossProject.crossType(CrossType.Pure).dependsOn(Factory, ImplicitApply)

lazy val `mixins-ImplicitsSingletonJVM` = `mixins-ImplicitsSingleton`.jvm.addSbtFiles(file("../build.sbt.shared"))

lazy val `mixins-ImplicitsSingletonJS` = `mixins-ImplicitsSingleton`.js.addSbtFiles(file("../build.sbt.shared"))

lazy val unidoc = project
  .enablePlugins(StandaloneUnidoc, TravisUnidocTitle)
  .settings(
    scalacOptions += "-Yliteral-types",
    addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4"),
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.patch),
    UnidocKeys.unidocProjectFilter in ScalaUnidoc in UnidocKeys.unidoc := {
      inProjects(
        UntyperJVM,
        MixinJVM,
        DemixinJVM,
        ConstructorJVM,
        CallerJVM,
        TheJVM,
        FactoryJVM,
        PartialApplyJVM,
        ImplicitApplyJVM,
        ByNameJVM,
        `mixins-ImplicitsSingletonJVM`,
        SelfTypeJVM,
        StructuralJVM,
        GlbJVM
      )
    }
  )
