package net.bplaced.clayn.cfs.test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;
import net.bplaced.clayn.cfs.CFileSystem;
import net.bplaced.clayn.cfs.Directory;
import net.bplaced.clayn.cfs.SimpleFile;
import net.bplaced.clayn.cfs.util.Child;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 */
public abstract class DirectoryTest extends BaseCFSTest
{

    protected static final String TEST_NAME = "getName";
    protected static final String TEST_MK_DIR = "mkDir";
    protected static final String TEST_MK_DIRS = "mkDirs";
    protected static final String TEST_LIST_ALL_FILES = "listFiles_All";
    protected static final String TEST_DELETE = "delete";
    protected static final String TEST_LIST_DIRECTORIES = "listDirectories";
    protected static final String TEST_STRING = "toString";
    protected static final String TEST_PARENT = "getParent";
    protected static final String TEST_ROOT = "isRoot";

    protected static final String[] TEST_ALL = new String[]
    {
        TEST_NAME,
        TEST_MK_DIR,
        TEST_LIST_ALL_FILES,
        TEST_DELETE,
        TEST_MK_DIRS,
        TEST_LIST_DIRECTORIES,
        TEST_STRING,
        TEST_PARENT,
        TEST_ROOT
    };

    private CFileSystem fileSystem;

    @Before
    @Override
    public void setUp() throws Exception
    {
        fileSystem = getFileSystem();
    }

    @Test
    public final void testGetName() throws IOException
    {
        checkTest(TEST_NAME);
        System.out.println(TEST_NAME);
        Directory dir = fileSystem.getDirectory("Test");

        assertNotNull(dir);
        assertFalse(dir.exists());

        assertNotNull(dir.getName());
        assertEquals("Test", dir.getName());

        dir.mkDir();
        assertNotNull(dir.getName());
        assertEquals("Test", dir.getName());
    }

    @Test(expected = IOException.class)
    public final void testMkDir() throws IOException
    {
        checkTest(TEST_MK_DIR);
        System.out.println(TEST_MK_DIR);

        Directory dir = null;

        try
        {
            dir = fileSystem.getDirectory("Test");
        } catch (IOException iOException)
        {
            fail("Couldn't create testdirectory");
        }

        assertNotNull(dir);
        assertFalse(dir.exists());

        dir.mkDir();
        assertTrue(dir.exists());

        try
        {
            dir = fileSystem.getDirectory("Test2/Test3");
        } catch (IOException iOException)
        {
            fail("Couldn't create testdirectory");
        }
        assertFalse(dir.exists());
        dir.changeDirectory("Test2").mkDir();
    }

    @Test
    public final void testListFiles_All() throws IOException
    {
        checkTest(TEST_LIST_ALL_FILES);
        System.out.println(TEST_LIST_ALL_FILES);
        Directory dir = fileSystem.getDirectory("Test");
        dir.mkDirs();

        assertNotNull(dir);
        List<SimpleFile> files = dir.listFiles();
        assertNotNull(files);
        assertTrue(files.isEmpty());
        dir.changeDirectory("Test2").mkDir();
        files = dir.listFiles();
        assertNotNull(files);
        assertTrue(files.isEmpty());
        dir.getFile("TestFile.txt");
        files = dir.listFiles();
        assertNotNull(files);
        assertTrue(files.isEmpty());
        dir.getFile("TestFile.txt").create();
        files = dir.listFiles();
        assertNotNull(files);
        assertFalse(files.isEmpty());
        assertEquals(1, files.size());
        dir.getFile("TestFile2.txt").create();
        files = dir.listFiles();
        assertNotNull(files);
        assertFalse(files.isEmpty());
        assertEquals(2, files.size());
        files.stream().map(SimpleFile::exists).forEach(Assert::assertTrue);
    }

    @Test
    public final void testDelete() throws IOException
    {
        checkTest(TEST_DELETE);
        System.out.println(TEST_DELETE);
        Directory dir = fileSystem.getDirectory("Test");
        assertNotNull(dir);
        assertFalse(dir.exists());
        Directory dir2 = dir.changeDirectory("Test2");
        dir2.mkDirs();
        SimpleFile file = dir.getFile("TestFile1.txt");
        SimpleFile file2 = dir2.getFile("TestFile2.txt");
        file.create();
        file2.create();

        assertTrue(dir.exists());
        assertTrue(dir2.exists());
        assertTrue(file.exists());
        assertTrue(file2.exists());

        dir.delete();

        assertFalse(dir.exists());
        assertFalse(dir2.exists());
        assertFalse(file.exists());
        assertFalse(file2.exists());
    }

