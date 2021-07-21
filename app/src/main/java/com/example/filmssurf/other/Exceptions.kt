package com.example.filmssurf.other

import java.io.IOException

class ApiLimitException(message: String): IOException(message)
class InvalidFormatException(message: String): IOException(message)
class InvalidApiKeyException(message: String): IOException(message)
class ServiceOfflineException(message: String): IOException(message)
class InternalErrorException(message: String): IOException(message)
class TimeoutRequestException(message: String): IOException(message)