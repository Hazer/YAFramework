package io.vithor.yamvpframework.rest

/**
 * Created by Guizion on 13/06/16.
 */

import okhttp3.Request
import okhttp3.ResponseBody
import retrofit2.Response
import java.net.ConnectException

/**
 * Created by Vithorio Polten on 1/4/16.
 */
class ErrorService(val errorType: ErrorType, val statusCode: HttpStatusCode = HttpStatusCode(0), val responseBody: ResponseBody? = null, val errorBody: String? = null, val request: Request? = null) {

    object Factory {
        fun from(request: Request, t: Throwable): ErrorService {
            if (t is ConnectException) {
                return ErrorService(ErrorType.CannotReachServer)
            } else if (t.cause is java.io.EOFException) {
                return ErrorService(ErrorType.CannotReachServer)
            } else if (t.cause is java.net.UnknownHostException) {
                return ErrorService(ErrorType.CannotReachServer)
            }
            return ErrorService(ErrorType.Unknown, request = request)
        }

        fun <T> from(request: Request, resp: Response<T>): ErrorService {
            val raw = resp.raw()
            val code = HttpStatusCode(raw?.code() ?: 0)
            val errorType = when (code.description) {
                HttpStatusCode.Status.InternalServerError -> ErrorType.InternalServerError
                HttpStatusCode.Status.NetworkConnectTimeoutError -> ErrorType.TimeOut
                HttpStatusCode.Status.Unauthorized -> ErrorType.Unauthorized
                HttpStatusCode.Status.BadRequest -> ErrorType.BadRequest
                else -> ErrorType.Unknown
            }
            return ErrorService(errorType, code, raw?.body(), resp.errorBody()?.string(), request = request)
        }
    }

    enum class ErrorType {
        Unknown,
        CannotReachServer,
        DeserializationFailed,
        InternalServerError,
        TimeOut,
        Unauthorized,
        BadRequest,

        ;
    }
}
