package fr.uge.confroid.database;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.Date;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import fr.uge.confroid.configuration.Configuration;
import fr.uge.confroid.configuration.IntegerValue;
import fr.uge.confroid.storage.ConfroidDatabase;
import fr.uge.confroid.storage.ConfroidPackage;
import fr.uge.confroid.storage.ConfroidPackageDao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class ConfroidDatabaseTest {
    private ConfroidPackageDao dao;
    private ConfroidDatabase database;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, ConfroidDatabase.class).build();
        dao = database.packageDao();
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void testShouldFindAllVersionsReturnEmptyList() {
        assertEquals(0, dao.findAllVersions("p1").size());
    }

    @Test
    public void testShouldCreateAndFindByTag() {
        ConfroidPackage p = new ConfroidPackage("p1", 0, new Date(), new Configuration(new IntegerValue(0)), "T");
        dao.create(p);

        assertEquals(p, dao.findByTag(p.getName(), p.getTag()));
    }

    @Test
    public void testShouldCreateAndFindByVersion() {
        ConfroidPackage p1 = new ConfroidPackage("p1", 0, new Date(), new Configuration(new IntegerValue(0)));
        dao.create(p1);

        assertEquals(p1, dao.findByVersion(p1.getName(), p1.getVersion()));
    }

    @Test
    public void testShouldFindLatest() {
        assertNull(dao.findLastVersion("p1"));

        ConfroidPackage p = new ConfroidPackage("p1", 0, new Date(), new Configuration(new IntegerValue(0)));
        dao.create(p);

        assertEquals(p, dao.findLastVersion(p.getName()));

        p = new ConfroidPackage("p1", 1, new Date(), new Configuration(new IntegerValue(1)));
        dao.create(p);
    }

    @Test
    public void testShouldNotAllowVersionConflict() {
        assertNull(dao.findLastVersion("p1"));

        ConfroidPackage p = new ConfroidPackage("p1", 0, new Date(), new Configuration(new IntegerValue(0)));
        dao.create(p);

        assertEquals(p, dao.findLastVersion(p.getName()));

        exception.expect(SQLiteConstraintException.class);
        p = new ConfroidPackage("p1", 0, new Date(), new Configuration(new IntegerValue(1)));
        dao.create(p);
    }
}

