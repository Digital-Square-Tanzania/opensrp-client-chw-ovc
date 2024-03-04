package org.smartregister.chw.ovc.interactor;

import androidx.annotation.VisibleForTesting;

import org.smartregister.chw.ovc.contract.OvcRegisterContract;
import org.smartregister.chw.ovc.util.AppExecutors;
import org.smartregister.chw.ovc.util.OvcUtil;

public class BaseOvcRegisterInteractor implements OvcRegisterContract.Interactor {

    private AppExecutors appExecutors;

    @VisibleForTesting
    BaseOvcRegisterInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BaseOvcRegisterInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void saveRegistration(final String jsonString, final OvcRegisterContract.InteractorCallBack callBack) {

        Runnable runnable = () -> {
            try {
                OvcUtil.saveFormEvent(jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }

            appExecutors.mainThread().execute(() -> callBack.onRegistrationSaved());
        };
        appExecutors.diskIO().execute(runnable);
    }
}
