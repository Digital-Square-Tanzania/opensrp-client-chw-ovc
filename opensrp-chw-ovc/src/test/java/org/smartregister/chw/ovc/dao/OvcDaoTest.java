package org.smartregister.chw.ovc.dao;

import net.sqlcipher.database.SQLiteDatabase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.smartregister.repository.Repository;

@RunWith(MockitoJUnitRunner.class)
public class OvcDaoTest extends OvcDao {

    @Mock
    private Repository repository;

    @Mock
    private SQLiteDatabase database;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        setRepository(repository);
    }

    @Test
    public void testIsRegisteredForGbv() {
        Mockito.doReturn(database).when(repository).getReadableDatabase();
        boolean registered = OvcDao.isRegisteredForGbv("12345");
        Mockito.verify(database).rawQuery(Mockito.contains("SELECT count(p.base_entity_id) count FROM ec_gbv_register p WHERE p.base_entity_id = '12345' AND p.is_closed = 0"), Mockito.any());
        Assert.assertFalse(registered);
    }

    @Test
    public void testGetClientAge() {
        Mockito.doReturn(database).when(repository).getReadableDatabase();
        OvcDao.getClientAge("12345");
        Mockito.verify(database).rawQuery(Mockito.contains("SELECT  dob  FROM ec_family_member WHERE base_entity_id = '12345'"), Mockito.any());
    }

    @Test
    public void testGetClientSex() {
        Mockito.doReturn(database).when(repository).getReadableDatabase();
        OvcDao.getClientSex("12345");
        Mockito.verify(database).rawQuery(Mockito.contains("SELECT  gender  FROM ec_family_member WHERE base_entity_id = '12345'"), Mockito.any());
    }
}

