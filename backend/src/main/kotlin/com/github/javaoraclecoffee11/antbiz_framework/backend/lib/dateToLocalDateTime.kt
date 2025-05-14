package com.github.javaoraclecoffee11.antbiz_framework.backend.lib

import java.util.Date

fun Date.toLocalDateTime() = this.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()