package com.nacho

import io.micronaut.runtime.Micronaut.run
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.servers.Server

@OpenAPIDefinition(
	info = Info(
		title = "Shared Expenses API",
		version = "1.0",
		description = "An API for managing shared expenses among friends"
	),
	servers = [Server(url = "http://localhost:8085")]
)
object Api {}

fun main(args: Array<String>) {
	run(*args)
}

