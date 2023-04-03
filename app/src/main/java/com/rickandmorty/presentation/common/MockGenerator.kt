package com.rickandmorty.presentation.common

import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.KTypeParameter
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.typeOf

inline fun <reified T : Any> makeRandomInstance(
    random: Random = Random,
    config: MakeRandomInstanceConfig = MakeRandomInstanceConfig(),
): T {
    val producer = RandomInstanceProducer(random, config)
    return producer.makeRandomInstance(T::class, typeOf<T>()) as T
}

class NoUsableConstructor : Error()

class MakeRandomInstanceConfig(
    var possibleCollectionSizes: IntRange = 1..5,
    var possibleIntRange: IntRange = 0..99999,
    var possibleStringSizes: IntRange = 1..10,
    var any: Any = "Anything",
)

class RandomInstanceProducer(
    private val random: Random,
    private val config: MakeRandomInstanceConfig,
) {

    private fun makeRandomInstanceForParam(
        paramType: KType,
        classRef: KClass<*>,
        type: KType,
    ): Any {
        return when (val classifier = paramType.classifier) {
            is KClass<*> -> makeRandomInstance(classifier, paramType)
            is KTypeParameter -> {
                val typeParameterName = classifier.name
                val typeParameterId =
                    classRef.typeParameters.indexOfFirst { it.name == typeParameterName }
                val parameterType = type.arguments[typeParameterId].type ?: typeOf<Any>()
                makeRandomInstance(parameterType.classifier as KClass<*>, parameterType)
            }

            else -> throw Error("Type of the classifier $classifier is not supported")
        }
    }

    fun makeRandomInstance(classRef: KClass<*>, type: KType): Any {
        val primitive = makeStandardInstanceOrNull(classRef, type)
        if (primitive != null) {
            return primitive
        }

        if (isEnum(classRef)) {
            return getRandomEnum(classRef)
        }

        val constructors = classRef.constructors.shuffled(random)

        for (constructor in constructors) {
            try {
                val arguments = constructor.parameters
                    .map { makeRandomInstanceForParam(it.type, classRef, type) }
                    .toTypedArray()

                return constructor.call(*arguments)
            } catch (e: Throwable) {
                e.printStackTrace()
                // no-op. We catch any possible error here that might occur during class creation
            }
        }

        throw NoUsableConstructor()
    }

    private fun makeStandardInstanceOrNull(classRef: KClass<*>, type: KType) = when (classRef) {
        Any::class -> config.any
        Boolean::class -> random.nextBoolean()
        Int::class -> random.nextInt(config.possibleIntRange)
        Long::class -> random.nextLong()
        Double::class -> random.nextDouble()
        Float::class -> random.nextFloat()
        Char::class -> makeRandomChar(random)
        String::class -> makeRandomString(random)
        List::class, Collection::class -> makeRandomList(classRef, type)
        Map::class -> makeRandomMap(classRef, type)
        else -> null
    }

    private fun makeRandomList(classRef: KClass<*>, type: KType): List<Any?> {
        val numOfElements = random.nextInt(
            config.possibleCollectionSizes.first,
            config.possibleCollectionSizes.last + 1
        )
        val elemType = type.arguments[0].type!!
        return (1..numOfElements)
            .map { makeRandomInstanceForParam(elemType, classRef, type) }
    }

    private fun makeRandomMap(classRef: KClass<*>, type: KType): Map<Any?, Any?> {
        val numOfElements = random.nextInt(
            config.possibleCollectionSizes.first,
            config.possibleCollectionSizes.last + 1
        )
        val keyType = type.arguments[0].type!!
        val valType = type.arguments[1].type!!
        val keys = (1..numOfElements)
            .map { makeRandomInstanceForParam(keyType, classRef, type) }
        val values = (1..numOfElements)
            .map { makeRandomInstanceForParam(valType, classRef, type) }
        return keys.zip(values).toMap()
    }

    private fun makeRandomChar(random: Random) = ('A'..'z').random(random)
    private fun makeRandomString(random: Random) =
        (
            1..random.nextInt(
                config.possibleStringSizes.first,
                config.possibleStringSizes.last + 1
            )
            )
            .map { makeRandomChar(random) }
            .joinToString(separator = "") { "$it" }

    private fun isEnum(type: KClass<out Any>) = type.isSubclassOf(Enum::class)
    private fun getRandomEnum(type: KClass<out Any>) =
        getEnumValues(type).toList().shuffled().first()

    private fun getEnumValues(enumClass: KClass<out Any>): Array<out Enum<*>> =
        (enumClass as KClass<out Enum<*>>).java.enumConstants
}
