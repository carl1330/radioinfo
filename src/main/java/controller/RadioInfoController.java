package controller;

import model.HttpBadRequestException;
import api.RadioApiI;
import model.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The `RadioInfoController` class is responsible for managing radio information, including fetching
 * channels, schedules, and updating cached data. It acts as an intermediary between the view and the model,
 * handling user interactions and communication with external services.
 * <p>
 * This class provides methods for fetching and updating radio channels and schedules, as well as accessing
 * various pieces of radio-related data.
 *
 * @author Carl Gulliksson dv21cgn
 * @version 1.0
 */
public class RadioInfoController {
    private final RadioInfoModel radioInfoModel;
    private final RadioApiI radioApi;

    private final ReentrantLock channelUpdateLock;

    public RadioInfoController(RadioInfoModel radioInfoModel, RadioApiI srRadioApi) {
        this.radioInfoModel = radioInfoModel;
        this.radioApi = srRadioApi;
        this.channelUpdateLock = new ReentrantLock();
    }

    /**
     * Fetches the list of radio channels and updates the model.
     *
     * @throws HttpBadRequestException If the request is bad.
     * @throws IOException            If an I/O error occurs.
     * @throws URISyntaxException     If there is a syntax error in a URI.
     * @throws InterruptedException   If the thread is interrupted while waiting.
     */
    public void fetchChannels() throws HttpBadRequestException, IOException, URISyntaxException, InterruptedException {
        LinkedHashMap<Integer, Channel> channels = radioApi.getChannels().toChannelMap();
        radioInfoModel.setChannels(channels);
    }

    /**
     * Selects a radio channel and fetches its schedule if it is empty.
     *
     * @param channelId The ID of the selected channel.
     * @throws HttpBadRequestException If the request is bad.
     * @throws IOException            If an I/O error occurs.
     * @throws URISyntaxException     If there is a syntax error in a URI.
     * @throws InterruptedException   If the thread is interrupted while waiting.
     */
    public void selectChannel(int channelId) throws HttpBadRequestException, IOException, URISyntaxException, InterruptedException {
        radioInfoModel.setSelectedChannelId(channelId);
        if(!scheduleIsEmpty(channelId)) {
            return;
        }
        fetchSchedule(channelId);
    }

    /**
     * Fetches the schedule for a given radio channel and updates the model.
     *
     * @param channelId The ID of the channel to fetch the schedule for.
     * @throws HttpBadRequestException If the request is bad.
     * @throws IOException            If an I/O error occurs.
     * @throws URISyntaxException     If there is a syntax error in a URI.
     * @throws InterruptedException   If the thread is interrupted while waiting.
     */
    public  void fetchSchedule(int channelId) throws HttpBadRequestException, IOException, URISyntaxException, InterruptedException {
        channelUpdateLock.lock();
        try {
            Schedule schedule = radioApi.getSchedule(channelId).toSchedule();
            LinkedHashMap<Integer, Channel> channels = radioInfoModel.getChannels();
            channels.replace(channelId, new Channel(channelId, radioInfoModel.getChannels().get(channelId).name(), schedule));
            radioInfoModel.setChannels(channels);
        } finally {
            channelUpdateLock.unlock();
        }
    }

    /**
     * Updates cached schedules for all channels.
     *
     * @throws HttpBadRequestException If the request is bad.
     * @throws IOException            If an I/O error occurs.
     * @throws URISyntaxException     If there is a syntax error in a URI.
     * @throws InterruptedException   If the thread is interrupted while waiting.
     */
    public void updateCachedSchedules() throws HttpBadRequestException, IOException, URISyntaxException, InterruptedException {
        channelUpdateLock.lock();
        try {
            LinkedHashMap<Integer, Channel> channels = new LinkedHashMap<>();
            for (Channel channel : radioInfoModel.getChannels().values()) {
                if(!scheduleIsEmpty(channel.id())) {
                    Schedule schedule = radioApi.getSchedule(channel.id()).toSchedule();
                    channels.put(channel.id(), new Channel(channel.id(), channel.name(), schedule));
                } else {
                    channels.put(channel.id(), channel);
                }
            }
            radioInfoModel.setChannels(channels);
        } finally {
            channelUpdateLock.unlock();
        }
    }

    /**
     * Returns a map of all radio channels.
     *
     * @return A map of channel IDs to their corresponding Channel objects.
     */
    public Map<Integer, Channel> getChannels() {
        return radioInfoModel.getChannels();
    }

    /**
     * Returns the schedule for a specific channel.
     *
     * @param channelId The ID of the channel to get the schedule for.
     * @return The schedule of the specified channel.
     */
    public Schedule getSchedule(int channelId) {
        return radioInfoModel.getChannels().get(channelId).schedule();
    }

    /**
     * Returns the Channel object for a given channel ID.
     *
     * @param channelId The ID of the channel to retrieve.
     * @return The Channel object corresponding to the given ID.
     */
    public Channel getChannel(int channelId) {
        return radioInfoModel.getChannels().get(channelId);
    }

    /**
     * Returns the list of programs for the selected channel's schedule.
     *
     * @return The list of programs for the selected channel's schedule.
     */
    public List<Program> getSelectedChannelSchedule() {
        return getSchedule(radioInfoModel.getSelectedChannelId()).getPrograms();
    }

    /**
     * Checks if the schedule for a specific channel is empty.
     *
     * @param channelId The ID of the channel to check.
     * @return True if the schedule is empty, otherwise false.
     */
    public boolean scheduleIsEmpty(int channelId) {
        return getChannels().containsKey(channelId) && getChannels().get(channelId).schedule().isEmpty();
    }

    /**
     * Adds an observer to the radio view model.
     *
     * @param radioInfoView The observer to add.
     */
    public void addModelObserver(RadioViewModelObserver radioInfoView) {
        radioInfoModel.addObserver(radioInfoView);
    }
}
