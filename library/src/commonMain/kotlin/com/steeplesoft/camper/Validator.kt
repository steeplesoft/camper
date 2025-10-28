package com.steeplesoft.camper

abstract class Validator<T>(
    val validate: (s: T?) -> Boolean,
    val errorText: String
)
