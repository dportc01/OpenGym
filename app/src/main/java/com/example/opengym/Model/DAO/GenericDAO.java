package com.example.opengym.Model.DAO;

import com.example.opengym.Model.Entities.User;

public interface GenericDAO<T> {

    /**
     * Create an new entry on the database
     * @param entity new entity to add to the database
     * @throws android.database.SQLException if the table doesn't exits in the Database
     * or if the an entry with the same primary key already exists
     */
    void create(T entity) throws android.database.SQLException;

    /**
     * Remove an entry from the database,
     * the implementation should handle identification
     * @param id primary key of the entry
     * @return numbers of rows affected by the operation
     */
    int delete(String id);

    /**
     * Read an entry from the database,
     * @param id primary key of the entry
     * @return an object of the requested type or <code>null</code> if it doesn't exist
     * @throws android.database.sqlite.SQLiteException if the table in the database doesn't exits
     */
    T read(String id) throws android.database.sqlite.SQLiteException;

    /**
     * Update an entry from the database,
     * the implementation should handle identification
     * @param entity new entity with modified values
     * @param id primary key of the entry modify
     * @return number of rows affected by the operation
     */
    int update(T entity, String id);

    /**
     * Close the connection opened when creating object
     * that implements the interface.
     */
    void closeConnection();
}
