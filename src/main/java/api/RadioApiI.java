package api;

import java.io.IOException;
import java.net.URISyntaxException;


/**
 * The `RadioApiI` interface defines methods for interacting with a radio API service.
 * Implementing classes must provide concrete implementations for these methods to
 * retrieve radio-related data.
 */
public interface RadioApiI {

    /**
     * Fetches data from the radio API using the specified path.
     *
     * @param path The path to the resource to fetch.
     * @return The fetched data as a string.
     * @throws HttpBadRequestException If the request is bad.
     * @throws IOException            If an I/O error occurs while fetching data.
     * @throws InterruptedException   If the thread is interrupted while waiting.
     * @throws URISyntaxException     If there is a syntax error in the path.
     */
    String fetchData(String path) throws HttpBadRequestException, IOException, InterruptedException, URISyntaxException;

    /**
     * Retrieves a list of radio channels from the radio API.
     *
     * @return A response containing the list of radio channels.
     * @throws HttpBadRequestException If the request is bad.
     * @throws IOException            If an I/O error occurs while fetching data.
     * @throws URISyntaxException     If there is a syntax error in the request URI.
     * @throws InterruptedException   If the thread is interrupted while waiting.
     */
    RadioApiChannelsResponse getChannels() throws HttpBadRequestException, IOException, URISyntaxException, InterruptedException;

    /**
     * Retrieves detailed program information for a specific radio channel from the radio API.
     *
     * @param channelId The ID of the channel to fetch program information for.
     * @return A response containing program details for the specified channel.
     * @throws HttpBadRequestException If the request is bad.
     * @throws IOException            If an I/O error occurs while fetching data.
     * @throws URISyntaxException     If there is a syntax error in the request URI.
     * @throws InterruptedException   If the thread is interrupted while waiting.
     */
    RadioApiProgramResponse getProgram(int channelId) throws HttpBadRequestException, IOException, URISyntaxException, InterruptedException;

    /**
     * Retrieves the schedule for a specific radio channel from the radio API.
     *
     * @param channelId The ID of the channel to fetch the schedule for.
     * @return A response containing the schedule for the specified channel.
     * @throws HttpBadRequestException If the request is bad.
     * @throws IOException            If an I/O error occurs while fetching data.
     * @throws URISyntaxException     If there is a syntax error in the request URI.
     * @throws InterruptedException   If the thread is interrupted while waiting.
     */
    RadioApiScheduleResponse getSchedule(int channelId) throws HttpBadRequestException, IOException, URISyntaxException, InterruptedException;
}

