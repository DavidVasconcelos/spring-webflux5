dependencies {
    val detektVersion: String by project
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion")
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-cli:$detektVersion")
}

detekt {
    autoCorrect = true
}

