package com.roadlink.tripservice.infrastructure.persistence.trip.constraint

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.persistence.AttributeConverter

/**
 * An attribute converter for converting lists of sealed objects to JSON strings for storage in a database,
 * and vice versa.
 *
 * This converter is particularly useful for sealed classes, which often lack constructors, making direct serialization/deserialization challenging.
 *
 * @param T the type of sealed object to be converted.
 * @property clazz the class of the sealed object type.
 * @property deserializer the deserializer used to deserialize sealed objects from JSON strings.
 */
open class SealedEntityListConverter<T : Any>(
    private val clazz: Class<T>,
    private val typeMap: Map<String, T>

) : AttributeConverter<List<T>, String> {

    /**
     * An ObjectMapper object for serializing and deserializing sealed objects.
     */
    private val objectMapper: ObjectMapper = jacksonObjectMapper().apply {
        registerModule(SimpleModule().apply {
            addDeserializer(clazz, SealedEntityDeserializer(typeMap))
            addSerializer(clazz, SealedEntitySerializer())
        })
    }

    /**
     * Converts a list of sealed objects into a JSON string for storage in the database.
     *
     * @param attribute the list of sealed objects to convert.
     * @return a JSON string representing the list of sealed objects.
     */
    override fun convertToDatabaseColumn(attribute: List<T>): String {
        return objectMapper.writeValueAsString(attribute)
    }

    /**
     * Converts a JSON string stored in the database into a list of sealed objects.
     *
     * @param dbData the JSON string stored in the database.
     * @return a list of sealed objects deserialized from the JSON string.
     */
    override fun convertToEntityAttribute(dbData: String): List<T> {
        val listType = objectMapper.typeFactory.constructCollectionType(
            List::class.java,
            clazz
        )
        return objectMapper.readValue(dbData, listType)
    }
}

/**
 * A serializer for sealed objects that adds type information to the JSON output.
 */
class SealedEntitySerializer<T : Any> : JsonSerializer<T>() {
    override fun serialize(value: T?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeStartObject()
        gen?.writeStringField("type", value?.javaClass?.simpleName)
        gen?.writeEndObject()
    }
}

/**
 * A deserializer for sealed objects that extracts type information from the JSON input.
 *
 * This deserializer is designed to work in conjunction with sealed classes and is used to deserialize sealed objects from JSON strings.
 * It extracts the type information from the JSON and uses it to determine which sealed object to instantiate.
 *
 * @param typeMap a map that associates type names in the JSON with sealed objects.
 */
class SealedEntityDeserializer<T : Any>(private val typeMap: Map<String, T>) :
    JsonDeserializer<T>() {

    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): T {
        val node = p?.codec?.readTree<JsonNode>(p)

        val type = node?.get("type")?.textValue()
            ?: throw JsonParseException(p, "Type field missing in JSON")

        return typeMap[type]
            ?: throw JsonParseException(p, "Invalid type: $type")
    }
}