package com.distributeddb.reno.filetable;

import java.util.Collection;
import java.util.LinkedList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.distributeddb.errors.FileNotFoundError;
import com.distributeddb.utils.SqlAccess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileTable {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileTable.class);

    /**
     * @param String user - username of account
     * @param String filename - name of file to add
     * @param long fileSize - how large the file is in blocks
     * @return boolean - If the put operation was successful or not
     *
     * Duplicates are updated with the new fileSize
     */
    public static boolean addFile(final String user, final String filename, final long fileSize) {
        Connection conn = null;
        try {
            // Attempt to update existing file
            conn = SqlAccess.getConnection();
            final String update = "update renodata.files set filesize = ? where user = ? AND filename = ?;";
            PreparedStatement statement = conn.prepareStatement(update);
            statement.setLong(1, fileSize);
            statement.setString(2, user);
            statement.setString(3, filename);
            if (statement.executeUpdate() == 1) {
                return true;
            }

            // Insert new file
            final String insert = "insert into renodata.files (filename, filesize, user) values (?, ?, ?);";
            statement = conn.prepareStatement(insert);
            statement.setString(1, filename);
            statement.setLong(2, fileSize);
            statement.setString(3, user);
            if (statement.execute()) {
                return true;
            }
        } catch (SQLException e) {
            LOGGER.error("{}", e.getStackTrace().toString());
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                LOGGER.error("{}", e.getStackTrace().toString());
            }
        }

        return false;
    }

    /**
     * @param String user - username of account
     * @return Collection of FileData that corresponds to the user, returns an empty collection if user does not exist
     */
    public static Collection<FileData> getFiles(final String user) {
        Connection conn = null;
        ResultSet result = null;
        Collection<FileData> files = new LinkedList<>();
        try {
            conn = SqlAccess.getConnection();
            final String update = "select filename, filesize from renodata.files where user = ?;";
            PreparedStatement statement = conn.prepareStatement(update);
            statement.setString(1, user);
            result = statement.executeQuery();
            while (result.next()) {
                final String filename = result.getString("FILENAME");
                final long filesize = result.getLong("FILESIZE");
                files.add(new FileData(filename, filesize));
            }
        } catch (SQLException e) {
            LOGGER.error("{}", e.getStackTrace().toString());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                LOGGER.error("{}", e.getStackTrace().toString());
            }
        }

        return files;
    }

    /**
     * @param String user
     * @param String filename
     * @return FileData block of the file
     * @throws FileNotFoundError if file does not exist for that user
     *
     * Retrieves a singular file
     */
    public static FileData getFile(final String user, final String filename) throws FileNotFoundError {
        Connection conn = null;
        ResultSet result = null;
        try {
            conn = SqlAccess.getConnection();
            final String update = "select filesize from renodata.files where user = ? and filename = ?;";

            PreparedStatement statement = conn.prepareStatement(update);
            statement.setString(1, user);
            statement.setString(2, filename);

            result = statement.executeQuery();
            if (!result.next()) {
                throw new FileNotFoundError(String.format("The file %s was not found", filename));
            }
            
            final long filesize = result.getLong("FILESIZE");
            return new FileData(filename, filesize);
        } catch (SQLException e) {
            LOGGER.error("{}", e.getStackTrace().toString());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                LOGGER.error("{}", e.getStackTrace().toString());
            }
        }

        throw new FileNotFoundError(String.format("The file %s was not found", filename));
    }

    /**
     * @param String user
     * @param String filename
     * @throws FileNotFoundError if file does not exist for that user
     */
    public static boolean removeFile(final String user, final String filename) throws FileNotFoundError {
        Connection conn = null;
        try {
            conn = SqlAccess.getConnection();
            final String update = "delete from renodata.files where user = ? and filename = ?;";

            PreparedStatement statement = conn.prepareStatement(update);
            statement.setString(1, user);
            statement.setString(2, filename);

            if (statement.executeUpdate() == 0) {
                throw new FileNotFoundError(String.format("The file %s was not found", filename));
            }

            return true;
        } catch (SQLException e) {
            LOGGER.error("{}", e.getStackTrace().toString());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                LOGGER.error("{}", e.getStackTrace().toString());
            }
        }
        throw new FileNotFoundError(String.format("The file %s was not found", filename));
    }

    /**
     * @param String user
     * @return boolean if user was successfully removed
     *
     * Removes user and all their files from FileTable
     */
    public static boolean removeUser(final String user) {
        Connection conn = null;
        try {
            conn = SqlAccess.getConnection();
            final String update = "delete from renodata.files where user = ?;";

            PreparedStatement statement = conn.prepareStatement(update);
            statement.setString(1, user);

            if (statement.executeUpdate() == 0) {
                return false;
            }

            return true;
        } catch (SQLException e) {
            LOGGER.error("{}", e.getStackTrace().toString());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                LOGGER.error("{}", e.getStackTrace().toString());
            }
        }

        return false;
    }

    /**
     * @param String user
     * @param String filename
     * @return Boolean if the file exists or not
     */
    public static boolean containsFile(final String user, final String filename) {
        Connection conn = null;
        try {
            conn = SqlAccess.getConnection();
            final String update = "select * from renodata.files where user = ? and filename = ?;";

            PreparedStatement statement = conn.prepareStatement(update);
            statement.setString(1, user);
            statement.setString(2, filename);

            final ResultSet result = statement.executeQuery();
            if (!result.next()) {
                return false;
            }

            return true;
        } catch (SQLException e) {
            LOGGER.error("{}", e.getStackTrace().toString());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                LOGGER.error("{}", e.getStackTrace().toString());
            }
        }
        return false;
    }

}
