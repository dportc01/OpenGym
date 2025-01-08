package com.example.opengym.Model.DAO;

import java.util.ArrayList;

public interface GenericDAO<T> {

    /**
     * Create an new entry on the database
     * @param entity new entity to add to the database
     * @param parentId id of the parent entity that contains the entity to be updated
     * can be <code>null</code> if it doesn't have it
     * @return row ID or -1 if an error curred
     */
    long create(T entity, long parentId);

    /**
     * Remove an entry from the database
     * @param id of the entry to delete
     * @return numbers of rows affected by the operation
     */
    int delete(long id);

    /**
     * Read all the entries that have the id as a foreign key
     * @param parentId id that is used as a foreign key in the entries of another table
     * @return a list of entries
     */
    ArrayList<T> readAll(long parentId);

    /**
     * Update an entry from the database and assign it the id
     * @param entity new entity with modified values
     * @param id of the entry to be updated
     * @return number of rows affected by the operation
     */
    int update(T entity, long id);

    /**
     * Close the connection opened when creating object
     * that implements the interface.
     */
    void closeConnection();
}
