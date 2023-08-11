import api.sr.SrRadioApi;
import api.sr.SrRadioApiChannelsResponse;
import api.sr.SrRadioApiProgramResponse;
import api.sr.SrRadioApiScheduleResponse;
import model.HttpBadRequestException;
import model.Channel;
import model.Schedule;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SrRadioApiTest {
    @Test
    void shouldFetchTwoChannels() throws HttpBadRequestException, IOException, URISyntaxException, InterruptedException {
        HttpClient httpClient = mock(HttpClient.class);
        HttpResponse<String> httpResponse = mock(HttpResponse.class);

        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn(
                "{" +
                        "  \"copyright\": \"Copyright Sveriges Radio 2023. All rights reserved.\",\n" +
                        "  \"channels\": [\n" +
                        "    {\n" +
                        "      \"image\": \"https://static-cdn.sr.se/images/132/2186745_512_512.jpg?preset=api-default-square\",\n" +
                        "      \"imagetemplate\": \"https://static-cdn.sr.se/images/132/2186745_512_512.jpg\",\n" +
                        "      \"color\": \"31a1bd\",\n" +
                        "      \"tagline\": \"Talat innehåll om samhälle, kultur och vetenskap. Kanalen erbjuder nyheter och aktualiteter, granskning och fördjupning men också livsåskådnings-och livsstilsprogram samt underhållning och upplevelser till exempel i form av teater.\",\n" +
                        "      \"siteurl\": \"https://sverigesradio.se/p1\",\n" +
                        "      \"liveaudio\": {\n" +
                        "        \"id\": 132,\n" +
                        "        \"url\": \"https://sverigesradio.se/topsy/direkt/srapi/132.mp3\",\n" +
                        "        \"statkey\": \"/app/direkt/p1[k(132)]\"\n" +
                        "      },\n" +
                        "      \"scheduleurl\": \"https://api.sr.se/v2/scheduledepisodes?channelid=132\",\n" +
                        "      \"channeltype\": \"Rikskanal\",\n" +
                        "      \"xmltvid\": \"p1.sr.se\",\n" +
                        "      \"id\": 132,\n" +
                        "      \"name\": \"P1\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"image\": \"https://static-cdn.sr.se/images/163/2186754_512_512.jpg?preset=api-default-square\",\n" +
                        "      \"imagetemplate\": \"https://static-cdn.sr.se/images/163/2186754_512_512.jpg\",\n" +
                        "      \"color\": \"ff5a00\",\n" +
                        "      \"tagline\": \"P2 är den klassiska musikkanalen som även erbjuder jazz samt folk- och världsmusik. Digitalt sänder vi musikprogram dygnet runt, i FM finns även program på andra språk än svenska.\",\n" +
                        "      \"siteurl\": \"https://sverigesradio.se/p2\",\n" +
                        "      \"liveaudio\": {\n" +
                        "        \"id\": 163,\n" +
                        "        \"url\": \"https://sverigesradio.se/topsy/direkt/srapi/163.mp3\",\n" +
                        "        \"statkey\": \"/app/direkt/p2[k(163)]\"\n" +
                        "      },\n" +
                        "      \"scheduleurl\": \"https://api.sr.se/v2/scheduledepisodes?channelid=163\",\n" +
                        "      \"channeltype\": \"Rikskanal\",\n" +
                        "      \"xmltvid\": \"p2.sr.se\",\n" +
                        "      \"id\": 163,\n" +
                        "      \"name\": \"P2\"\n" +
                        "    }" +
                        "   ]" +
                        "}"
        );
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);

        SrRadioApi srRadioApi = new SrRadioApi(httpClient);
        SrRadioApiChannelsResponse srRadioApiChannelsResponse = srRadioApi.getChannels();
        assertEquals(2, srRadioApiChannelsResponse.channels().size());
    }

    @Test
    void shouldConvertChannelApiResponseToModel() throws IOException, InterruptedException, HttpBadRequestException, URISyntaxException {
        HttpClient httpClient = mock(HttpClient.class);
        HttpResponse<String> httpResponse = mock(HttpResponse.class);

        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn(
                "{" +
                        "  \"copyright\": \"Copyright Sveriges Radio 2023. All rights reserved.\",\n" +
                        "  \"channels\": [\n" +
                        "    {\n" +
                        "      \"image\": \"https://static-cdn.sr.se/images/132/2186745_512_512.jpg?preset=api-default-square\",\n" +
                        "      \"imagetemplate\": \"https://static-cdn.sr.se/images/132/2186745_512_512.jpg\",\n" +
                        "      \"color\": \"31a1bd\",\n" +
                        "      \"tagline\": \"Talat innehåll om samhälle, kultur och vetenskap. Kanalen erbjuder nyheter och aktualiteter, granskning och fördjupning men också livsåskådnings-och livsstilsprogram samt underhållning och upplevelser till exempel i form av teater.\",\n" +
                        "      \"siteurl\": \"https://sverigesradio.se/p1\",\n" +
                        "      \"liveaudio\": {\n" +
                        "        \"id\": 132,\n" +
                        "        \"url\": \"https://sverigesradio.se/topsy/direkt/srapi/132.mp3\",\n" +
                        "        \"statkey\": \"/app/direkt/p1[k(132)]\"\n" +
                        "      },\n" +
                        "      \"scheduleurl\": \"https://api.sr.se/v2/scheduledepisodes?channelid=132\",\n" +
                        "      \"channeltype\": \"Rikskanal\",\n" +
                        "      \"xmltvid\": \"p1.sr.se\",\n" +
                        "      \"id\": 132,\n" +
                        "      \"name\": \"P1\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"image\": \"https://static-cdn.sr.se/images/163/2186754_512_512.jpg?preset=api-default-square\",\n" +
                        "      \"imagetemplate\": \"https://static-cdn.sr.se/images/163/2186754_512_512.jpg\",\n" +
                        "      \"color\": \"ff5a00\",\n" +
                        "      \"tagline\": \"P2 är den klassiska musikkanalen som även erbjuder jazz samt folk- och världsmusik. Digitalt sänder vi musikprogram dygnet runt, i FM finns även program på andra språk än svenska.\",\n" +
                        "      \"siteurl\": \"https://sverigesradio.se/p2\",\n" +
                        "      \"liveaudio\": {\n" +
                        "        \"id\": 163,\n" +
                        "        \"url\": \"https://sverigesradio.se/topsy/direkt/srapi/163.mp3\",\n" +
                        "        \"statkey\": \"/app/direkt/p2[k(163)]\"\n" +
                        "      },\n" +
                        "      \"scheduleurl\": \"https://api.sr.se/v2/scheduledepisodes?channelid=163\",\n" +
                        "      \"channeltype\": \"Rikskanal\",\n" +
                        "      \"xmltvid\": \"p2.sr.se\",\n" +
                        "      \"id\": 163,\n" +
                        "      \"name\": \"P2\"\n" +
                        "    }" +
                        "   ]" +
                        "}"
        );
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);

        SrRadioApi srRadioApi = new SrRadioApi(httpClient);
        SrRadioApiChannelsResponse srRadioApiChannelsResponse = srRadioApi.getChannels();
        HashMap<Integer, Channel> channels = srRadioApiChannelsResponse.toChannelMap();
        assertEquals(2, channels.size());
    }

    @Test
    void shouldFetchP3Schedule() throws IOException, InterruptedException, HttpBadRequestException, URISyntaxException {
        SrRadioApi srRadioApi = new SrRadioApi();
        SrRadioApiScheduleResponse srRadioApiScheduleResponse = srRadioApi.getSchedule(164);
        assertTrue(srRadioApiScheduleResponse.schedule().size() > 0);
    }

    @Test
    void shouldConvertScheduleApiResponseToModel() throws IOException, InterruptedException, HttpBadRequestException, URISyntaxException {
        HttpClient httpClient = mock(HttpClient.class);
        HttpResponse<String> httpResponse = mock(HttpResponse.class);

        when(httpResponse.statusCode()).thenReturn(200);

        when(httpResponse.body()).thenReturn(
                "{\n" +
                        "  \"copyright\": \"Copyright Sveriges Radio 2023. All rights reserved.\",\n" +
                        "  \"schedule\": [\n" +
                        "    {\n" +
                        "      \"episodeid\": 2209025,\n" +
                        "      \"title\": \"Ekot senaste nytt\",\n" +
                        "      \"description\": \"Senaste nyheterna varje timme från Ekot.\",\n" +
                        "      \"starttimeutc\": \"/Date(" + LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() + ")/\",\n" +
                        "      \"endtimeutc\": \"/Date(1691272920000)/\",\n" +
                        "      \"program\": {\n" +
                        "        \"id\": 5380,\n" +
                        "        \"name\": \"Ekot senaste nytt\"\n" +
                        "      },\n" +
                        "      \"channel\": {\n" +
                        "        \"id\": 164,\n" +
                        "        \"name\": \"P3\"\n" +
                        "      },\n" +
                        "      \"imageurl\": \"https://static-cdn.sr.se/images/5380/a7898d6c-786f-4fcb-b68e-c5f56f4b3bef.jpg?preset=api-default-square\",\n" +
                        "      \"imageurltemplate\": \"https://static-cdn.sr.se/images/5380/a7898d6c-786f-4fcb-b68e-c5f56f4b3bef.jpg\",\n" +
                        "      \"photographer\": \"\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"episodeid\": 2209026,\n" +
                        "      \"title\": \"Vaken\",\n" +
                        "      \"subtitle\": \"med Anton Vretander och Calle Nilsmo\",\n" +
                        "      \"description\": \"Musik, tävlingar och samtal. Ett program för dig som är vaken, helt enkelt.\",\n" +
                        "      \"starttimeutc\": \"/Date(" + LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() + ")/\",\n" +
                        "      \"endtimeutc\": \"/Date(1691276400000)/\",\n" +
                        "      \"program\": {\n" +
                        "        \"id\": 2689,\n" +
                        "        \"name\": \"Vaken med P3 & P4\"\n" +
                        "      },\n" +
                        "      \"channel\": {\n" +
                        "        \"id\": 164,\n" +
                        "        \"name\": \"P3\"\n" +
                        "      },\n" +
                        "      \"imageurl\": \"https://static-cdn.sr.se/images/2689/0a92a699-d655-4f50-8606-8b2c150d6a03.jpg?preset=api-default-square\",\n" +
                        "      \"imageurltemplate\": \"https://static-cdn.sr.se/images/2689/0a92a699-d655-4f50-8606-8b2c150d6a03.jpg\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"episodeid\": 2209027,\n" +
                        "      \"title\": \"Ekot senaste nytt\",\n" +
                        "      \"description\": \"Senaste nyheterna varje timme från Ekot.\",\n" +
                        "      \"starttimeutc\": \"/Date(" + LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() + ")/\",\n" +
                        "      \"endtimeutc\": \"/Date(1691276520000)/\",\n" +
                        "      \"program\": {\n" +
                        "        \"id\": 5380,\n" +
                        "        \"name\": \"Ekot senaste nytt\"\n" +
                        "      },\n" +
                        "      \"channel\": {\n" +
                        "        \"id\": 164,\n" +
                        "        \"name\": \"P3\"\n" +
                        "      },\n" +
                        "      \"imageurl\": \"https://static-cdn.sr.se/images/5380/a7898d6c-786f-4fcb-b68e-c5f56f4b3bef.jpg?preset=api-default-square\",\n" +
                        "      \"imageurltemplate\": \"https://static-cdn.sr.se/images/5380/a7898d6c-786f-4fcb-b68e-c5f56f4b3bef.jpg\",\n" +
                        "      \"photographer\": \"\"\n" +
                        "    }" +
                        "   ]" +
                        "}"
        );
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);
        SrRadioApi srRadioApi = new SrRadioApi(httpClient);
        SrRadioApiScheduleResponse srRadioApiScheduleResponse = srRadioApi.getSchedule(164);
        Schedule schedule = srRadioApiScheduleResponse.toSchedule();
        assertTrue(schedule.getPrograms().size() > 0);
    }

    @Test
    void shouldFetchP4JämtlandProgram() throws IOException, InterruptedException, HttpBadRequestException, URISyntaxException {
        HttpClient httpClient = mock(HttpClient.class);
        HttpResponse<String> httpResponse = mock(HttpResponse.class);

        when(httpResponse.statusCode()).thenReturn(200);

        when(httpResponse.body()).thenReturn(
                "{\n" +
                        "  \"copyright\": \"Copyright Sveriges Radio 2023. All rights reserved.\",\n" +
                        "  \"program\": {\n" +
                        "    \"description\": \"I P4 Jämtland hör du lokala nyheter, aktualiteter, väderprognoser, sport och kultur. \",\n" +
                        "    \"broadcastinfo\": \"Har du nyhetstips? Kontakta oss via e-post: p4jamtland@sverigesradio.se eller 063-16 06 31\",\n" +
                        "    \"email\": \"p4jamtland@sverigesradio.se \",\n" +
                        "    \"phone\": \"\",\n" +
                        "    \"programurl\": \"https://sverigesradio.se/default.aspx?programid=78\",\n" +
                        "    \"programslug\": \"jamtland\",\n" +
                        "    \"programimage\": \"https://static-cdn.sr.se/images/78/3f969001-3c0d-46c0-a3a6-30b44ef999c0.jpg?preset=api-default-square\",\n" +
                        "    \"programimagetemplate\": \"https://static-cdn.sr.se/images/78/3f969001-3c0d-46c0-a3a6-30b44ef999c0.jpg\",\n" +
                        "    \"programimagewide\": \"https://static-cdn.sr.se/images/78/4c177fcc-4f7d-4661-86e5-ceaab573cbb0.jpg?preset=api-default-rectangle\",\n" +
                        "    \"programimagetemplatewide\": \"https://static-cdn.sr.se/images/78/4c177fcc-4f7d-4661-86e5-ceaab573cbb0.jpg\",\n" +
                        "    \"socialimage\": \"https://static-cdn.sr.se/images/78/3f969001-3c0d-46c0-a3a6-30b44ef999c0.jpg?preset=api-default-square\",\n" +
                        "    \"socialimagetemplate\": \"https://static-cdn.sr.se/images/78/3f969001-3c0d-46c0-a3a6-30b44ef999c0.jpg\",\n" +
                        "    \"socialmediaplatforms\": [\n" +
                        "      {\n" +
                        "        \"platform\": \"Facebook\",\n" +
                        "        \"platformurl\": \"https://www.facebook.com/P4Jamtland\"\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"platform\": \"Twitter\",\n" +
                        "        \"platformurl\": \"https://twitter.com//\"\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"platform\": \"Instagram\",\n" +
                        "        \"platformurl\": \"https://instagram.com/p4jamtland/\"\n" +
                        "      }\n" +
                        "    ],\n" +
                        "    \"channel\": {\n" +
                        "      \"id\": 200,\n" +
                        "      \"name\": \"P4 Jämtland\"\n" +
                        "    },\n" +
                        "    \"archived\": false,\n" +
                        "    \"hasondemand\": true,\n" +
                        "    \"haspod\": true,\n" +
                        "    \"responsibleeditor\": \"Olof Ekerlid\",\n" +
                        "    \"id\": 78,\n" +
                        "    \"name\": \"Nyheter P4 Jämtland\"\n" +
                        "  }\n" +
                        "}"
        );
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);
        SrRadioApi srRadioApi = new SrRadioApi(httpClient);
        SrRadioApiProgramResponse srRadioApiProgramResponse = srRadioApi.getProgram(78);
        assertEquals("Nyheter P4 Jämtland", srRadioApiProgramResponse.name());
    }

    @Test
    void shouldMergeThreeJsonSchedulesSuccessfully() {
        String jsonString1 = "{\n" +
                "  \"copyright\": \"Copyright Sveriges Radio 2023. All rights reserved.\",\n" +
                "  \"schedule\": [\n" +
                "    {\n" +
                "      \"episodeid\": 2212459,\n" +
                "      \"title\": \"Ekot senaste nytt\",\n" +
                "      \"description\": \"Senaste nyheterna varje timme från Ekot.\",\n" +
                "      \"starttimeutc\": \"\\/Date(1691359200000)\\/\",\n" +
                "      \"endtimeutc\": \"\\/Date(1691359320000)\\/\",\n" +
                "      \"program\": {\n" +
                "        \"id\": 5380,\n" +
                "        \"name\": \"Ekot senaste nytt\"\n" +
                "      },\n" +
                "      \"channel\": {\n" +
                "        \"id\": 164,\n" +
                "        \"name\": \"P3\"\n" +
                "      },\n" +
                "      \"imageurl\": \"https://static-cdn.sr.se/images/5380/a7898d6c-786f-4fcb-b68e-c5f56f4b3bef.jpg?preset=api-default-square\",\n" +
                "      \"imageurltemplate\": \"https://static-cdn.sr.se/images/5380/a7898d6c-786f-4fcb-b68e-c5f56f4b3bef.jpg\",\n" +
                "      \"photographer\": \"\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        String jsonString2 = "{\n" +
                "  \"copyright\": \"Copyright Sveriges Radio 2023. All Rights reserver.\",\n" +
                "  \"schedule\": [\n" +
                "   {\n" +
                "      \"episodeid\": 2212460,\n" +
                "      \"title\": \"Vaken\",\n" +
                "      \"subtitle\": \"med Erika Nielsen och Kalle Johansson\",\n" +
                "      \"description\": \"Musik, tävlingar och samtal. Ett program för dig som är vaken, helt enkelt.\",\n" +
                "      \"starttimeutc\": \"\\/Date(1691359320000)\\/\",\n" +
                "      \"endtimeutc\": \"\\/Date(1691362800000)\\/\",\n" +
                "      \"program\": {\n" +
                "        \"id\": 2689,\n" +
                "        \"name\": \"Vaken med P3 & P4\"\n" +
                "      },\n" +
                "      \"channel\": {\n" +
                "        \"id\": 164,\n" +
                "        \"name\": \"P3\"\n" +
                "      },\n" +
                "      \"imageurl\": \"https://static-cdn.sr.se/images/2689/0a92a699-d655-4f50-8606-8b2c150d6a03.jpg?preset=api-default-square\",\n" +
                "      \"imageurltemplate\": \"https://static-cdn.sr.se/images/2689/0a92a699-d655-4f50-8606-8b2c150d6a03.jpg\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        String jsonString3 = "{\n" +
                "  \"copyright\": \"Copyright Sveriges Radio 2023. All Rights reserver.\",\n" +
                "  \"schedule\": [\n" +
                " {\n" +
                "      \"episodeid\": 2212461,\n" +
                "      \"title\": \"Ekot senaste nytt\",\n" +
                "      \"description\": \"Senaste nyheterna varje timme från Ekot.\",\n" +
                "      \"starttimeutc\": \"\\/Date(1691362800000)\\/\",\n" +
                "      \"endtimeutc\": \"\\/Date(1691362920000)\\/\",\n" +
                "      \"program\": {\n" +
                "        \"id\": 5380,\n" +
                "        \"name\": \"Ekot senaste nytt\"\n" +
                "      },\n" +
                "      \"channel\": {\n" +
                "        \"id\": 164,\n" +
                "        \"name\": \"P3\"\n" +
                "      },\n" +
                "      \"imageurl\": \"https://static-cdn.sr.se/images/5380/a7898d6c-786f-4fcb-b68e-c5f56f4b3bef.jpg?preset=api-default-square\",\n" +
                "      \"imageurltemplate\": \"https://static-cdn.sr.se/images/5380/a7898d6c-786f-4fcb-b68e-c5f56f4b3bef.jpg\",\n" +
                "      \"photographer\": \"\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        SrRadioApi srRadioApi = new SrRadioApi();
    }
}
