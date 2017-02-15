package net.bplaced.clayn.cfs.test;

import java.util.HashSet;
import java.util.Set;
import net.bplaced.clayn.cfs.CFileSystem;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

/**
 * The base class for all default tests of the filesystem. It defines some
 * necessary methods for running the tests and may have some own tests.
 *
 * @author Clayn <clayn_osmato@gmx.de>
 */
public abstract class BaseCFSTest
{

    protected final Set<String> runningTests = new HashSet<>();

    public BaseCFSTest()
    {
    }

    @Before
    public void setUp() throws Exception
    {
    }

    @After
    public void tearDown()
    {
    }

    protected final void checkTest(String test)
    {
        Assume.assumeTrue(runningTests.contains(test));
    }

    /**
     * Returns a new {@link CFileSystem filesystem}. Each call of this method
     * must create a new instance.
     *
     * @return a filesystem to perform the tests
     * @throws Exception if an exception occures during the creation
     * @since 0.1
     */
    public abstract CFileSystem getFileSystem() throws Exception;

    @Test
    public final void testGetFileSystem() throws Exception
    {
        CFileSystem cfs1 = getFileSystem();
        CFileSystem cfs2 = getFileSystem();

        assertFalse(cfs1 == cfs2);
    }
}
