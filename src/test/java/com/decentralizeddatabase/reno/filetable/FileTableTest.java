package com.decentralizeddatabase.reno.filetable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.Test;

import com.decentralizeddatabase.errors.FileNotFoundError;

public final class FileTableTest {

    private static final String USER = "TEST_USER";
    private static final List<String> FILES = Arrays.asList("file0.file",
                                                            "file1.file");
    private static final long FILE_SIZE = 10000;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        System.setProperty("sql.user", "reno");
        System.setProperty("sql.password", "mangietangie");
        System.setProperty("sql.url", "jdbc:mysql://localhost?useSSL=false");
    }
    
    @After
    public void cleanup() {
        FileTable.removeUser(USER);
    }

    private void addFileHelper(final String user, final String filename, final long fileSize) {
		FileTable.addFile(user, filename, fileSize);
    }

    @Test
    public void testGetFiles() {
		addFileHelper(USER, FILES.get(0), FILE_SIZE);
		addFileHelper(USER, FILES.get(1), FILE_SIZE);

		final Collection<FileData> actual = FileTable.getFiles(USER);
		
		Assert.assertEquals(2, actual.size());

		for (FileData data : actual) {
			Assert.assertTrue(FILES.get(0).equals(data.getFilename()) || FILES.get(1).equals(data.getFilename()));
		}
    }

    @Test
    public void testGetFilesWithDuplicates() {
		addFileHelper(USER, FILES.get(0), FILE_SIZE);
		addFileHelper(USER, FILES.get(0), FILE_SIZE + 5);

		final Collection<FileData> actual = FileTable.getFiles(USER);
		
		Assert.assertEquals(1, actual.size());
		for (FileData file : actual) {
			Assert.assertEquals(FILE_SIZE + 5, file.getFileSize());
			Assert.assertEquals(FILES.get(0), file.getFilename());
		}
    }

    @Test
    public void testGetFilesWithNoFiles() {
		final Collection<FileData> actual = FileTable.getFiles("invalid");

		Assert.assertEquals(0, actual.size());
    }

    @Test
    public void testGetFile() throws FileNotFoundError {
		addFileHelper(USER, FILES.get(0), FILE_SIZE);

		final FileData actual = FileTable.getFile(USER, FILES.get(0));

		Assert.assertEquals(FILES.get(0), actual.getFilename());
		Assert.assertEquals(FILE_SIZE, actual.getFileSize());
    }

    @Test
    public void testGetNonExistantFile() throws FileNotFoundError {
		thrown.expect(FileNotFoundError.class);
		FileTable.getFile(USER, FILES.get(0));

		Assert.fail("No file should have been found");
    }

    @Test
    public void testRemoveFile() throws FileNotFoundError {
		addFileHelper(USER, FILES.get(0), FILE_SIZE);

		final boolean removed = FileTable.removeFile(USER, FILES.get(0));		
		Assert.assertTrue(removed);

		thrown.expect(FileNotFoundError.class);
		FileTable.getFile(USER, FILES.get(0));

		Assert.fail("File still in file table");
    }

    @Test
    public void testRemoveNonExistantFile() throws FileNotFoundError {
		thrown.expect(FileNotFoundError.class);
		FileTable.removeFile(USER, FILES.get(0));

		Assert.fail("FileNotFoundError not thrown");
    }

    @Test
    public void testRemoveUser() {
		addFileHelper(USER, FILES.get(0), FILE_SIZE);
		addFileHelper(USER, FILES.get(1), FILE_SIZE);
		
		final boolean removed = FileTable.removeUser(USER);
		Assert.assertTrue(removed);

		final Collection<FileData> actual = FileTable.getFiles(USER);
		Assert.assertEquals(0, actual.size());
    }
}
