package org.smartregister.chw.ovc.presenter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.smartregister.chw.ovc.contract.OvcRegisterFragmentContract;
import org.smartregister.chw.ovc.util.Constants;
import org.smartregister.chw.ovc.util.DBConstants;
import org.smartregister.configurableviews.model.View;

import java.util.Set;
import java.util.TreeSet;

public class BaseOvcRegisterFragmentPresenterTest {
    @Mock
    protected OvcRegisterFragmentContract.View view;

    @Mock
    protected OvcRegisterFragmentContract.Model model;

    private BaseOvcRegisterFragmentPresenter baseOvcRegisterFragmentPresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        baseOvcRegisterFragmentPresenter = new BaseOvcRegisterFragmentPresenter(view, model, "");
    }

    @Test
    public void assertNotNull() {
        Assert.assertNotNull(baseOvcRegisterFragmentPresenter);
    }

    @Test
    public void getMainCondition() {
        Assert.assertEquals("ec_ovc_register.is_closed is 0", baseOvcRegisterFragmentPresenter.getMainCondition());
    }

    @Test
    public void getDueFilterCondition() {
        Assert.assertEquals(" (cast( julianday(STRFTIME('%Y-%m-%d', datetime('now'))) -  julianday(IFNULL(SUBSTR(malaria_test_date,7,4)|| '-' || SUBSTR(malaria_test_date,4,2) || '-' || SUBSTR(malaria_test_date,1,2),'')) as integer) between 7 and 14) ", baseOvcRegisterFragmentPresenter.getDueFilterCondition());
    }

    @Test
    public void getDefaultSortQuery() {
        Assert.assertEquals(Constants.TABLES.OVC_REGISTER + "." + DBConstants.KEY.LAST_INTERACTED_WITH + " DESC ", baseOvcRegisterFragmentPresenter.getDefaultSortQuery());
    }

    @Test
    public void getMainTable() {
        Assert.assertEquals(Constants.TABLES.OVC_REGISTER, baseOvcRegisterFragmentPresenter.getMainTable());
    }

    @Test
    public void initializeQueries() {
        Set<View> visibleColumns = new TreeSet<>();
        baseOvcRegisterFragmentPresenter.initializeQueries(null);
        Mockito.doNothing().when(view).initializeQueryParams("ec_ovc_register", null, null);
        Mockito.verify(view).initializeQueryParams("ec_ovc_register", null, null);
        Mockito.verify(view).initializeAdapter(visibleColumns);
        Mockito.verify(view).countExecute();
        Mockito.verify(view).filterandSortInInitializeQueries();
    }

}