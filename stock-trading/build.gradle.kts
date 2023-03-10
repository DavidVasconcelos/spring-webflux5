tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

dependencies {
    val reactorKotlinVersion: String by project
    val reactorTestVersion: String by project
    val ninjaSquadVersion: String by project
    val detektVersion: String by project
    val kluentVersion: String by project
    implementation(project(":stock-commons"))
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:$reactorKotlinVersion")
    testImplementation("io.projectreactor:reactor-test:$reactorTestVersion")
    testImplementation("com.ninja-squad:springmockk:$ninjaSquadVersion")
    testImplementation("org.amshove.kluent:kluent:$kluentVersion")
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion")
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-cli:$detektVersion")
}

detekt {
    autoCorrect = true
}
