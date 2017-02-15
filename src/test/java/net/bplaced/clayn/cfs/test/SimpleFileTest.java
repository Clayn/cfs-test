package net.bplaced.clayn.cfs.test;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import net.bplaced.clayn.cfs.CFileSystem;
import net.bplaced.clayn.cfs.Directory;
import net.bplaced.clayn.cfs.SimpleFile;
import net.bplaced.clayn.cfs.util.CFiles;
import net.bplaced.clayn.cfs.util.SizeUnit;
import org.junit.Assert;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 */
public abstract class SimpleFileTest extends BaseCFSTest
{

    protected static final String TEST_EXISTS = "exists";
    protected static final String TEST_CREATE = "create";
    protected static final String TEST_DELETE = "delete";
    protected static final String TEST_NAME = "getName";
    protected static final String TEST_PARENT = "getParent";
    protected static final String TEST_SIZE = "getSize";
    protected static final String TEST_READ = "openRead";
    protected static final String TEST_WRITE = "openWrite";
    protected static final String TEST_STRING = "toString";

    protected static final String[] TEST_ALL = new String[]
    {
        TEST_CREATE,
        TEST_DELETE,
        TEST_EXISTS,
        TEST_NAME,
        TEST_PARENT,
        TEST_READ,
        TEST_SIZE,
        TEST_STRING,
        TEST_WRITE
    };

    public SimpleFileTest()
    {
    }

    @Test
    public final void testExists() throws Exception
    {
        checkTest(TEST_EXISTS);
        System.out.println(TEST_EXISTS);
        CFileSystem fs = getFileSystem();
        SimpleFile file = fs.getFile("Test.txt");
        assertNotNull(file);
        assertFalse(file.exists());
        file.create();
        assertTrue(file.exists());
    }

    @Test
    public final void testCreate() throws Exception
    {
        checkTest(TEST_CREATE);
        System.out.println(TEST_CREATE);
        CFileSystem fs = getFileSystem();
        SimpleFile file = fs.getFile("Test.txt");
        assertNotNull(file);
        assertFalse(file.exists());
        file.create();
        assertTrue(file.exists());
    }

    @Test
    public final void testDelete() throws Exception
    {
        checkTest(TEST_DELETE);
        System.out.println(TEST_DELETE);
        CFileSystem fs = getFileSystem();
        SimpleFile file = fs.getFile("Test.txt");
        assertNotNull(file);
        assertFalse(file.exists());
        file.create();
        assertTrue(file.exists());
        file.delete();
        assertFalse(file.exists());
    }

    @Test
    public final void testOpenRead() throws Exception
    {
        checkTest(TEST_READ);
        System.out.println(TEST_READ);
        String text = "Hello World";
        byte[] data = text.getBytes();
        CFileSystem fs = getFileSystem();
        SimpleFile file = fs.getFile("Test.txt");
        file.create();
        try (OutputStream out = file.openWrite())
        {
            out.write(data);
        }
        assertEquals(data.length, file.getSize());
        byte[] read = CFiles.readAllBytes(file);
        assertEquals(data.length, read.length);
        assertArrayEquals(data, read);
        assertEquals(text, new String(read));
    }

    @Test
    public final void testGetName() throws Exception
    {
        checkTest(TEST_NAME);
        System.out.println(TEST_NAME);
        CFileSystem fs = getFileSystem();
        SimpleFile file = fs.getFile("Test.txt");
        SimpleFile file2 = fs.getFile("Test2.txt");
        SimpleFile file3 = fs.getFile("dir/Test.txt");
        assertNotNull(file);
        assertNotNull(file2);
        assertNotNull(file3);

        assertEquals("Test.txt", file.getName());
        assertEquals("Test2.txt", file2.getName());
        assertEquals("Test.txt", file3.getName());

        assertEquals(file.getName(), file3.getName());
        assertNotEquals(file2.getName(), file.getName());
        assertNotEquals(file2.getName(), file3.getName());
    }

