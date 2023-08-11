package api;

import model.HttpBadRequestException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * An abstract class representing a base for interacting with a radio API.
 */
public abstract class RadioApi implements RadioApiI {

    /**
     * The HttpClient instance used for making HTTP requests.
     */
    private final HttpClient httpClient;

    /**
     * The base URL of the radio API.
     */
    private final String baseUrl;

    /**
     * Constructs a new RadioApi instance.
     *
     * @param httpClient The HttpClient instance to use for making HTTP requests.
     * @param baseUrl The base URL of the radio API.
     */
    public RadioApi(HttpClient httpClient, String baseUrl) {
        this.httpClient = httpClient;
        this.baseUrl = baseUrl;
    }

    /**
     * Fetches data from the radio API using the provided path.
     *
     * @param path The path to the specific API endpoint.
     * @return The response body as a string.
     * @throws HttpBadRequestException If the HTTP response status code is 400 (Bad Request).
     * @throws IOException If an I/O error occurs during the request.
     * @throws InterruptedException If the request is interrupted.
     * @throws URISyntaxException If there is a syntax error in the provided URI.
     */
    public String fetchData(String path) throws HttpBadRequestException, IOException, InterruptedException, URISyntaxException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI(baseUrl + path))
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() == 400)
            throw new HttpBadRequestException();

        return httpResponse.body();
    }

    /**
     * Abstract method to get information about radio channels.
     *
     * @return A response containing information about radio channels.
     * @throws HttpBadRequestException If the HTTP response status code is 400 (Bad Request).
     * @throws IOException If an I/O error occurs during the request.
     * @throws URISyntaxException If there is a syntax error in the provided URI.
     * @throws InterruptedException If the request is interrupted.
     */
    public abstract RadioApiChannelsResponse getChannels() throws HttpBadRequestException, IOException, URISyntaxException, InterruptedException;

    /**
     * Abstract method to get program information for a specific channel.
     *
     * @param channelId The ID of the channel to retrieve program information for.
     * @return A response containing program information for the specified channel.
     * @throws HttpBadRequestException If the HTTP response status code is 400 (Bad Request).
     * @throws IOException If an I/O error occurs during the request.
     * @throws URISyntaxException If there is a syntax error in the provided URI.
     * @throws InterruptedException If the request is interrupted.
     */
    public abstract RadioApiProgramResponse getProgram(int channelId) throws HttpBadRequestException, IOException, URISyntaxException, InterruptedException;

    /**
     * Abstract method to get schedule information for a specific channel.
     *
     * @param channelId The ID of the channel to retrieve schedule information for.
     * @return A response containing schedule information for the specified channel.
     * @throws HttpBadRequestException If the HTTP response status code is 400 (Bad Request).
     * @throws IOException If an I/O error occurs during the request.
     * @throws URISyntaxException If there is a syntax error in the provided URI.
     * @throws InterruptedException If the request is interrupted.
     */
    public abstract RadioApiScheduleResponse getSchedule(int channelId) throws HttpBadRequestException, IOException, URISyntaxException, InterruptedException;
}
