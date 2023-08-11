package se.umu.cs.dv21cgn.radioinfo.api;

import se.umu.cs.dv21cgn.radioinfo.model.Channel;

import java.util.LinkedHashMap;

/**
 * An interface representing a response from a radio API when requesting channel information.
 * Implementing classes should provide a method to convert the API response into a LinkedHashMap of Channel objects.
 */
public interface RadioApiChannelsResponse {

    /**
     * Converts the API response into a LinkedHashMap of Channel objects.
     *
     * @return A LinkedHashMap containing channel information from the API response.
     */
    LinkedHashMap<Integer, Channel> toChannelMap();
}
