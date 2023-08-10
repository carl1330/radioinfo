import api.sr.SrRadioApi;
import controller.RadioInfoController;
import model.RadioInfoModel;
import view.RadioInfoView;
public class RadioInfo {
    public static void main(String[] args) {
        RadioInfoModel radioInfoModel = new RadioInfoModel();
        RadioInfoController radioInfoController = new RadioInfoController(radioInfoModel, new SrRadioApi());
       new RadioInfoView(radioInfoController);
    }
}
