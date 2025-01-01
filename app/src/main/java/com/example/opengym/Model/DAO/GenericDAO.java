package com.example.opengym.Model.DAO;

public interface GenericDAO<T> {

    /**
     * Create an new entry on the database
     * @param entity new entity to add to the database
     * @param parentId Foreign key that is used in the primary key,
     * can be <code>null</code> if it doesn't have it
     * @return row ID or -1 if an error curred
     */
    long create(T entity, String parentId);

    /**
     * Remove an entry from the database
     * @param id primary key of the entry
     * @param parentId Foreign key that is used in the primary key,
     * can be <code>null</code> if it doesn't have it
     * @return numbers of rows affected by the operation
     */
    int delete(String id, String parentId);

    /**
     * Read an entry from the database,
     * @param id primary key of the entry
     * @param parentId Foreign key that is used in the primary key,
     * can be null if it doesn't have it
     * @return an object of the requested type or <code>null</code> if the entry doesn't exist
     */
    T read(String id, String parentId);

    /**
     * Update an entry from the database
     * @param entity new entity with modified values
     * @param id primary key of the entry modify
     * @param parentId Foreign key that is used in the primary key,
     * can be null if it doesn't have it
     * @return number of rows affected by the operation
     */
    int update(T entity, String id, String parentId);

    /**
     * Close the connection opened when creating object
     * that implements the interface.
     */
    void closeConnection();
}
