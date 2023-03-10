tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

dependencies {
    val reactorKotlinVersion: String by project
    val detektVersion: String by project
    implementation(project(":stock-commons"))
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:$reactorKotlinVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo")
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion")
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-cli:$detektVersion")
}

detekt {
    autoCorrect = true
}