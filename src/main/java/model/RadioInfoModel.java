package model;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A model class representing radio information and channel selection.
 */
public class RadioInfoModel {

    private LinkedHashMap<Integer, Channel> channels;
    private final AtomicInteger selectedChannel;
    private final List<RadioViewModelObserver> observers;

    /**
     * Constructs a new RadioInfoModel instance with default values.
     */
    public RadioInfoModel() {
        this.channels = new LinkedHashMap<>();
        this.selectedChannel = new AtomicInteger(1);
        this.observers = new ArrayList<>();
    }

    /**
     * Constructs a new RadioInfoModel instance with initial channels.
     *
     * @param channels The initial channel data.
     */
    public RadioInfoModel(LinkedHashMap<Integer, Channel> channels) {
        this.channels = channels;
        this.selectedChannel = new AtomicInteger(1);
        this.observers = new ArrayList<>();
    }

    /**
     * Adds an observer to be notified of data changes.
     *
     * @param observer The observer to be added.
     */
    public synchronized void addObserver(RadioViewModelObserver observer) {
        observers.add(observer);
    }

    /**
     * Notifies all registered observers that radio data has changed.
     */
    private void notifyObservers() {
        for (RadioViewModelObserver observer : observers) {
            observer.onRadioDataChanged();
        }
    }

    /**
     * Returns a copy of the stored channel data.
     *
     * @return A copy of the stored channel data.
     */
    public synchronized LinkedHashMap<Integer, Channel> getChannels() {
        return new LinkedHashMap<>(channels);
    }

    /**
     * Sets the channel data and notifies observers of the change.
     *
     * @param channels The new channel data.
     */
    public synchronized void setChannels(LinkedHashMap<Integer, Channel> channels) {
        this.channels = channels;
        notifyObservers();
    }

    /**
     * Returns the ID of the currently selected channel.
     *
     * @return The ID of the selected channel.
     */
    public int getSelectedChannelId() {
        return this.selectedChannel.get();
    }

    /**
     * Sets the ID of the currently selected channel and notifies observers of the change.
     *
     * @param selectedChannel The ID of the newly selected channel.
     */
    public void setSelectedChannelId(int selectedChannel) {
        this.selectedChannel.set(selectedChannel);
        notifyObservers();
    }
}
