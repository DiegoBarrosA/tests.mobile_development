import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class EmailService {
    private val client = OkHttpClient()
    private val emailJsUrl = "https://api.emailjs.com/api/v1.0/email/send"

    companion object {
        private const val SERVICE_ID = "service_99ttfa5"
        private const val TEMPLATE_ID = "template_mg5ivy4"
        private const val USER_ID = "e-vIlIUcU-eJMZstN"

    }

    fun sendEmail(toEmail: String, message: String) {
        val params = JSONObject()
        params.put("to_email", toEmail)
        params.put("message", message)

        val jsonBody = JSONObject()
        jsonBody.put("service_id", SERVICE_ID)
        jsonBody.put("template_id", TEMPLATE_ID)
        jsonBody.put("user_id", USER_ID)
        jsonBody.put("template_params", params)

        val requestBody = jsonBody.toString()
            .toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(emailJsUrl)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        println("Failed to send email: ${response.body?.string()}")
                    }
                }
            }
        })
    }
}