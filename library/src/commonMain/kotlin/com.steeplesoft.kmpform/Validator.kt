package com.steeplesoft.kmpform

abstract class Validator<T>(
    val validate: (s: T?) -> Boolean,
    val errorText: String
)
