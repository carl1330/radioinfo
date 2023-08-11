package se.umu.cs.dv21cgn.radioinfo.api.sr;

import se.umu.cs.dv21cgn.radioinfo.api.RadioApiChannelsResponse;
import se.umu.cs.dv21cgn.radioinfo.model.Channel;
import se.umu.cs.dv21cgn.radioinfo.model.Schedule;

import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * A record representing a response from the SR (Sveriges Radio) API when requesting channel information.
 * Implements the RadioApiChannelsResponse interface to provide a method for converting the API response into a LinkedHashMap of Channel objects.
 */
public record SrRadioApiChannelsResponse(Collection<SrRadioApiChannelResponse> channels) implements RadioApiChannelsResponse {

    /**
     * {@inheritDoc}
     */
    public LinkedHashMap<Integer, Channel> toChannelMap() {
        LinkedHashMap<Integer, Channel> channelHashMap = new LinkedHashMap<>();
        for (SrRadioApiChannelResponse response : channels) {
            channelHashMap.put(response.id, new Channel(response.id, response.name, new Schedule()));
        }
        return channelHashMap;
    }

    /**
     * A record representing a channel response within the channels response from the SR (Sveriges Radio) API.
     */
    public record SrRadioApiChannelResponse(int id, String name, String image) { }
}
