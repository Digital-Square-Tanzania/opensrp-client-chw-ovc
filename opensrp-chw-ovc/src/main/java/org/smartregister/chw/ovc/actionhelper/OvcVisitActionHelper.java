package org.smartregister.chw.ovc.actionhelper;

import android.content.Context;

import org.smartregister.chw.ovc.model.BaseOvcVisitAction;
import org.smartregister.chw.ovc.domain.VisitDetail;

import java.util.List;
import java.util.Map;

import timber.log.Timber;

/**
 * Designed to set default methods for the MVC Action Helper
 * This object must remain inert to the Home Visit action. Its designed primarily for extension by simple visit actions
 */
public abstract class OvcVisitActionHelper implements BaseOvcVisitAction.OvcVisitActionHelper {
    protected Context context;

    @Override
    public void onJsonFormLoaded(String jsonString, Context context, Map<String, List<VisitDetail>> details) {
        this.context = context;
    }

    /**
     * set preprocessed status to be inert
     *
     * @return null
     */
    @Override
    public String getPreProcessed() {
        return null;
    }

    /**
     * set schedule status to be inert
     *
     * @return null
     */
    @Override
    public BaseOvcVisitAction.ScheduleStatus getPreProcessedStatus() {
        return BaseOvcVisitAction.ScheduleStatus.DUE;
    }

    /**
     * set schedule status to be inert
     *
     * @return null
     */
    @Override
    public String getPreProcessedSubTitle() {
        return null;
    }

    /**
     * prevent post processing
     *
     * @return null
     */
    @Override
    public String postProcess(String jsonPayload) {
        return null;
    }

    /**
     * Do nothing on payload received
     *
     * @param baseOvcVisitAction
     */
    @Override
    public void onPayloadReceived(BaseOvcVisitAction baseOvcVisitAction) {
        Timber.v("onPayloadReceived");
    }

    public Context getContext() {
        return context;
    }
}