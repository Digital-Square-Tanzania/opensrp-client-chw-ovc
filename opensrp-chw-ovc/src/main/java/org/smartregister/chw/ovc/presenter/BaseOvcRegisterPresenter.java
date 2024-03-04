package org.smartregister.chw.ovc.presenter;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.smartregister.chw.ovc.R;
import org.smartregister.chw.ovc.contract.OvcRegisterContract;

import java.lang.ref.WeakReference;
import java.util.List;

import timber.log.Timber;

public class BaseOvcRegisterPresenter implements OvcRegisterContract.Presenter, OvcRegisterContract.InteractorCallBack {

    public static final String TAG = BaseOvcRegisterPresenter.class.getName();

    protected WeakReference<OvcRegisterContract.View> viewReference;
    private OvcRegisterContract.Interactor interactor;
    protected OvcRegisterContract.Model model;

    public BaseOvcRegisterPresenter(OvcRegisterContract.View view, OvcRegisterContract.Model model, OvcRegisterContract.Interactor interactor) {
        viewReference = new WeakReference<>(view);
        this.interactor = interactor;
        this.model = model;
    }

    @Override
    public void startForm(String formName, String entityId, String metadata, String currentLocationId) throws Exception {
        if (StringUtils.isBlank(entityId)) {
            return;
        }

        JSONObject form = model.getFormAsJson(formName, entityId, currentLocationId);
        getView().startFormActivity(form);
    }

    @Override
    public void saveForm(String jsonString) {
        try {
            getView().showProgressDialog(R.string.saving_dialog_title);
            interactor.saveRegistration(jsonString, this);
        } catch (Exception e) {
            Timber.tag(TAG).e(Log.getStackTraceString(e));
        }
    }

    @Override
    public void onRegistrationSaved() {
        getView().hideProgressDialog();

    }

    @Override
    public void registerViewConfigurations(List<String> list) {
//        implement
    }

    @Override
    public void unregisterViewConfiguration(List<String> list) {
//        implement
    }

    @Override
    public void onDestroy(boolean b) {
//        implement
    }

    @Override
    public void updateInitials() {
//        implement
    }

    private OvcRegisterContract.View getView() {
        if (viewReference != null)
            return viewReference.get();
        else
            return null;
    }
}
