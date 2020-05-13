package br.eti.rafaelcouto.temporada.network.config

import android.os.Build
import br.eti.rafaelcouto.temporada.BuildConfig
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.notNullValue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class AuthenticatorClientTest {
    // mocks
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        this.mockWebServer = MockWebServer()
    }

    @Test
    fun `when build interceptor requested then should build interceptor correctly`() {
        mockWebServer.start()
        mockWebServer.enqueue(MockResponse())

        val client = AuthenticatorClient.build()

        assertThat(
            client.interceptors.filterIsInstance<HttpLoggingInterceptor>(),
            hasSize(1)
        )

        client.newCall(Request.Builder().url(mockWebServer.url("/")).build()).execute()

        val url = mockWebServer.takeRequest().requestUrl

        assertThat(url, notNullValue())

        assertThat(
            url?.queryParameterValues("api_key")?.first(),
            equalTo(BuildConfig.API_KEY)
        )
    }
}
