package com.github.javaoraclecoffee11.antbiz_framework.backend.lib

import java.time.LocalDateTime

fun String.datetimeParser() = LocalDateTime.parse(this + "T00:00:00")