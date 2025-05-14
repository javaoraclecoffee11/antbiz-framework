package com.github.javaoraclecoffee11.antbiz_framework.backend

import io.github.cdimascio.dotenv.dotenv
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import space.mori.dalbodeule.snapadmin.external.SnapAdminAutoConfiguration


val dotenv = dotenv {
    ignoreIfMissing = true
}

@SpringBootApplication
@ImportAutoConfiguration(classes = [SnapAdminAutoConfiguration::class])
class AntbizFrameworkApplication

fun main(args: Array<String>) {
    val envVars = mapOf(
        "DB_HOST" to dotenv["DB_HOST"],
        "DB_PORT" to dotenv["DB_PORT"],
        "DB_NAME" to dotenv["DB_NAME"],
        "DB_USER" to dotenv["DB_USER"],
        "DB_PASSWORD" to dotenv["DB_PASSWORD"],
        "JWT_SIGNING_KEY" to dotenv["JWT_SIGNING_KEY"],
        "JWT_VALIDATE_KEY" to dotenv["JWT_VALIDATE_KEY"],
        "HOST_NAME" to dotenv["HOST_NAME"],
        "JWT_ISSUER" to (dotenv["JWT_ISSUER"] ?: "localhost"),
    )

    runApplication<AntbizFrameworkApplication>(*args) {
        setDefaultProperties(envVars)
    }
}
