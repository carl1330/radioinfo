package se.umu.cs.dv21cgn.radioinfo;

import se.umu.cs.dv21cgn.radioinfo.api.sr.SrRadioApi;
import se.umu.cs.dv21cgn.radioinfo.controller.RadioInfoController;
import se.umu.cs.dv21cgn.radioinfo.model.RadioInfoModel;
import se.umu.cs.dv21cgn.radioinfo.view.RadioInfoView;
public class RadioInfo {
    public static void main(String[] args) {
        RadioInfoModel radioInfoModel = new RadioInfoModel();
        RadioInfoController radioInfoController = new RadioInfoController(radioInfoModel, new SrRadioApi());
       new RadioInfoView(radioInfoController);
    }
}
