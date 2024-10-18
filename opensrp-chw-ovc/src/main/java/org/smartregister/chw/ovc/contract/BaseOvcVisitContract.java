package org.smartregister.chw.ovc.contract;

import android.content.Context;

import com.vijay.jsonwizard.domain.Form;

import org.json.JSONObject;
import org.smartregister.chw.ovc.domain.MemberObject;
import org.smartregister.chw.ovc.model.BaseOvcVisitAction;
import org.smartregister.chw.ovc.util.Constants;

import java.util.LinkedHashMap;
import java.util.Map;

public interface BaseOvcVisitContract {

    interface View extends VisitView {

        Presenter presenter();

        Form getFormConfig(JSONObject jsonForm);

        void startForm(BaseOvcVisitAction baseOvcVisitAction);

        void startFormActivity(JSONObject jsonForm);

        void startFragment(BaseOvcVisitAction baseOvcVisitAction);

        void redrawHeader(MemberObject memberObject);

        void redrawVisitUI();

        void displayProgressBar(boolean state);

        Map<String, BaseOvcVisitAction> getBaseOvcVisitActions();

        void close();

        void submittedAndClose();

        Presenter getPresenter();

        /**
         * Save the received data into the events table
         * Start aggregation of all events and persist results into the events table
         */
        void submitVisit(Constants.SaveType saveType);

        void initializeActions(LinkedHashMap<String, BaseOvcVisitAction> map);

        Context getContext();

        void displayToast(String message);

        Boolean getEditMode();

        void setEditMode(boolean editMode);

        void onMemberDetailsReloaded(MemberObject memberObject);
    }

    interface VisitView {

        /**
         * Results action when a dialog is opened and returns a payload
         *
         * @param jsonString
         */
        void onDialogOptionUpdated(String jsonString);

        Context getMyContext();
    }

    interface Presenter {

        void startForm(String formName, String memberID, String currentLocationId);

        /**
         * Recall this method to redraw ui after every submission
         *
         * @return
         */
        boolean validateStatus();

        /**
         * Preload header and visit
         */
        void initialize();

        void submitVisit(Constants.SaveType saveType);

        void reloadMemberDetails(String memberID);
    }

    interface Model {

        JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception;

    }

    interface Interactor {

        void reloadMemberDetails(String memberID, InteractorCallBack callBack);

        MemberObject getMemberClient(String memberID);

        void saveRegistration(String jsonString, boolean isEditMode, final InteractorCallBack callBack);

        void calculateActions(View view, MemberObject memberObject, InteractorCallBack callBack);

        void submitVisit(View view, String memberID, Map<String, BaseOvcVisitAction> map, InteractorCallBack callBack, Constants.SaveType saveType);
    }

    interface InteractorCallBack {

        void onMemberDetailsReloaded(MemberObject memberObject);

        void onRegistrationSaved(boolean isEdit);

        void preloadActions(LinkedHashMap<String, BaseOvcVisitAction> map);

        void onSubmitted(boolean successful, Constants.SaveType saveType);
    }
}