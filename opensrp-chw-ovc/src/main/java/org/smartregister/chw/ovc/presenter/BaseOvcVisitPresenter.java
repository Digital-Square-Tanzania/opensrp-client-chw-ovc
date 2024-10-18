package org.smartregister.chw.ovc.presenter;

import org.json.JSONObject;
import org.smartregister.chw.ovc.R;
import org.smartregister.chw.ovc.contract.BaseOvcVisitContract;
import org.smartregister.chw.ovc.domain.MemberObject;
import org.smartregister.chw.ovc.model.BaseOvcVisitAction;
import org.smartregister.chw.ovc.util.Constants;
import org.smartregister.chw.ovc.util.JsonFormUtils;
import org.smartregister.util.FormUtils;

import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;

import timber.log.Timber;

public class BaseOvcVisitPresenter implements BaseOvcVisitContract.Presenter, BaseOvcVisitContract.InteractorCallBack {

    protected WeakReference<BaseOvcVisitContract.View> view;
    protected BaseOvcVisitContract.Interactor interactor;
    protected MemberObject memberObject;

    public BaseOvcVisitPresenter(MemberObject memberObject, BaseOvcVisitContract.View view, BaseOvcVisitContract.Interactor interactor) {
        this.view = new WeakReference<>(view);
        this.interactor = interactor;
        this.memberObject = memberObject;
    }

    @Override
    public void startForm(String formName, String memberID, String currentLocationId) {
        try {
            if (view.get() != null) {
                JSONObject jsonObject = FormUtils.getInstance(view.get().getContext()).getFormJson(formName);
                JsonFormUtils.getRegistrationForm(jsonObject, memberID, currentLocationId);
                view.get().startFormActivity(jsonObject);
            }
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    @Override
    public boolean validateStatus() {
        return false;
    }

    @Override
    public void initialize() {
        view.get().displayProgressBar(true);
        view.get().redrawHeader(memberObject);
        interactor.calculateActions(view.get(), memberObject, this);
    }

    @Override
    public void submitVisit(Constants.SaveType saveType) {
        if (view.get() != null) {
            view.get().displayProgressBar(true);
            interactor.submitVisit(view.get(), memberObject.getBaseEntityId(), view.get().getBaseOvcVisitActions(), this, saveType);
        }
    }

    @Override
    public void reloadMemberDetails(String memberID) {
        view.get().displayProgressBar(true);
        interactor.reloadMemberDetails(memberID, this);
    }

    @Override
    public void onMemberDetailsReloaded(MemberObject memberObject) {
        if (view.get() != null) {
            this.memberObject = memberObject;
            view.get().displayProgressBar(false);
            view.get().onMemberDetailsReloaded(memberObject);
        }
    }

    @Override
    public void onRegistrationSaved(boolean isEdit) {
        Timber.v("onRegistrationSaved");
    }

    @Override
    public void preloadActions(LinkedHashMap<String, BaseOvcVisitAction> map) {
        if (view.get() != null)
            view.get().initializeActions(map);
    }

    @Override
    public void onSubmitted(boolean successful, Constants.SaveType saveType) {
        if (view.get() != null) {
            view.get().displayProgressBar(false);
            if (successful) {
                if (saveType == Constants.SaveType.SUBMIT_AND_CLOSE) {
                    view.get().submittedAndClose();
                }
            } else {
                view.get().displayToast(view.get().getContext().getString(R.string.error_unable_save_home_visit));
            }
        }
    }
}
