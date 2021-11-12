package de.solidblocks.cloud.config

import com.fasterxml.jackson.annotation.JsonProperty

data class DockerRegistryConfig(

    @JsonProperty("address")
    val address: String?,

    @JsonProperty("password")
    val password: String,

    @JsonProperty("username")
    val username: String
)
