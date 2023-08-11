package se.umu.cs.dv21cgn.radioinfo.model;

/**
 * An interface to be implemented by classes that observe changes in the RadioInfoModel.
 */
public interface RadioInfoModelObserver {

    /**
     * This method is called when radio data in the associated RadioInfoModel has changed.
     * Observers should use this method to update their UI or perform other necessary actions.
     */
    void onRadioDataChanged();
}
