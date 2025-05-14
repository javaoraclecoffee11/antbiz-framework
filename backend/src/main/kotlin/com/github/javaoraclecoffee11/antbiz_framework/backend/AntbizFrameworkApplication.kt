package com.github.javaoraclecoffee11.antbiz_framework.backend

import io.github.cdimascio.dotenv.dotenv
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import space.mori.dalbodeule.snapadmin.external.SnapAdminAutoConfiguration


val dotenv = dotenv {
    ignoreIfMissing = true
    filename = "../.env"
}

@SpringBootApplication
@ImportAutoConfiguration(classes = [SnapAdminAutoConfiguration::class])
class AntbizFrameworkApplication

fun main(args: Array<String>) {
    val envVars = mapOf(
        "DB_HOST" to dotenv["ANTBIZ_DB_HOST"],
        "DB_PORT" to dotenv["ANTBIZ_DB_PORT"],
        "DB_NAME" to dotenv["ANTBIZ_DB_NAME"],
        "DB_USER" to dotenv["ANTBIZ_DB_USER"],
        "DB_PASSWORD" to dotenv["ANTBIZ_DB_PASSWORD"],
        "JWT_SIGNING_KEY" to dotenv["ANTBIZ_JWT_SIGNING_KEY"],
        "JWT_VALIDATE_KEY" to dotenv["ANTBIZ_JWT_VALIDATE_KEY"],
        "HOST_NAME" to dotenv["ANTBIZ_HOST_NAME"],
        "FRONTEND_URL" to dotenv["ANTBIZ_FRONTEND"],
        "JWT_ISSUER" to (dotenv["ANTBIZ_JWT_ISSUER"] ?: "localhost"),
    )

    runApplication<AntbizFrameworkApplication>(*args) {
        setDefaultProperties(envVars)
    }
}
