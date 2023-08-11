import se.umu.cs.dv21cgn.radioinfo.api.RadioApi;
import se.umu.cs.dv21cgn.radioinfo.api.RadioApiChannelsResponse;
import se.umu.cs.dv21cgn.radioinfo.api.RadioApiProgramResponse;
import se.umu.cs.dv21cgn.radioinfo.api.RadioApiScheduleResponse;
import se.umu.cs.dv21cgn.radioinfo.model.HttpBadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FetchApiDataTest {
    HttpClient httpClient;
    HttpResponse<String> httpResponse;
    RadioApi radioApi;
    String baseUrl = "https://example.com";
    @BeforeEach
    void setUp() {
        httpClient = mock(HttpClient.class);
        httpResponse = mock(HttpResponse.class);
        radioApi = new RadioApi(httpClient, baseUrl){
            @Override
            public RadioApiChannelsResponse getChannels() throws HttpBadRequestException, IOException, URISyntaxException, InterruptedException {
                return null;
            }

            @Override
            public RadioApiProgramResponse getProgram(int channelId) throws HttpBadRequestException, IOException, URISyntaxException, InterruptedException {
                return null;
            }

            @Override
            public RadioApiScheduleResponse getSchedule(int channelId) throws HttpBadRequestException, IOException, URISyntaxException, InterruptedException {
                return null;
            }

        };
    }

    @Test
    void shouldReturnApiData() throws IOException, InterruptedException {
        String channelsApiPath = "";

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);
        when(httpResponse.body()).thenReturn("{\n" +
                "  \"copyright\": \"Copyright Sveriges Radio 2023. All rights reserved.\",\n" +
                "  \"channel\": {\n" +
                "    \"image\": \"https://static-cdn.sr.se/images/132/2186745_512_512.jpg?preset=api-default-square\",\n" +
                "    \"imagetemplate\": \"https://static-cdn.sr.se/images/132/2186745_512_512.jpg\",\n" +
                "    \"color\": \"31a1bd\",\n" +
                "    \"tagline\": \"Talat innehåll om samhälle, kultur och vetenskap. Kanalen erbjuder nyheter och aktualiteter, granskning och fördjupning men också livsåskådnings-och livsstilsprogram samt underhållning och upplevelser till exempel i form av teater.\",\n" +
                "    \"siteurl\": \"https://sverigesradio.se/p1\",\n" +
                "    \"liveaudio\": {\n" +
                "      \"id\": 132,\n" +
                "      \"url\": \"https://sverigesradio.se/topsy/direkt/srapi/132.mp3\",\n" +
                "      \"statkey\": \"/app/direkt/p1[k(132)]\"\n" +
                "    },\n" +
                "    \"scheduleurl\": \"https://api.sr.se/v2/scheduledepisodes?channelid=132\",\n" +
                "    \"channeltype\": \"Rikskanal\",\n" +
                "    \"xmltvid\": \"p1.sr.se\",\n" +
                "    \"id\": 132,\n" +
                "    \"name\": \"P1\"\n" +
                "  }\n" +
                "}");
        when(httpResponse.statusCode()).thenReturn(200);

        String radioApiResponse = null;

        try {
            radioApiResponse = radioApi.fetchData(channelsApiPath);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }

         assertNotNull(radioApiResponse);
    }

    @Test
    void shouldThrowHttpBadRequestException() throws IOException, InterruptedException {
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(400);

        assertThrows(HttpBadRequestException.class, () -> {
            String channelsApiPath = "";
            radioApi.fetchData(channelsApiPath);
        });
    }

}
