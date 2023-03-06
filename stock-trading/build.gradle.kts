tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

dependencies {
    val reactorKotlinVersion: String by project
    val reactorTestVersion: String by project
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:$reactorKotlinVersion")
    testImplementation("io.projectreactor:reactor-test:$reactorTestVersion")
}
