package com.example.opengym.Model.DAO;

import com.example.opengym.Model.Entities.User;

public interface GenericDAO<T> {

    /**
     * Create an new entry on the database,
     * the implementation should handle identification
     * @param entity
     * @throws android.database.SQLException if the table doesn't exits in the Database
     */
    void create(T entity) throws android.database.SQLException;

    /**
     * Remove an entry from the database,
     * the implementation should handle identification
     * @param entity
     * @return numbers of rows affected by the operation
     */
    int delete(T entity);

    /**
     * Read an entry from the database,
     * @param id primary key of the entry
     * @return an object of the requested type or <code>null<code/> if it doesn't exist
     */
    T read(String id);

    /**
     * Update an entry from the database,
     * the implementation should handle identification
     * @param entity
     * @return number of rows affected by the operation
     */
    int update(T entity);

    /**
     * Close the connection opened when creating object
     * that implements the interface.
     */
    void closeConection();
}