    @Test
    public final void testMkDirs() throws IOException
    {
        checkTest(TEST_MK_DIRS);
        System.out.println(TEST_MK_DIRS);
        Directory dir = fileSystem.getDirectory("Test");
        Directory dir2 = dir.changeDirectory("Test2");

        assertFalse(dir.exists());
        assertFalse(dir2.exists());

        dir2.mkDirs();

        assertTrue(dir.exists());
        assertTrue(dir2.exists());

        dir = fileSystem.getDirectory("Test3");
        dir2 = dir.changeDirectory("Test4");
        Directory dir3 = dir.changeDirectory("Test5");

        assertFalse(dir.exists());
        assertFalse(dir2.exists());
        assertFalse(dir3.exists());

        dir2.mkDirs();
        assertTrue(dir.exists());
        assertTrue(dir2.exists());
        assertFalse(dir3.exists());

    }

    @Test
    public final void testListDirectories() throws IOException
    {
        checkTest(TEST_LIST_DIRECTORIES);
        System.out.println(TEST_LIST_DIRECTORIES);
        Directory dir = fileSystem.getDirectory("Test");
        List<Directory> directories = dir.listDirectories();

        assertNotNull(directories);
        assertTrue(directories.isEmpty());

        dir.changeDirectory("Test2").mkDirs();
        directories = dir.listDirectories();

        assertNotNull(directories);
        assertFalse(directories.isEmpty());
        assertEquals(1, directories.size());

        dir.changeDirectory("Test3");

        assertNotNull(directories);
        assertFalse(directories.isEmpty());
        assertEquals(1, directories.size());

        dir.changeDirectory("Test2").changeDirectory("Test4").mkDirs();

        assertNotNull(directories);
        assertFalse(directories.isEmpty());
        assertEquals(1, directories.size());
    }

    @Test
    public final void testToString() throws IOException
    {
        checkTest(TEST_STRING);
        System.out.println(TEST_STRING);
        String separator=fileSystem.getSeparator();
        Directory dir = fileSystem.getDirectory("Test");

        assertEquals(String.format("%1$sTest%1$s", separator),
                dir.toString());
        assertEquals(String.format("%1$sTest%1$sTest2%1$s",
                separator),
                dir.changeDirectory("Test2").toString());
    }

    @Test
    public final void testGetParent() throws IOException
    {
        checkTest(TEST_PARENT);
        System.out.println(TEST_PARENT);
        Directory dir = fileSystem.getDirectory("Test");
        Directory dir2 = fileSystem.getDirectory("Test2");

        Directory dir3 = dir.changeDirectory("Test3");
        Directory dir4 = dir.changeDirectory("Test4");
        Directory dir5 = dir2.changeDirectory("Test5");

        Directory root = fileSystem.getRoot();

        assertNull(root.getParent());
        Stream.of(dir, dir2, dir3, dir4, dir5).map(Child<Directory>::getParent).forEach(
                Assert::assertNotNull);

        assertFalse(Directory.equals(dir3.getParent(), dir5.getParent()));
        assertTrue(Directory.equals(dir3.getParent(), dir4.getParent()));
        assertTrue(Directory.equals(dir.getParent(), dir2.getParent()));
        assertTrue(Directory.equals(root, dir2.getParent()));
    }

    @Test
    public final void testDirectoryEquals() throws IOException, Exception
    {
        CFileSystem cfs = getFileSystem();
        Directory dir = cfs.getDirectory("Test");
        Directory dir2 = cfs.getDirectory("Test2");
        Directory root = cfs.getRoot();

        assertTrue(Directory.equals(dir.getParent(), root));
        assertTrue(Directory.equals(dir2.getParent(), root));
        assertTrue(Directory.equals(dir.getParent(), dir2.getParent()));

        assertFalse(Directory.equals(dir, dir2));

        Directory dir3 = dir.changeDirectory("Test3");
        Directory dir4 = dir2.changeDirectory("Test3");

        assertFalse(Directory.equals(dir3, dir4));
        assertFalse(Directory.equals(cfs.getDirectory("Dir"), cfs.getDirectory(
                "dir")));
    }

    @Test
    public final void testIsRoot() throws IOException
    {
        checkTest(TEST_ROOT);
        System.out.println(TEST_ROOT);
        Directory dir = fileSystem.getDirectory("Test");
        Directory root = fileSystem.getRoot();

        assertFalse(dir.isRoot());
        assertTrue(dir.getParent().isRoot());
        assertTrue(root.isRoot());
        assertTrue(Directory.equals(dir.getParent(), root));
        assertFalse(dir.changeDirectory("Test2").isRoot());
    }
}
