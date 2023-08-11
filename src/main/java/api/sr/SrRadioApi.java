package api.sr;

import api.RadioApi;
import com.google.gson.*;
import model.HttpBadRequestException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * An implementation of the RadioApi abstract class for interacting with the SR (Sveriges Radio) API.
 */
public class SrRadioApi extends RadioApi {

    /**
     * Constructs a new SrRadioApi instance with a custom HttpClient.
     *
     * @param httpClient The HttpClient instance to use for making HTTP requests.
     */
    public SrRadioApi(HttpClient httpClient) {
        super(httpClient, "https://api.sr.se/api/v2/");
    }

    /**
     * Constructs a new SrRadioApi instance with a default HttpClient.
     */
    public SrRadioApi() {
        super(HttpClient.newHttpClient(), "https://api.sr.se/api/v2/");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SrRadioApiChannelsResponse getChannels() throws HttpBadRequestException, IOException, URISyntaxException, InterruptedException {
        String jsonString = super.fetchData("channels?format=json&pagination=false");
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        return new Gson().fromJson(jsonObject, SrRadioApiChannelsResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SrRadioApiProgramResponse getProgram(int channelId) throws HttpBadRequestException, IOException, URISyntaxException, InterruptedException {
        String jsonString = super.fetchData("programs/" + channelId + "?format=json");
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        return new Gson().fromJson(jsonObject.get("program"), SrRadioApiProgramResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SrRadioApiScheduleResponse getSchedule(int channelId) throws HttpBadRequestException, IOException, URISyntaxException, InterruptedException {
        String jsonString = super.fetchData("scheduledepisodes?channelid=" + channelId + "&format=json&pagination=false");
        String jsonStringYesterday = super.fetchData("scheduledepisodes?channelid=" + channelId + "&format=json&pagination=false&date=" + LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        String jsonStringTomorrow = super.fetchData("scheduledepisodes?channelid=" + channelId + "&format=json&pagination=false&date=" + LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        JsonObject jsonObject = mergeSchedule(jsonStringYesterday, jsonString, jsonStringTomorrow);
        return new Gson().fromJson(jsonObject, SrRadioApiScheduleResponse.class);
    }

    /**
     * Merges schedule information from three different days into a single JsonObject.
     *
     * @param schedule1 Schedule information for the first day.
     * @param schedule2 Schedule information for the second day.
     * @param schedule3 Schedule information for the third day.
     * @return A JsonObject containing merged schedule information.
     */
    private JsonObject mergeSchedule(String schedule1, String schedule2, String schedule3) {
        JsonObject jsonObject1 = JsonParser.parseString(schedule1).getAsJsonObject();
        JsonObject jsonObject2 = JsonParser.parseString(schedule2).getAsJsonObject();
        JsonObject jsonObject3 = JsonParser.parseString(schedule3).getAsJsonObject();

        JsonArray scheduleArray1 = jsonObject1.getAsJsonArray("schedule");
        JsonArray scheduleArray2 = jsonObject2.getAsJsonArray("schedule");
        JsonArray scheduleArray3 = jsonObject3.getAsJsonArray("schedule");

        for (JsonElement program : scheduleArray2) {
            scheduleArray1.add(program);
        }

        for (JsonElement program : scheduleArray3) {
            scheduleArray1.add(program);
        }

        // Create a new JsonObject to merge episodes
        JsonObject mergedEpisodes = new JsonObject();
        mergedEpisodes.add("schedule", scheduleArray1);

        return mergedEpisodes;
    }
}
