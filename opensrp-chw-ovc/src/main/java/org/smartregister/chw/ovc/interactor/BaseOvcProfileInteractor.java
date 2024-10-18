package org.smartregister.chw.ovc.interactor;

import androidx.annotation.VisibleForTesting;

import org.smartregister.chw.ovc.OvcLibrary;
import org.smartregister.chw.ovc.contract.OvcProfileContract;
import org.smartregister.chw.ovc.domain.MemberObject;
import org.smartregister.chw.ovc.domain.Visit;
import org.smartregister.chw.ovc.util.AppExecutors;
import org.smartregister.chw.ovc.util.Constants;
import org.smartregister.chw.ovc.util.OvcUtil;

public class BaseOvcProfileInteractor implements OvcProfileContract.Interactor {
    protected AppExecutors appExecutors;

    @VisibleForTesting
    BaseOvcProfileInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BaseOvcProfileInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void refreshProfileInfo(MemberObject memberObject, OvcProfileContract.InteractorCallBack callback) {
        Runnable runnable = () -> appExecutors.mainThread().execute(() -> {
            callback.refreshMedicalHistory(getVisit(Constants.EVENT_TYPE.MVC_HOUSEHOLD_SERVICES_VISIT, memberObject) != null ||
                    getVisit(Constants.EVENT_TYPE.MVC_CHILD_SERVICES_VISIT, memberObject) != null);
        });
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveRegistration(final String jsonString, final OvcProfileContract.InteractorCallBack callback) {

        Runnable runnable = () -> {
            try {
                OvcUtil.saveFormEvent(jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }

        };
        appExecutors.diskIO().execute(runnable);
    }

    protected Visit getVisit(String eventType, MemberObject memberObject) {
        try {
            return OvcLibrary.getInstance().visitRepository().getLatestVisit(memberObject.getBaseEntityId(), eventType);
        } catch (Exception e) {
            return null;
        }
    }
}
