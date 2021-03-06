package cs.ut.ee.fileencryption

import androidx.room.*

@Dao
interface FileDao {
    /**
     * Query for all filenames in the db
     */
    @Query("SELECT name FROM file")
    fun loadFilenames(): Array<String>

    /**
     * Query for all IDs in the db
     */
    @Query("SELECT id FROM file")
    fun loadFileIDs(): Array<Int>

    /**
     * Get all data from a file
     */
    @Query("SELECT * FROM file WHERE name LIKE :first")
    fun findByName(first: String): FileEntity


    /**
     * Insert a new file into the db
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFile(vararg file: FileEntity)

    /**
     * Delete a file from the db
     */
    @Delete
    fun deleteFile(vararg file: FileEntity)

    @Query("DELETE  FROM file")
    fun deleteAll()

}