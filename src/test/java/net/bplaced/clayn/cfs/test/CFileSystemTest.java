package net.bplaced.clayn.cfs.test;

import net.bplaced.clayn.cfs.CFileSystem;
import net.bplaced.clayn.cfs.Directory;
import net.bplaced.clayn.cfs.FileSettings;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

/**
 * Base class for all tests that test the {@link CFileSystem}. Each test can be
 * skipped by not adding the specific {@code TEST_} to the {@code runningTests}. 
 * This test also tests basic functionality such as the root and filesetting 
 * implementations.
 *
 * @author Clayn <clayn_osmato@gmx.de>
 */
public abstract class CFileSystemTest extends BaseCFSTest
{

    protected static final String TEST_ROOT = "getRoot";
    protected static final String TEST_CREATE = "createFileSystem";
    protected static final String TEST_SETTINGS = "getFileSettings";

    public CFileSystemTest()
    {
    }

    /**
     * Test of getRoot method.
     */
    @Test
    public final void testGetRoot() throws Exception
    {
        Assume.assumeTrue(runningTests.contains(TEST_ROOT));
        System.out.println("getRoot");
        CFileSystem cfs = getFileSystem();
        Assert.assertNotNull(cfs);
        Directory root = cfs.getRoot();
        Assert.assertNotNull(root);
        Assert.assertEquals("/", root.getName());
        Directory root2 = cfs.getRoot();
        Assert.assertEquals(root.getName(), root2.getName());
    }

    @Test
    public final void testCreateFS() throws Exception
    {
        //Assume.assumeTrue(runningTests.contains(TEST_CREATE));
        System.out.println("createFileSystem");
        CFileSystem cfs = getFileSystem();
        Assert.assertNotNull(cfs);
        Directory root = cfs.getRoot();
        Assert.assertNotNull(root);
        Assert.assertTrue(root.exists());
    }

    /**
     * Test of getFileSettings method.
     * @throws java.lang.Exception
     */
    @Test
    public final void testGetFileSettings() throws Exception
    {
        Assume.assumeTrue(String.format("%s Test not enabled for %s%n",
                TEST_SETTINGS, getClass().getName()), runningTests.contains(
                        "getFileSettings"));
        System.out.println(TEST_SETTINGS);
        CFileSystem cfs = getFileSystem();
        Assert.assertNotNull(cfs);
        FileSettings set = cfs.getFileSettings();
        Assert.assertNotNull(set);
        Assert.assertTrue(
                set.getCreateOnAccess() == cfs.getFileSettings().getCreateOnAccess());

    }
}