    @Test
    public final void testGetParent() throws Exception
    {
        checkTest(TEST_PARENT);
        System.out.println(TEST_PARENT);
        CFileSystem fs = getFileSystem();
        SimpleFile file = fs.getFile("Test.txt");
        SimpleFile file2 = fs.getFile("dir/Test.txt");
        SimpleFile file3 = fs.getFile("Test2.txt");
        List<SimpleFile> files = Arrays.asList(file, file2, file3);
        files.stream().parallel().forEach(Assert::assertNotNull);
        files.stream().parallel().map(SimpleFile::getParent).forEach(
                Assert::assertNotNull);

        Directory dir1 = file.getParent();
        Directory dir2 = file2.getParent();

        assertNotEquals(dir1.getName(), dir2.getName());
        assertNull(dir1.getParent());
        assertNotNull(dir2.getParent());

        dir2 = file3.getParent();
        assertNull(dir2.getParent());
        assertEquals(dir1.getName(), dir2.getName());

    }

    @Test
    public final void testOpenWrite() throws Exception
    {
        checkTest(TEST_WRITE);
        System.out.println(TEST_WRITE);
        byte[] data = "Hello World".getBytes();
        CFileSystem fs = getFileSystem();
        SimpleFile file = fs.getFile("Test.txt");
        file.create();
        int size = data.length;
        try (OutputStream out = file.openWrite())
        {
            out.write(data);
            out.flush();
        }
        assertEquals(size, file.getSize());
    }

    @Test(expected = IOException.class)
    public final void testGetSize() throws Exception
    {
        checkTest(TEST_SIZE);
        System.out.println(TEST_SIZE);
        byte[] data = "Hello World".getBytes();
        CFileSystem fs = getFileSystem();
        SimpleFile file = fs.getFile("Test.txt");
        file.create();
        assertEquals(0, file.getSize());
        int size = data.length;
        try (OutputStream out = file.openWrite())
        {
            out.write(data);
            out.flush();
        }
        assertEquals(size, file.getSize());
        assertEquals(file.getSize(), file.getSize(SizeUnit.BYTE),0.001);
        assertEquals(size/1000.0, file.getSize(SizeUnit.KILO_BYTE),0.001);
        
        file = fs.getFile("Test2.txt");
        file.getSize();
    }

    @Test
    public final void testToString() throws Exception
    {
        checkTest(TEST_STRING);
        System.out.println(TEST_STRING);
        CFileSystem fs = getFileSystem();
        SimpleFile file = fs.getFile("Test.txt");
        SimpleFile file2 = fs.getFile("Test2.txt");
        SimpleFile file3 = fs.getFile("dir/Test.txt");
        List<SimpleFile> files = Arrays.asList(file, file2, file3);

        assertEquals(file.getParent().toString() + file.getName(),
                file.toString());
        assertEquals(file2.getParent().toString() + file2.getName(),
                file2.toString());
        assertEquals(file3.getParent().toString() + file3.getName(),
                file3.toString());

        assertEquals("/Test.txt", file.toString());
        assertEquals("/Test2.txt", file2.toString());
        assertEquals("/dir/Test.txt", file3.toString());
    }

    @Test
    public final void testFileEquals() throws IOException, Exception
    {
        System.out.println("equals");
        CFileSystem cfs = getFileSystem();
        Directory dir = cfs.getDirectory("Test");
        Directory dir2 = cfs.getDirectory("Test2");

        assertFalse(SimpleFile.equals(dir.getFile("Testfile.txt"), dir2.getFile(
                "Testfile.txt")));
        assertTrue(SimpleFile.equals(dir.getFile("Test2.txt"), dir.getFile(
                "Test2.txt")));
        assertFalse(SimpleFile.equals(dir2.getFile("Test3.txt"), dir2.getFile(
                "test3.txt")));

    }
}
